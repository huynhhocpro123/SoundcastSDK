package vn.mobiapps.soundcastsdk.interactor;

import vn.mobiapps.soundcastsdk.apimanager.APIManager;
import vn.mobiapps.soundcastsdk.apimanager.ApiResponse;
import vn.mobiapps.soundcastsdk.model.modeldata.DataParserXMLModel;
import vn.mobiapps.soundcastsdk.model.modelview.RequestModel;

import static vn.mobiapps.soundcastsdk.until.Contanst.URLRQ;

/**
 * Created by NguyenTanHuynh on 7/30/2018.
 */

public class SoundCastInteractorImpl implements ISoundCastInteractor {

    ISoundCastInteractorListener interactorListener;

    public SoundCastInteractorImpl(ISoundCastInteractorListener interactorListener) {
        try {
            if (interactorListener != null) {
                this.interactorListener = interactorListener;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void sendGetURL(RequestModel requestModel) {
        String URL = URLRQ + requestModel.networkID + "/site/" + requestModel.siteID + "/tag/" + requestModel.tagID + "?pageTitle=" + requestModel.pageTitle + "&pageDescription=" + requestModel.pageDescription + "&keywords=" + requestModel.keywords + "&pageUrl=" + requestModel.pageUrl + "&tags=" + requestModel.tags + "&test=" + requestModel.test;
        APIManager.sendGet(URL, new ApiResponse() {
            @Override
            public void onSuccess(String result) {
                interactorListener.onSuccessSendGetURL(result);
            }

            @Override
            public void onError(String error) {
                interactorListener.onErrorSendGetURL(error);
            }
        });
    }

    @Override
    public void sendGetVastXML(String URL) {
        APIManager.sendGetVast(URL, new ApiResponse.ApiResponeModelVast() {
            @Override
            public void onSuccess(DataParserXMLModel model) {
                interactorListener.onSuccessParserXML(model);
            }

            @Override
            public void onError(String s) {
                interactorListener.onErrorParserXML(s);
            }
        });
    }
}
