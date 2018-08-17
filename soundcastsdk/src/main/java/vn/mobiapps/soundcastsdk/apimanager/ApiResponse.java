package vn.mobiapps.soundcastsdk.apimanager;

import vn.mobiapps.soundcastsdk.model.modeldata.DataParserXMLModel;

/**
 * Created by NguyenTanHuynh on 7/18/2018.
 */

public interface ApiResponse{
    void onSuccess(String result);

    void onError(String error);

    interface ApiResponeModelVast {
        void onSuccess(DataParserXMLModel model);
        void onError(String s);
    }
}
