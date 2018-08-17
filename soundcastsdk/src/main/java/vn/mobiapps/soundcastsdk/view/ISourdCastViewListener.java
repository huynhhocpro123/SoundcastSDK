package vn.mobiapps.soundcastsdk.view;


import vn.mobiapps.soundcastsdk.model.modeldata.DataParserXMLModel;

/**
 * Created by NguyenTanHuynh on 7/30/2018.
 */

public interface ISourdCastViewListener {
    void showProgress();
    void hideProgress();

    void onSuccessSendGetURL(String data);
    void onErrorSendGetURL(String data);

    void onSuccessParserXML(DataParserXMLModel data);
    void onErrorParserXML(String data);
}
