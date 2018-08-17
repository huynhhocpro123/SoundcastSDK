package vn.mobiapps.soundcastsdk.model.modeldata;

/**
 * Created by NguyenTanHuynh on 7/19/2018.
 */

public class ModelCategories {
    private String id;
    private String name;

    public ModelCategories(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
