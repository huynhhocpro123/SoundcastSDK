package vn.mobiapps.soundcastsdk.presenter;

import vn.mobiapps.soundcastsdk.interactor.ISoundCastInteractor;
import vn.mobiapps.soundcastsdk.interactor.ISoundCastInteractorListener;
import vn.mobiapps.soundcastsdk.interactor.SoundCastInteractorImpl;
import vn.mobiapps.soundcastsdk.model.modeldata.DataParserXMLModel;
import vn.mobiapps.soundcastsdk.model.modelview.RequestModel;
import vn.mobiapps.soundcastsdk.view.ISourdCastViewListener;


/**
 * Created by NguyenTanHuynh on 7/30/2018.
 */

public class SoundCastPresenterImpl implements ISoundCastPresenter, ISoundCastInteractorListener {

    ISourdCastViewListener viewListener;
    ISoundCastInteractor interactor;

    public SoundCastPresenterImpl(ISourdCastViewListener viewListener) {
        try {
            if (viewListener != null) {
                this.viewListener = viewListener;
                interactor = new SoundCastInteractorImpl(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void sendGetURL(RequestModel requestModel) {
        viewListener.showProgress();
        interactor.sendGetURL(requestModel);
    }

    @Override
    public void sendGetVastXML(String url) {
        viewListener.showProgress();
        interactor.sendGetVastXML(url);
    }

    @Override
    public void onDestroy() {
        interactor = null;
        viewListener = null;
    }

    @Override
    public void onSuccessSendGetURL(String data) {
        viewListener.hideProgress();
        viewListener.onSuccessSendGetURL(data);
    }

    @Override
    public void onErrorSendGetURL(String data) {
        viewListener.hideProgress();
        viewListener.onErrorSendGetURL(data);
    }

    @Override
    public void onSuccessParserXML(DataParserXMLModel data) {
        viewListener.hideProgress();
        viewListener.onSuccessParserXML(data);
    }

    @Override
    public void onErrorParserXML(String data) {
        viewListener.hideProgress();
        viewListener.onErrorParserXML(data);
    }
}
