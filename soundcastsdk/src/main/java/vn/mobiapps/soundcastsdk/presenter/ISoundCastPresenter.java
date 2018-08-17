package vn.mobiapps.soundcastsdk.presenter;

import vn.mobiapps.soundcastsdk.model.modelview.RequestModel;

/**
 * Created by NguyenTanHuynh on 7/30/2018.
 */

public interface ISoundCastPresenter {
    void sendGetURL(RequestModel requestModel);
    void sendGetVastXML(String url);
    void onDestroy();
}
