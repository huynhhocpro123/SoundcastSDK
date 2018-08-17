package vn.mobiapps.soundcastsdk.until;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import vn.mobiapps.soundcastsdk.apimanager.APIManager;
import vn.mobiapps.soundcastsdk.apimanager.ApiResponse;
import vn.mobiapps.soundcastsdk.interfaceplayer.MediaListener;

import static android.util.Log.d;

/**
 * Created by NguyenTanHuynh on 7/30/2018.
 */

public class MediaPlayerAudio {
    public static String TAB = MediaPlayerAudio.class.getSimpleName();
    public MediaPlayer mediaPlayerAdvertisement = null;
    private int startTime;
    private int finalTime;
    private Handler handlerAdvertisement = null;
    private Handler handlerPlayAudio = null;
    public MediaPlayer mediaPlayerPlayAudio = null;
    private String linkPlayAudio;
    MediaListener mediaListener;

    public MediaPlayerAudio(MediaListener mediaListener, String linkPlayAudio) {
        try {
            if (mediaListener != null) {
                this.mediaListener = mediaListener;
                this.linkPlayAudio = linkPlayAudio;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public MediaPlayerAudio() {
    }

    public void deytroy() {
        if (mediaPlayerAdvertisement != null) {
            mediaPlayerAdvertisement.release();
            mediaPlayerAdvertisement = null;
        }
        if (UpdateSongTime != null && handlerAdvertisement != null) {
            handlerAdvertisement.removeCallbacks(UpdateSongTime);
            handlerAdvertisement = null;
        }
        if (startRunableAudio != null && handlerPlayAudio != null) {
            handlerPlayAudio.removeCallbacks(startRunableAudio);
            handlerPlayAudio = null;
        }
        startTime = 0;
        finalTime = 0;
        if (mediaPlayerPlayAudio != null) {
            mediaPlayerPlayAudio.release();
            mediaPlayerPlayAudio = null;
        }
    }

    public void playmedia(final String url) {
        try {
            try {
                deytroy();
                mediaListener.showProgress();
                mediaListener.hideKeyBoard();
                mediaPlayerAdvertisement = new MediaPlayer();
                handlerAdvertisement = new Handler();
                mediaPlayerAdvertisement.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayerAdvertisement.setDataSource(url);
                mediaPlayerAdvertisement.prepareAsync();
                mediaPlayerAdvertisement.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                        mediaListener.setBackGroundButtonPause();
                        finalTime = mediaPlayerAdvertisement.getDuration();
                        startTime = mediaPlayerAdvertisement.getCurrentPosition();
                        mediaListener.addStart(startTime, finalTime);
                        APIManager.sendGet(Contanst.URLRQUEST + Contanst.TOKEN + Contanst.START, new ApiResponse() {
                            @Override
                            public void onSuccess(String result) {
                                d(TAB, "Task result 1 :" + result);
                            }

                            @Override
                            public void onError(String error) {

                            }
                        });

                        handlerAdvertisement.postDelayed(UpdateSongTime, 0);
                        mediaListener.hideProgress();
                    }
                });
                mediaPlayerAdvertisement.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        return false;
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayerAdvertisement.getCurrentPosition();
            mediaListener.updateTimeAdvertisement(startTime);
            if (mediaPlayerAdvertisement.isPlaying() == true) {
                mediaListener.setNotEnableButton();
            }
            if (startTime / Contanst.MILISECONDS > 0) {
//                Log.d(TAB, "mediaPlayerAdvertisement.getCurrentPosition() / Contanst.MILISECONDS:???" + startTime / Contanst.MILISECONDS);
                if (startTime / Contanst.MILISECONDS == (finalTime / Contanst.MILISECONDS) / 4) {
//                    Log.d(TAB, "mediaPlayerAdvertisement.getCurrentPosition() / Contanst.MILISECONDS == (totalTime / Contanst.MILISECONDS) / 4:>>>>" + (finalTime / Contanst.MILISECONDS) / 4);
                    mediaListener.addFirstQuartile();
                    mediaListener.showButtonSkip();
                    APIManager.sendGet(Contanst.URLRQUEST + Contanst.TOKEN + Contanst.FIRSTQUARTILE, new ApiResponse() {
                        @Override
                        public void onSuccess(String result) {
                            d(TAB, "Task result 2 :" + result);
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
                } else if (startTime / Contanst.MILISECONDS == (finalTime / Contanst.MILISECONDS) / 2) {
                    mediaListener.addMidPoint();
                    APIManager.sendGet(Contanst.URLRQUEST + Contanst.TOKEN + Contanst.MIDPOINT, new ApiResponse() {
                        @Override
                        public void onSuccess(String result) {
                            d(TAB, "Task result 3 :" + result);
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
                } else if (startTime / Contanst.MILISECONDS == (finalTime * 3 / 4) / Contanst.MILISECONDS) {
                    mediaListener.addThirdQuartile();
                    APIManager.sendGet(Contanst.URLRQUEST + Contanst.TOKEN + Contanst.THIRDQUARTILE, new ApiResponse() {
                        @Override
                        public void onSuccess(String result) {
                            d(TAB, "Task result 4 :" + result);

                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
                }
            }
            handlerAdvertisement.postDelayed(this, Contanst.MILISECONDS);
            if (startTime / Contanst.MILISECONDS == finalTime / Contanst.MILISECONDS) {
                handlerAdvertisement.removeCallbacks(UpdateSongTime);
                mediaListener.addComplete();
                mediaListener.hideButtonSkip();
                playmedias(linkPlayAudio);
                APIManager.sendGet(Contanst.URLRQUEST + Contanst.TOKEN + Contanst.COMPLETE, new ApiResponse() {
                    @Override
                    public void onSuccess(String result) {
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }
        }
    };


    private Runnable startRunableAudio = new Runnable() {
        @Override
        public void run() {
            startTime = mediaPlayerPlayAudio.getCurrentPosition();
            finalTime = mediaPlayerPlayAudio.getDuration();
            mediaListener.updateTimePlayAudio(startTime);
            handlerPlayAudio.postDelayed(this, Contanst.MILISECONDS);
            if (startTime / Contanst.MILISECONDS == finalTime / Contanst.MILISECONDS) {
                handlerPlayAudio.removeCallbacks(startRunableAudio);
                mediaPlayerPlayAudio.release();
                mediaPlayerPlayAudio = null;
                mediaListener.setBackGroundButtonPlay();
            }
        }
    };


    public void playmedias(final String url) {
        try {
            try {
                mediaListener.hideKeyBoard();
                deytroy();
                mediaListener.showProgress();
                handlerPlayAudio = new Handler();
                Log.d(TAB, "Play music 2");
                mediaPlayerPlayAudio = new MediaPlayer();
                mediaPlayerPlayAudio.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayerPlayAudio.setDataSource(url);
                mediaPlayerPlayAudio.prepareAsync();
                mediaListener.setEnableButton();
                mediaPlayerPlayAudio.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                        finalTime = mediaPlayerPlayAudio.getDuration();
                        startTime = mediaPlayerPlayAudio.getCurrentPosition();
                        mediaListener.setTimePlayAudio(startTime, finalTime);
                        mediaListener.hideProgress();
                    }
                });
                mediaPlayerPlayAudio.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        return false;
                    }
                });
                handlerPlayAudio.postDelayed(startRunableAudio, 0);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        if (mediaPlayerPlayAudio != null && mediaPlayerPlayAudio.isPlaying() == true) {
            mediaListener.setBackGroundButtonPause();
            mediaPlayerPlayAudio.pause();
        }
        if (mediaPlayerAdvertisement != null && mediaPlayerAdvertisement.isPlaying() == true) {
            mediaListener.setBackGroundButtonPause();
            mediaPlayerAdvertisement.pause();
        }
    }

    public void start() {
        if (mediaPlayerPlayAudio != null && mediaPlayerPlayAudio.isPlaying() == false) {
            mediaPlayerPlayAudio.start();
        }

        if (mediaPlayerAdvertisement != null && mediaPlayerAdvertisement.isPlaying() == false) {
            mediaPlayerAdvertisement.start();
        }
    }

}
