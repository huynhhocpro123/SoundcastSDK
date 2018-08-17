package vn.mobiapps.soundcastsdk.interactor;

import vn.mobiapps.soundcastsdk.model.modelview.RequestModel;

/**
 * Created by NguyenTanHuynh on 7/30/2018.
 */

public interface ISoundCastInteractor {
    void sendGetURL(RequestModel requestModel);
    void sendGetVastXML(String URL);
}
