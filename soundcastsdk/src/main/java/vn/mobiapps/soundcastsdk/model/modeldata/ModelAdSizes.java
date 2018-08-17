package vn.mobiapps.soundcastsdk.model.modeldata;

/**
 * Created by NguyenTanHuynh on 7/19/2018.
 */

public class ModelAdSizes {
    private String id;
    private String width;
    private String height;
    private String label;

    public ModelAdSizes(String id, String width, String height, String label) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
