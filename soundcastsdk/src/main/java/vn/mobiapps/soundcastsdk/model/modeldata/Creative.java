package vn.mobiapps.soundcastsdk.model.modeldata;

import java.util.ArrayList;

/**
 * Created by NguyenTanHuynh on 7/19/2018.
 */

public class Creative {
    private String id;
    private String ad_id;
    private CreativeView creativeView;
    private ArrayList<ModelCategories> creativeCategories;
    private ArrayList<ModelAdSizes> adSizes;

    public Creative(String id, String ad_id, CreativeView creativeView, ArrayList<ModelCategories> creativeCategories, ArrayList<ModelAdSizes> adSizes) {
        this.id = id;
        this.ad_id = ad_id;
        this.creativeView = creativeView;
        this.creativeCategories = creativeCategories;
        this.adSizes = adSizes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAd_id() {
        return ad_id;
    }

    public void setAd_id(String ad_id) {
        this.ad_id = ad_id;
    }

    public CreativeView getCreativeView() {
        return creativeView;
    }

    public void setCreativeView(CreativeView creativeView) {
        this.creativeView = creativeView;
    }

    public ArrayList<ModelCategories> getCreativeCategories() {
        return creativeCategories;
    }

    public void setCreativeCategories(ArrayList<ModelCategories> creativeCategories) {
        this.creativeCategories = creativeCategories;
    }

    public ArrayList<ModelAdSizes> getAdSizes() {
        return adSizes;
    }

    public void setAdSizes(ArrayList<ModelAdSizes> adSizes) {
        this.adSizes = adSizes;
    }
}
