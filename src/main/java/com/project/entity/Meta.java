package com.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Meta implements Serializable {
    private static final long serialVersionUID = -5645108301917994003L;
    private String title;
    private boolean requireAuth;
    private boolean NoTabPage;

    public Meta(String title) {
        this.title = title;
        this.requireAuth = true;
        this.NoTabPage = false;
    }
}
