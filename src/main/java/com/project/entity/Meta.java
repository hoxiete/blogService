package com.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Meta implements Serializable {
    private String title;
    private boolean requireAuth;
    private boolean NoTabPage;

    public Meta(String title) {
        this.title = title;
        this.requireAuth = true;
        this.NoTabPage = false;
    }
}
