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
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import vn.mobiapps.soundcastsdk.model.modeldata.DataParserXMLModel;

/**
 * Created by NguyenTanHuynh on 7/19/2018.
 */

public class APIManager {

    public static void sendGet(final String url, final ApiResponse listener) {
        (new Thread() {
            @Override
            public void run() {
                // super.run();
                try {
                    try {
                        HttpURLConnection urlConnection = null;
                        URL obj = new URL(url);
                        urlConnection = (HttpURLConnection) obj.openConnection();
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
                        } else {
                            listener.onError("Error");
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
                        } else {
                            listener.onError("Error");
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
                    String intension = "";
                    String content = "";
                    String impressionTrack = "";
                    String clickTrack = "";
                    String clickThrough = "";
                    InputStream inputStream;
                    ArrayList<String> listLink;
                    URL obj = new URL(url);
                    HttpURLConnection urlConnection = (HttpURLConnection) obj.openConnection();
                    urlConnection.connect();
                    inputStream = urlConnection.getInputStream();
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser xmlPullParser = factory.newPullParser();
                    xmlPullParser.setInput(inputStream, null);
                    listLink = new ArrayList<>();
                    int event = xmlPullParser.getEventType();
                    while (event != XmlPullParser.END_DOCUMENT) {
                        switch (event) {
                            case XmlPullParser.START_TAG:
                                break;
                            case XmlPullParser.TEXT:
                                intension = xmlPullParser.getText().trim();
                                break;
                            case XmlPullParser.END_TAG:
                                String thedong = xmlPullParser.getName();
                                if (thedong.equals("Duration")) {
                                    content = intension;
                                } else if (thedong.equals("MediaFile")) {
                                    listLink.add(intension);
                                } else if (thedong.equals("Impression")) {
                                    impressionTrack = intension;
                                } else if (thedong.equals("ClickTracking")) {
                                    clickTrack = intension;
                                } else if (thedong.equals("ClickThrough")) {
                                    clickThrough = intension;
                                }
                                break;
                        }
                        event = xmlPullParser.next();
                    }
                    DataParserXMLModel model = new DataParserXMLModel();
                    model.time = content;
                    model.link = listLink;
                    model.impressionTrack = impressionTrack;
                    model.clickTrack = clickTrack;
                    model.clickThrough = clickThrough;
                    if (model != null) {
                        listener.onSuccess(model);
                    } else {
                        listener.onError("Error null");
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
