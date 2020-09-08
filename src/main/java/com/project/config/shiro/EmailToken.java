package com.project.config.shiro;

import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;

import java.io.Serializable;

public class EmailToken implements HostAuthenticationToken, RememberMeAuthenticationToken, Serializable {
    // 手机号码
    private String email;
    private boolean rememberMe;
    private String host;
    /**
     * 重写getPrincipal方法
     */
    @Override
    public Object getPrincipal() {
        return email;
    }
    /**
     * 重写getCredentials方法
     */
    @Override
    public Object getCredentials() {
        return email;
    }
    public EmailToken() { this.rememberMe = false; }
    public EmailToken(String email) { this(email, false, null); }
    public EmailToken(String email, boolean rememberMe) { this(email, rememberMe, null); }
    public EmailToken(String email, boolean rememberMe, String host) {
        this.email = email;
        this.rememberMe = rememberMe;
        this.host = host;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    @Override
    public String getHost() {
        return host;
    }
    @Override
    public boolean isRememberMe() {
        return rememberMe;
    }
}
