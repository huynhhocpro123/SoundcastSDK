package vn.mobiapps.soundcastsdk.apimanager;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import vn.mobiapps.soundcastsdk.model.modeldata.DataParserXMLModel;

import static vn.mobiapps.soundcastsdk.until.Contanst.DATA;
import static vn.mobiapps.soundcastsdk.until.Contanst.ERROR;
import static vn.mobiapps.soundcastsdk.until.Contanst.ERRORParserXML;
import static vn.mobiapps.soundcastsdk.until.Contanst.ERRORTIMEOUT;

/**
 * Created by soundcast on 7/19/2018.
 */

public class APIManager {

    public static void sendGet(final int timeout, final String url, final ApiResponse listener) {
        (new Thread() {
            @Override
            public void run() {
                // super.run();
                try {
                    HttpURLConnection urlConnection = null;
                    URL obj = new URL(url);
                    urlConnection = (HttpURLConnection) obj.openConnection();
                    if (timeout == 5000) {
                        urlConnection.setConnectTimeout(timeout);
                    }
                    urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
                    int responseCode = urlConnection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = null;
                        StringBuffer response = new StringBuffer();
                        try {
                            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                            String line = "";
                            while ((line = reader.readLine()) != null) {
                                response.append(line);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (reader != null) {
                                try {
                                    reader.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        listener.onSuccess(response.toString());
                    } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                        listener.onError(ERROR);
                    } else {
                        listener.onError(ERRORTIMEOUT);
                    }

                } catch (final SocketTimeoutException e) {
                    e.printStackTrace();
                    listener.onError(ERRORTIMEOUT);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        ).start();
    }

    // HTTP POST request
    public static <T> void sendPost(final T Object, final String url, final ApiResponse listener) {
        (new Thread() {
            @Override
            public void run() {
                // super.run();
                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                try {
                    try {
                        HttpURLConnection urlConnection = null;
                        URL obj = new URL(url);
                        urlConnection = (HttpsURLConnection) obj.openConnection();

                        //add reuqest header
                        urlConnection.setRequestMethod("POST");
                        urlConnection = (HttpsURLConnection) obj.openConnection();
                        urlConnection.setRequestProperty("Content-Type", "application/json");
                        urlConnection.setRequestProperty("Accept", "application/json");
                        urlConnection.setDoOutput(true);
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
                        writer.write(gson.toJson(Object));
                        writer.flush();
                        writer.close();
                        int responseCode = urlConnection.getResponseCode();
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            BufferedReader reader = null;
                            StringBuffer response = new StringBuffer();
                            try {
                                reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                                String line = "";
                                while ((line = reader.readLine()) != null) {
                                    response.append(line);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                if (reader != null) {
                                    try {
                                        reader.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            listener.onSuccess(response.toString());
                        } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                            listener.onError(ERROR);
                        } else {
                            listener.onError("ERROR");
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        ).start();
    }

    public static void sendGetVast(final String url, final ApiResponse.ApiResponeModelVast listener) {
        (new Thread() {
            @Override
            public void run() {
                // super.run();
                try {
                    Map<String, String> data = null;
                    String intension = "";
                    String content = "";
                    String impressionTrack = "";
                    String clickTrack = "";
                    String clickThrough = "";
                    DataParserXMLModel model = null;
                    InputStream inputStream;
                    ArrayList<String> listLink = null;
//                    URL obj = new URL(url);
                    URL obj = new URL("http://x3.instreamatic.com/v2/daast/777.xml?preview=11");
                    HttpURLConnection urlConnection = (HttpURLConnection) obj.openConnection();
                    urlConnection.connect();
                    inputStream = urlConnection.getInputStream();
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser xmlPullParser = factory.newPullParser();
                    xmlPullParser.setInput(inputStream, null);
                    listLink = new ArrayList<>();
                    int event = xmlPullParser.getEventType();
                    int responseCode = urlConnection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        String xmltag;
                        String attr = "";
                        String key = "";
                        data = new HashMap<String, String>();
                        while (event != XmlPullParser.END_DOCUMENT) {
                            xmltag = xmlPullParser.getName();
                            switch (event) {
                                case XmlPullParser.START_TAG:
                                    attr = xmlPullParser.getAttributeValue(null, "event");
                                    if (xmltag.equalsIgnoreCase("Tracking")) {
                                        if (!attr.equals("")) {
                                            key = attr;
                                        }
                                    }
                                    break;
                                case XmlPullParser.TEXT:
                                    intension = xmlPullParser.getText().trim();
                                    break;
                                case XmlPullParser.END_TAG:
                                    if (xmltag.equals("Duration")) {
                                        content = intension;
                                    } else if (xmltag.equals("MediaFile")) {
                                        listLink.add(intension);
                                    } else if (xmltag.equals("Impression")) {
                                        impressionTrack = intension;
                                    } else if (xmltag.equals("ClickTracking")) {
                                        clickTrack = intension;
                                    } else if (xmltag.equals("ClickThrough")) {
                                        clickThrough = intension;
                                    } else if (xmltag.equalsIgnoreCase("Tracking") && !attr.equals("")) {
                                        data.put(key, intension);
                                    }
                                    break;
                            }
                            event = xmlPullParser.next();
                        }
                        model = new DataParserXMLModel();
                        model.time = content;
                        model.link = listLink;
                        model.impressionTrack = impressionTrack;
                        model.clickTrack = clickTrack;
                        model.clickThrough = clickThrough;
                        if (data != null) {
                            DATA = data;
                        }
                        if (model != null) {
                            listener.onSuccess(model);
                        } else {
                            listener.onError(ERRORParserXML);
                        }
                    } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                        listener.onError(ERROR);
                    } else {
                        listener.onError(ERRORTIMEOUT);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        }
        ).start();
    }
}
