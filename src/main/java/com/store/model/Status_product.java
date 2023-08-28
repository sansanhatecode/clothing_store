package com.store.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Status_product implements Serializable {
    private int statusId;
    private String statusName;

    public Status_product() {
    }

    public Status_product(int statusId, String statusName) {
        this.statusId = statusId;
        this.statusName = statusName;
    }
}
