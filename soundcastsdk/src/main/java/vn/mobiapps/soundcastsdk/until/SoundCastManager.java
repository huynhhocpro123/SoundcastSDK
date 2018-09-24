package vn.mobiapps.soundcastsdk.until;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;

import java.io.IOException;

import vn.mobiapps.soundcastsdk.apimanager.APIManager;
import vn.mobiapps.soundcastsdk.apimanager.ApiResponse;
import vn.mobiapps.soundcastsdk.interfaceplayer.MediaListener;

import static vn.mobiapps.soundcastsdk.until.Contanst.CHECKAUDIO;
import static vn.mobiapps.soundcastsdk.until.Contanst.COMPLETE;
import static vn.mobiapps.soundcastsdk.until.Contanst.DATA;
import static vn.mobiapps.soundcastsdk.until.Contanst.EVENTCOMPLETE;
import static vn.mobiapps.soundcastsdk.until.Contanst.EVENTFIRSTQUARTILE;
import static vn.mobiapps.soundcastsdk.until.Contanst.EVENTMIDPOINT;
import static vn.mobiapps.soundcastsdk.until.Contanst.EVENTPAUSE;
import static vn.mobiapps.soundcastsdk.until.Contanst.EVENTRESUME;
import static vn.mobiapps.soundcastsdk.until.Contanst.EVENTSKIP;
import static vn.mobiapps.soundcastsdk.until.Contanst.EVENTSTART;
import static vn.mobiapps.soundcastsdk.until.Contanst.EVENTTHIRDQUARTILE;
import static vn.mobiapps.soundcastsdk.until.Contanst.MIDPOINT;
import static vn.mobiapps.soundcastsdk.until.Contanst.MILISECONDS;
import static vn.mobiapps.soundcastsdk.until.Contanst.SECONDS;
import static vn.mobiapps.soundcastsdk.until.Contanst.SKIP;
import static vn.mobiapps.soundcastsdk.until.Contanst.THIRDQUARTILE;
import static vn.mobiapps.soundcastsdk.until.Contanst.TIMEOUT;
import static vn.mobiapps.soundcastsdk.until.Contanst.TOKEN;
import static vn.mobiapps.soundcastsdk.until.Contanst.URLADVERTISEMENT;
import static vn.mobiapps.soundcastsdk.until.Contanst.URLRQUEST;

/**
 * Created by soundcast  on 7/30/2018.
 */

public class SoundCastManager {
    public MediaPlayer mediaPlayeAdvertisement = null;
    private int startTime;
    private int startPlayAudio;
    private int finalTime;
    private int finalPlayAudio;
    private Handler handlerAdvertisement = null;
    private Handler handlerPlayAudio = null;
    public MediaPlayer mediaPlayAudio = null;
    private String linkPlayAudio;
    private MediaListener mediaListener;
    private boolean checkSecons = false;


    public SoundCastManager(MediaListener mediaListener, String linkPlayAudio) {
        try {
            if (mediaListener != null) {
                this.mediaListener = mediaListener;
                this.linkPlayAudio = linkPlayAudio;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SoundCastManager() {
    }

    public void playAdvertisement(final String url) {
        try {
            mediaListener.showProgress();
            mediaListener.hideKeyBoard();
            mediaPlayeAdvertisement = new MediaPlayer();
            handlerAdvertisement = new Handler();
            mediaPlayeAdvertisement.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayeAdvertisement.setDataSource(url);
            mediaPlayeAdvertisement.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    finalTime = mediaPlayeAdvertisement.getDuration();
                    startTime = mediaPlayeAdvertisement.getCurrentPosition();
                    mediaListener.callAd(startTime, finalTime);
                    if (startTime / MILISECONDS == 0) {
                        StartAdvertisement();
                    }
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
            try {
                startTime = mediaPlayeAdvertisement.getCurrentPosition();
                mediaListener.updateTimeAdvertisement(startTime);
                if (startTime == 0) {
                    mediaListener.setBackGroundButtonPlay();
                }
                if (startTime > 0) {
                    if (startTime / MILISECONDS == finalTime / MILISECONDS / 4) {
                        FirstQuarTileAdvertisement();
                        mediaListener.showButtonSkip();
                    } else if (startTime / MILISECONDS == (finalTime / MILISECONDS) / 2) {
                        MidpointAdvertisement();
                    } else if (startTime / MILISECONDS == (finalTime) * (3 / 4) / MILISECONDS) {
                        ThirdQuarTileAdvertisement();
                    }
                }
                handlerAdvertisement.postDelayed(this, MILISECONDS);
                if (startTime / MILISECONDS == finalTime / MILISECONDS) {
                    mediaPlayeAdvertisementRelease();
                    CompleteAdvertisement();
                    mediaListener.hideButtonSkip();
                    mediaListener.setBackGroundButtonPause();
                    if (checkSecons) {
                        updateStatusAudio();
                    } else {
                        playmedias();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };


    private Runnable startRunableAudio = new Runnable() {
        @Override
        public void run() {
            try {
                startPlayAudio = mediaPlayAudio.getCurrentPosition();
                finalPlayAudio = mediaPlayAudio.getDuration();
                mediaListener.updateTimePlayAudio(startPlayAudio);
//                mediaListener.setEnableButton();
                handlerPlayAudio.postDelayed(this, MILISECONDS);
                if (startPlayAudio == 0) {
                    mediaListener.setBackGroundButtonPlay();
                }
                if (CHECKAUDIO) {
                    if (startPlayAudio / MILISECONDS == SECONDS) {
                        mediaPlayeAdvertisementRelease();
                        checkSecons = true;
                        if (startRunableAudio != null && handlerPlayAudio != null) {
                            handlerPlayAudio.removeCallbacks(startRunableAudio);
                            handlerPlayAudio = null;
                        }
                        pause();
                        CHECKAUDIO = false;
                        playAdvertisement(URLADVERTISEMENT);
                    }
                }
                if (startPlayAudio / MILISECONDS == finalPlayAudio / MILISECONDS) {
                    mediaPlayAudioRelease();
                    mediaPlayeAdvertisementRelease();
                    CHECKAUDIO = false;
                    SECONDS = 0;
                    checkSecons = false;
                    mediaListener.setBackGroundButtonPause();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void updateStatusAudio() {
        mediaListener.hideKeyBoard();
        mediaListener.showProgress();
        mediaListener.setBackGroundButtonPlay();
        CHECKAUDIO = false;
        checkSecons = false;
        if (mediaPlayAudio != null && mediaPlayAudio.isPlaying() == false) {
            mediaListener.setBackGroundButtonPlay();
            mediaPlayAudio.start();
        }
        handlerPlayAudio = new Handler();
        finalPlayAudio = mediaPlayAudio.getDuration();
        startPlayAudio = mediaPlayAudio.getCurrentPosition();
        mediaListener.setTimePlayAudio(startPlayAudio, finalPlayAudio);
        handlerPlayAudio.postDelayed(startRunableAudio, 0);
        mediaListener.hideProgress();
    }

    private void updateStatusAdvertisement() {
        mediaListener.hideKeyBoard();
        mediaListener.showProgress();
        mediaListener.setBackGroundButtonPlay();
        if (mediaPlayeAdvertisement != null && mediaPlayeAdvertisement.isPlaying() == false) {
            mediaListener.setBackGroundButtonPlay();
            mediaPlayeAdvertisement.start();
        }
        handlerAdvertisement = new Handler();
        finalTime = mediaPlayeAdvertisement.getDuration();
        startTime = mediaPlayeAdvertisement.getCurrentPosition();
        mediaListener.setTimePlayAudio(startTime, finalTime);
        handlerAdvertisement.postDelayed(UpdateSongTime, 0);
        mediaListener.hideProgress();
    }


    public void playmedias() {
        try {
            mediaListener.hideKeyBoard();
            mediaListener.showProgress();
            handlerPlayAudio = new Handler();
            mediaPlayAudio = new MediaPlayer();
            mediaPlayAudio.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayAudio.setDataSource(linkPlayAudio);
//            mediaListener.setEnableButton();
            mediaPlayAudio.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    finalPlayAudio = mediaPlayAudio.getDuration();
                    startPlayAudio = mediaPlayAudio.getCurrentPosition();
                    mediaListener.setTimePlayAudio(startPlayAudio, finalPlayAudio);
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
        try {
            if (mediaPlayAudio != null && mediaPlayAudio.isPlaying() == true) {
                mediaPlayAudio.pause();
                mediaListener.setBackGroundButtonPause();
            }

            if (mediaPlayeAdvertisement != null && mediaPlayeAdvertisement.isPlaying() == true) {
                PauseAdvertisement();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            if (mediaPlayAudio != null && mediaPlayAudio.isPlaying() == false) {
                mediaListener.setBackGroundButtonPlay();
                mediaPlayAudio.start();
            }

            if (mediaPlayeAdvertisement != null && mediaPlayeAdvertisement.isPlaying() == false) {
                ResumeAdvertisement();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void mediaPlayeAdvertisementRelease() {
        if (mediaPlayeAdvertisement != null) {
            mediaPlayeAdvertisement.release();
            mediaPlayeAdvertisement = null;
        }
        if (UpdateSongTime != null && handlerAdvertisement != null) {
            handlerAdvertisement.removeCallbacks(UpdateSongTime);
            handlerAdvertisement = null;
        }
    }

    public void mediaPlayAudioRelease() {
        if (mediaPlayAudio != null) {
            mediaPlayAudio.release();
            mediaPlayAudio = null;
        }
        if (startRunableAudio != null && handlerPlayAudio != null) {
            handlerPlayAudio.removeCallbacks(startRunableAudio);
            handlerPlayAudio = null;
        }
    }

    public void skipAuio() {
        mediaPlayeAdvertisementRelease();
        mediaListener.hideButtonSkip();
        mediaListener.setBackGroundButtonPause();
        if (checkSecons) {
            SkipAdvertisement();
            updateStatusAudio();
        } else {
            SkipAdvertisement();
            playmedias();
        }
    }

    private void StartAdvertisement() {
        mediaListener.addStart();
        APIManager.sendGet(TIMEOUT, URLRQUEST + TOKEN + Contanst.START, new ApiResponse() {
            @Override
            public void onSuccess(String result) {
                System.out.println("result StartTOKEN =  " + result.toString());
            }

            @Override
            public void onError(String error) {
                System.out.println("error StartTOKEN =  " + error.toString());
            }
        });
        if (DATA != null && DATA.get(EVENTSTART) != null) {
            APIManager.sendGet(TIMEOUT, String.valueOf(DATA.get(EVENTSTART)), new ApiResponse() {
                @Override
                public void onSuccess(String result) {
                    System.out.println("result Start =  " + result.toString());
                }

                @Override
                public void onError(String error) {
                    System.out.println("error Start =  " + error.toString());
                }
            });
        }
    }

    private void MidpointAdvertisement() {
        mediaListener.addMidPoint();
        APIManager.sendGet(TIMEOUT, URLRQUEST + TOKEN + MIDPOINT, new ApiResponse() {
            @Override
            public void onSuccess(String result) {
                System.out.println("result MidpointTOKEN =  " + result.toString());
            }

            @Override
            public void onError(String error) {
                System.out.println("error MidpointTOKEN =  " + error.toString());

            }
        });
        if (DATA != null && DATA.get(EVENTMIDPOINT) != null) {
            APIManager.sendGet(TIMEOUT, String.valueOf(DATA.get(EVENTMIDPOINT)), new ApiResponse() {
                @Override
                public void onSuccess(String result) {
                    System.out.println("result Midpoint =  " + result.toString());
                }

                @Override
                public void onError(String error) {
                    System.out.println("error Midpoint =  " + error.toString());

                }
            });
        }
    }

    private void FirstQuarTileAdvertisement() {
        mediaListener.addFirstQuartile();
        APIManager.sendGet(TIMEOUT, URLRQUEST + TOKEN + Contanst.FIRSTQUARTILE, new ApiResponse() {
            @Override
            public void onSuccess(String result) {
                System.out.println("result FirstQuarTileTOKEN =  " + result.toString());
            }

            @Override
            public void onError(String error) {
                System.out.println("error FirstQuarTileTOKEN =  " + error.toString());
            }
        });
        if (DATA != null && DATA.get(EVENTFIRSTQUARTILE) != null) {
            APIManager.sendGet(TIMEOUT, String.valueOf(DATA.get(EVENTFIRSTQUARTILE)), new ApiResponse() {
                @Override
                public void onSuccess(String result) {
                    System.out.println("result FirstQuarTile =  " + result.toString());
                }

                @Override
                public void onError(String error) {
                    System.out.println("error FirstQuarTile =  " + error.toString());
                }
            });
        }
    }

    private void ThirdQuarTileAdvertisement() {
        mediaListener.addThirdQuartile();
        APIManager.sendGet(TIMEOUT, URLRQUEST + TOKEN + THIRDQUARTILE, new ApiResponse() {
            @Override
            public void onSuccess(String result) {
                System.out.println("result ThirdQuarTileTOKEN =  " + result.toString());
            }

            @Override
            public void onError(String error) {
                System.out.println("error ThirdQuarTileTOKEN =  " + error.toString());
            }
        });
        if (DATA != null && DATA.get(EVENTTHIRDQUARTILE) != null) {
            APIManager.sendGet(TIMEOUT, String.valueOf(DATA.get(EVENTTHIRDQUARTILE)), new ApiResponse() {
                @Override
                public void onSuccess(String result) {
                    System.out.println("result ThirdQuarTile =  " + result.toString());
                }

                @Override
                public void onError(String error) {
                    System.out.println("error ThirdQuarTile =  " + error.toString());
                }
            });
        }
    }

    private void CompleteAdvertisement() {
        mediaListener.addComplete();
        APIManager.sendGet(TIMEOUT, URLRQUEST + TOKEN + COMPLETE, new ApiResponse() {
            @Override
            public void onSuccess(String result) {
                System.out.println("result CompleteTOKEN =  " + result.toString());
            }

            @Override
            public void onError(String error) {
                System.out.println("error CompleteTOKEN =  " + error.toString());

            }
        });
        if (DATA != null && DATA.get(EVENTCOMPLETE) != null) {
            APIManager.sendGet(TIMEOUT, String.valueOf(DATA.get(EVENTCOMPLETE)), new ApiResponse() {
                @Override
                public void onSuccess(String result) {
                    System.out.println("result Complete =  " + result.toString());
                }

                @Override
                public void onError(String error) {
                    System.out.println("error Complete =  " + error.toString());

                }
            });
        }
    }

    private void SkipAdvertisement() {
        APIManager.sendGet(TIMEOUT, URLRQUEST + TOKEN + SKIP, new ApiResponse() {
            @Override
            public void onSuccess(String result) {
                System.out.println("result SkipTOKEN =  " + result.toString());
            }

            @Override
            public void onError(String error) {
                System.out.println("error SkipTOKEN =  " + error.toString());
            }
        });
        if (DATA != null && DATA.get(EVENTSKIP) != null) {
            APIManager.sendGet(TIMEOUT, String.valueOf(DATA.get(EVENTSKIP)), new ApiResponse() {
                @Override
                public void onSuccess(String result) {
                    System.out.println("result Skip =  " + result.toString());
                }

                @Override
                public void onError(String error) {
                    System.out.println("error Skip =  " + error.toString());
                }
            });
        }
    }

    private void PauseAdvertisement() {
        mediaPlayeAdvertisement.pause();
        mediaListener.setBackGroundButtonPause();
        if (UpdateSongTime != null && handlerAdvertisement != null) {
            handlerAdvertisement.removeCallbacks(UpdateSongTime);
            handlerAdvertisement = null;
        }
        if (DATA != null && DATA.get(EVENTPAUSE) != null) {
            APIManager.sendGet(TIMEOUT, String.valueOf(DATA.get(EVENTPAUSE)), new ApiResponse() {
                @Override
                public void onSuccess(String result) {
                    System.out.println("result Pause =  " + result.toString());
                }

                @Override
                public void onError(String error) {
                    System.out.println("error Pause =  " + error.toString());
                }
            });
        }
    }

    private void ResumeAdvertisement() {
        mediaListener.setBackGroundButtonPlay();
        mediaPlayeAdvertisement.start();
        updateStatusAdvertisement();
        if (DATA != null && DATA.get(EVENTRESUME) != null) {
            APIManager.sendGet(TIMEOUT, String.valueOf(DATA.get(EVENTRESUME)), new ApiResponse() {
                @Override
                public void onSuccess(String result) {
                    System.out.println("result Resume =  " + result.toString());
                }

                @Override
                public void onError(String error) {
                    System.out.println("error Resume =  " + error.toString());
                }
            });
        }
    }
}
