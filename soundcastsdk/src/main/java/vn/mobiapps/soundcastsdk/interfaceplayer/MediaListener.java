package vn.mobiapps.soundcastsdk.interfaceplayer;

/**
 * Created by soundcast on 7/30/2018.
 */

public interface MediaListener {
    void updateTimeAdvertisement(int Start);
    
    void callAd(int start, int finish);

    void addStart();

    void addFirstQuartile();

    void addMidPoint();

    void addThirdQuartile();

    void addComplete();

    void setTimePlayAudio(int start, int finish);

    void updateTimePlayAudio(int start);

    void setBackGroundButtonPlay();

    void setBackGroundButtonPause();

    void showButtonSkip();

    void hideButtonSkip();

    void hideKeyBoard();

    void showProgress();

    void hideProgress();

    void midCroll(boolean check, int second);
}
