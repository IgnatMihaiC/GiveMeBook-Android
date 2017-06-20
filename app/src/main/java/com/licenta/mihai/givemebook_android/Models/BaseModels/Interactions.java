package com.licenta.mihai.givemebook_android.Models.BaseModels;

/**
 * Created by mihai on 24.05.2017.
 */

public class Interactions {

    private Long iId = 0l;

    private Integer type = -1;

    private Long refId = 0l;


    public Long getiId() {
        return iId;
    }

    public void setiId(Long iId) {
        this.iId = iId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getRefId() {
        return refId;
    }

    public void setRefId(Long refId) {
        this.refId = refId;
    }
}
