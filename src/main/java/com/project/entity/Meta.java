package com.project.entity;

import lombok.Data;

@Data
public class Meta {
    private String title;
    private boolean requireAuth;
    private boolean NoTabPage;

    public Meta() {
        this.title = null;
        this.requireAuth = true;
        this.NoTabPage = false;
    }
    public Meta(String title) {
        this.title = title;
        this.requireAuth = true;
        this.NoTabPage = false;
    }
}
