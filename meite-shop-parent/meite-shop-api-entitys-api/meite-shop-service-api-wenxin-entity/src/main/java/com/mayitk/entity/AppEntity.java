package com.mayitk.entity;

import lombok.Data;

/**
 * 实体类
 */
@Data
public class AppEntity {
    private String addId;
    private String addUser;

    public AppEntity(String addId, String addUser) {
        this.addId = addId;
        this.addUser = addUser;
    }

    public AppEntity() {
    }
}
