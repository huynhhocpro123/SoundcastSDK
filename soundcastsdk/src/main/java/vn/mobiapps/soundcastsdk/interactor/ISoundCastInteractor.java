package vn.mobiapps.soundcastsdk.interactor;

import vn.mobiapps.soundcastsdk.model.modelview.RequestModel;

/**
 * Created by soundcast on 7/30/2018.
 */

public interface ISoundCastInteractor {
    void loadAd(RequestModel requestModel);
    void sendGetVastXML(String URL);
}
