package vn.mobiapps.soundcast.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import vn.mobiapps.soundcast.R;
import vn.mobiapps.soundcastsdk.interfaceplayer.MediaListener;
import vn.mobiapps.soundcastsdk.model.modeldata.DataParserXMLModel;
import vn.mobiapps.soundcastsdk.model.modeldata.ModelResponse;
import vn.mobiapps.soundcastsdk.model.modelview.RequestModel;
import vn.mobiapps.soundcastsdk.presenter.ISoundCastPresenter;
import vn.mobiapps.soundcastsdk.presenter.SoundCastPresenterImpl;
import vn.mobiapps.soundcastsdk.until.SoundCastManager;
import vn.mobiapps.soundcastsdk.view.ISourdCastViewListener;

import static vn.mobiapps.soundcastsdk.until.Contanst.CHECKAUDIO;
import static vn.mobiapps.soundcastsdk.until.Contanst.SECONDS;
import static vn.mobiapps.soundcastsdk.until.Contanst.TOKEN;
import static vn.mobiapps.soundcastsdk.until.Contanst.URLADVERTISEMENT;
import static vn.mobiapps.soundcastsdk.until.Utils.TimeFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ISourdCastViewListener, MediaListener {
    private ISoundCastPresenter presenter;
    private Button startPlayerBtn;
    private EditText edtNet, edtSite, edTag;
    private TextView timeStart, timefinish, txtSkip;
    private SeekBar seekbarSong;
    private LinearLayout progress;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;
    private ListView listView;
    private CheckBox checkbox, checkboxMidscroll;
    private SoundCastManager manager;
    private String linkMP3PlayAudido = "https://demo-stg.soundcast.fm/assets/audio/going-blind-court_1.mp3";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        addControl();
        setOnClickListener();

    }

    private void init() {
        if (presenter == null) {
            presenter = new SoundCastPresenterImpl(this);
        }
    }

    private void setOnClickListener() {
        progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        startPlayerBtn.setOnClickListener(this);
        txtSkip.setOnClickListener(this);
        seekbarSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (manager.mediaPlayeAdvertisement == null && manager.mediaPlayAudio != null) {
                    manager.mediaPlayAudio.seekTo(seekBar.getProgress());
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        manager.start();
        hideKeyBoard();
    }

    private void addControl() {
        startPlayerBtn = (Button) findViewById(R.id.startPlayerBtn);
        edtNet = (EditText) findViewById(R.id.edtNet);
        edtSite = (EditText) findViewById(R.id.edtSite);
        edTag = (EditText) findViewById(R.id.edTag);
        timefinish = (TextView) findViewById(R.id.timefinish);
        timeStart = (TextView) findViewById(R.id.timeStart);
        seekbarSong = (SeekBar) findViewById(R.id.seekbarSong);
        progress = (LinearLayout) findViewById(R.id.progress);
        listView = (ListView) findViewById(R.id.listView);
        checkbox = (CheckBox) findViewById(R.id.checkbox);
        checkboxMidscroll = (CheckBox) findViewById(R.id.checkboxMidscroll);
        txtSkip = (TextView) findViewById(R.id.txtSkip);
        arrayList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);

        //Play Audio
        manager = new SoundCastManager(this, linkMP3PlayAudido);
    }

    public void showProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progress.setVisibility(View.VISIBLE);
            }
        });
    }

    public void hideProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progress.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void midCroll(boolean check, int second) {
        CHECKAUDIO = check;
        SECONDS = second;
    }

    @Override
    public void onSuccessParserXML(final DataParserXMLModel data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (data != null) {
                    updateData(data);
                }
            }
        });
    }

    @Override
    public void onErrorParserXML(final String data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (data != null) {
                    Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void responeData(final String data) {
        try {
            if (data != null) {
                ModelResponse resultObject = new Gson().fromJson(data, ModelResponse.class);
                TOKEN = resultObject.getToken();
                presenter.sendGetVastXML(resultObject.getCreative().getCreativeView().getUrl());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccessSendGetURL(final String data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                responeData(data);
            }
        });
    }

    @Override
    public void onErrorSendGetURL(final String data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (data != null) {
                    Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startPlayerBtn:
                if (manager.mediaPlayeAdvertisement != null && manager.mediaPlayeAdvertisement.isPlaying() == true) {
                    manager.pause();
                } else if (manager.mediaPlayeAdvertisement != null && manager.mediaPlayeAdvertisement.isPlaying() == false) {
                    manager.start();
                } else if (manager.mediaPlayAudio != null && manager.mediaPlayAudio.isPlaying() == true) {
                    manager.pause();
                } else if (manager.mediaPlayAudio != null && manager.mediaPlayAudio.isPlaying() == false) {
                    manager.start();
                } else if (edtNet.getText().toString().length() > 0 && edtSite.getText().toString().length() > 0 && edTag.getText().toString().length() > 0) {
                    if (checkbox.isChecked() && checkboxMidscroll.isChecked() == false) {
                        sendPresenter();
                    } else if (checkbox.isChecked() && checkboxMidscroll.isChecked()) {
                        midCroll(true, 15);
                        sendPresenter();
                    } else {
                        if (arrayList != null) {
                            arrayList.clear();
                        }
                        arrayList.add(getString(R.string.adcall));
                        arrayList.add(getString(R.string.Error));
                        arrayList.add(getString(R.string.audio));
                        adapter.notifyDataSetChanged();
                        manager.playmedias();
                    }
                }
                break;
            case R.id.txtSkip:
                manager.skipAuio();
                break;
        }
    }

    private void sendPresenter() {
        RequestModel requestModel = new RequestModel();
        requestModel.networkID = edtNet.getText().toString();
        requestModel.siteID = edtSite.getText().toString();
        requestModel.tagID = edTag.getText().toString();
        requestModel.pageTitle = "NRJ";
        requestModel.pageDescription = "null";
        requestModel.keywords = "null";
        requestModel.pageUrl = "https%3A%2F%2Fdemo-stg.soundcast.fm%2F";
        requestModel.tags = "null";
        requestModel.test = "true";
        presenter.loadAd(requestModel);
    }


    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    public void updateData(final DataParserXMLModel model) {
        try {
            if (model.link.get(0).toString() != null) {
                URLADVERTISEMENT = model.link.get(0).toString();
                manager.playAdvertisement(model.link.get(0).toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setBackGroundButtonPlay() {
        startPlayerBtn.setBackgroundResource(R.drawable.play); /**/
    }

    @Override
    public void setBackGroundButtonPause() {
        startPlayerBtn.setBackgroundResource(R.drawable.pause);
    }

    @Override
    public void showButtonSkip() {
        txtSkip.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideButtonSkip() {
        txtSkip.setVisibility(View.GONE);
    }

    @Override
    public void hideKeyBoard() {
    }

    @Override
    public void callAd(int start, int finish) {
        if (arrayList != null) {
            arrayList.clear();
        }
        arrayList.add(getString(R.string.callAd));
        adapter.notifyDataSetChanged();
        seekbarSong.setMax(finish);
        timeStart.setText(TimeFormat(start));
        timefinish.setText(TimeFormat(finish));
        seekbarSong.setProgress(start);
    }

    @Override
    public void addStart() {
        arrayList.add(getString(R.string.start));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addFirstQuartile() {
        arrayList.add(getString(R.string.firstquartile));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addMidPoint() {
        arrayList.add(getString(R.string.midpoint));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addThirdQuartile() {
        arrayList.add(getString(R.string.thirdquartile));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addComplete() {
        arrayList.add(getString(R.string.complete));
        adapter.notifyDataSetChanged();
        listView.setSelection(arrayList.size());
    }

    @Override
    public void updateTimeAdvertisement(int Start) {
        timeStart.setText(TimeFormat(Start));
        seekbarSong.setProgress(Start);
    }

    @Override
    public void setTimePlayAudio(int start, int finish) {
        seekbarSong.setMax(finish);
        timeStart.setText(TimeFormat(start));
        timefinish.setText(TimeFormat(finish));
        seekbarSong.setProgress(start);
    }

    @Override
    public void updateTimePlayAudio(int start) {
        timeStart.setText(TimeFormat(start));
        seekbarSong.setProgress(start);
    }
}
