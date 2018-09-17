package vn.mobiapps.soundcastsdk.model.modeldata;

/**
 * Created by soundcast on 7/19/2018.
 */

public class ModelResponse {
    private String mode;
    private String token;
    private Creative creative;
    private String error;


    public ModelResponse() {
    }

    public ModelResponse(String mode, String token, Creative creative) {
        this.mode = mode;
        this.token = token;
        this.creative = creative;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Creative getCreative() {
        return creative;
    }

    public void setCreative(Creative creative) {
        this.creative = creative;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
