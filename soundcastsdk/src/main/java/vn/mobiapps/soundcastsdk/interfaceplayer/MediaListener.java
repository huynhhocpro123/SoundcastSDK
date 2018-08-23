package vn.mobiapps.soundcastsdk.interfaceplayer;

/**
 * Created by NguyenTanHuynh on 7/30/2018.
 */

public interface MediaListener {
    void updateTimeAdvertisement(int Start);

    void addStart(int start, int finish);

    void addFirstQuartile();

    void addMidPoint();

    void addThirdQuartile();

    void addComplete();

    void setTimePlayAudio(int start, int finish);

    void updateTimePlayAudio(int start);

    void setBackGroundButtonPlay();

    void setBackGroundButtonPause();

    void setEnableButton();

    void setNotEnableButton();

    void showButtonSkip();

    void hideButtonSkip();

    void hideKeyBoard();

    void showProgress();

    void hideProgress();

    void onSuccessStart(String data);

    void onSuccessFirstQuartile(String data);

    void onSuccessMidPoint(String data);

    void onSuccessThirdQuartile(String data);

    void onSuccessComplete(String data);
}
