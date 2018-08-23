package vn.mobiapps.soundcastsdk.until;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import vn.mobiapps.soundcastsdk.apimanager.APIManager;
import vn.mobiapps.soundcastsdk.apimanager.ApiResponse;
import vn.mobiapps.soundcastsdk.interfaceplayer.MediaListener;

import static vn.mobiapps.soundcastsdk.until.Contanst.MILISECONDS;

/**
 * Created by NguyenTanHuynh on 7/30/2018.
 */

public class MediaPlayerAudio {
    public static String TAB = "MediaPlayerAudio";
    public MediaPlayer mediaPlayeAdvertisement = null;
    private int startTime;
    private int finalTime;
    private Handler handlerAdvertisement = null;
    private Handler handlerPlayAudio = null;
    public MediaPlayer mediaPlayAudio = null;
    private String linkPlayAudio;
    private MediaListener mediaListener;

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
        if (mediaPlayeAdvertisement != null) {
            mediaPlayeAdvertisement.release();
            mediaPlayeAdvertisement = null;
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
        if (mediaPlayAudio != null) {
            mediaPlayAudio.release();
            mediaPlayAudio = null;
        }
    }

    public void playAdvertisement(final String url) {
        try {
            deytroy();
            mediaListener.showProgress();
            mediaListener.hideKeyBoard();
            mediaPlayeAdvertisement = new MediaPlayer();
            handlerAdvertisement = new Handler();
            mediaPlayeAdvertisement.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayeAdvertisement.setDataSource(url);
            mediaPlayeAdvertisement.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayeAdvertisement.release();
                }
            });
            mediaPlayeAdvertisement.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    finalTime = mediaPlayeAdvertisement.getDuration();
                    startTime = mediaPlayeAdvertisement.getCurrentPosition();
                    mediaListener.addStart(startTime, finalTime);
                    APIManager.sendGet(Contanst.URLRQUEST + Contanst.TOKEN + Contanst.START, new ApiResponse() {
                        @Override
                        public void onSuccess(String result) {
                            Log.d(TAB, "result 1:" + result.toString());
                            mediaListener.onSuccessStart(result.toString());
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });

                    handlerAdvertisement.postDelayed(UpdateSongTime, 0);
                    mediaListener.hideProgress();
                }
            });
            mediaPlayeAdvertisement.prepareAsync();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e1) {
            e1.printStackTrace();
        } catch (SecurityException e1) {
            e1.printStackTrace();
        } catch (IllegalStateException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayeAdvertisement.getCurrentPosition();
            mediaListener.updateTimeAdvertisement(startTime);
            if (startTime / MILISECONDS == 0) {
                mediaListener.setBackGroundButtonPlay();
            }
            if (mediaPlayeAdvertisement.isPlaying() == true) {
                mediaListener.setNotEnableButton();
            }
            if (startTime / MILISECONDS > 0) {
                if (startTime / MILISECONDS == (finalTime / MILISECONDS) / 4) {
                    mediaListener.addFirstQuartile();
                    mediaListener.showButtonSkip();
                    APIManager.sendGet(Contanst.URLRQUEST + Contanst.TOKEN + Contanst.FIRSTQUARTILE, new ApiResponse() {
                        @Override
                        public void onSuccess(String result) {
                            Log.d(TAB, "result 2:" + result.toString());
                            mediaListener.onSuccessFirstQuartile(result.toString());
                        }

                        @Override
                        public void onError(String error) {
                        }
                    });
                } else if (startTime / MILISECONDS == (finalTime / MILISECONDS) / 2) {
                    mediaListener.addMidPoint();
                    APIManager.sendGet(Contanst.URLRQUEST + Contanst.TOKEN + Contanst.MIDPOINT, new ApiResponse() {
                        @Override
                        public void onSuccess(String result) {
                            Log.d(TAB, "result 3:" + result.toString());
                            mediaListener.onSuccessMidPoint(result.toString());
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
                } else if (startTime / MILISECONDS == (finalTime * 3 / 4) / MILISECONDS) {
                    mediaListener.addThirdQuartile();
                    APIManager.sendGet(Contanst.URLRQUEST + Contanst.TOKEN + Contanst.THIRDQUARTILE, new ApiResponse() {
                        @Override
                        public void onSuccess(String result) {
                            Log.d(TAB, "result 4:" + result.toString());
                            mediaListener.onSuccessThirdQuartile(result.toString());
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
                }
            }
            handlerAdvertisement.postDelayed(this, MILISECONDS);
            if (startTime / MILISECONDS == finalTime / MILISECONDS) {
                handlerAdvertisement.removeCallbacks(UpdateSongTime);
                mediaListener.addComplete();
                mediaListener.hideButtonSkip();
                mediaListener.setBackGroundButtonPause();
                playmedias();
                APIManager.sendGet(Contanst.URLRQUEST + Contanst.TOKEN + Contanst.COMPLETE, new ApiResponse() {
                    @Override
                    public void onSuccess(String result) {
                        Log.d(TAB, "result 5:" + result.toString());
                        mediaListener.onSuccessComplete(result.toString());
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
            startTime = mediaPlayAudio.getCurrentPosition();
            finalTime = mediaPlayAudio.getDuration();
            mediaListener.updateTimePlayAudio(startTime);
            handlerPlayAudio.postDelayed(this, MILISECONDS);
            if (startTime / MILISECONDS == finalTime / MILISECONDS) {
                handlerPlayAudio.removeCallbacks(startRunableAudio);
                mediaPlayAudio.release();
                mediaPlayAudio = null;
                mediaListener.setBackGroundButtonPause();
            }
        }
    };


    public void playmedias() {
        try {
            mediaListener.hideKeyBoard();
            deytroy();
            mediaListener.showProgress();
            handlerPlayAudio = new Handler();
            mediaPlayAudio = new MediaPlayer();
            mediaPlayAudio.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayAudio.setDataSource(linkPlayAudio);
            mediaListener.setEnableButton();
            mediaPlayAudio.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayAudio.release();
                }
            });
            mediaPlayAudio.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    finalTime = mediaPlayAudio.getDuration();
                    startTime = mediaPlayAudio.getCurrentPosition();
                    mediaListener.setTimePlayAudio(startTime, finalTime);
                    handlerPlayAudio.postDelayed(startRunableAudio, 0);
                    mediaListener.hideProgress();
                }
            });
            mediaPlayAudio.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e1) {
            e1.printStackTrace();
        } catch (SecurityException e1) {
            e1.printStackTrace();
        } catch (IllegalStateException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        if (mediaPlayAudio != null && mediaPlayAudio.isPlaying() == true) {
            mediaPlayAudio.pause();
        }

        if (mediaPlayeAdvertisement != null && mediaPlayeAdvertisement.isPlaying() == true) {
            mediaPlayeAdvertisement.pause();
        }
    }

    public void start() {
        if (mediaPlayAudio != null && mediaPlayAudio.isPlaying() == false) {
            mediaListener.setBackGroundButtonPlay();
            mediaPlayAudio.start();
        }

        if (mediaPlayeAdvertisement != null && mediaPlayeAdvertisement.isPlaying() == false) {
            mediaListener.setBackGroundButtonPlay();
            mediaPlayeAdvertisement.start();
        }
    }

}
