package vn.mobiapps.soundcastsdk.model.modeldata;

/**
 * Created by NguyenTanHuynh on 7/19/2018.
 */

public class CreativeView {
    private String url;
    private String code;

    public CreativeView(String url, String code) {
        this.url = url;
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
