package vn.mobiapps.soundcastsdk.interactor;

import vn.mobiapps.soundcastsdk.model.modeldata.DataParserXMLModel;

/**
 * Created by soundcast on 7/30/2018.
 */

public interface ISoundCastInteractorListener {
    void onSuccessSendGetURL(String data);
    void onErrorSendGetURL(String data);

    void onSuccessParserXML(DataParserXMLModel data);
    void onErrorParserXML(String data);
}
