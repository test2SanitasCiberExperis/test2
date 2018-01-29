package com.mycorp;

import com.ning.http.client.AsyncHttpClient;

public class Builder {
    private AsyncHttpClient client = null;
    private final String url;
    private String username = null;
    private String wdps = null;
    private String token = null;
    private String oauthToken = null;

    public Builder(String url) {
        this.url = url;
    }

    public Builder setUsername(String username) {
        this.username = username;
        return this;
    }

    public Builder setWdps(String wdps) {
        this.wdps = wdps;
        if (wdps != null) {
            this.token = null;
            this.setOauthToken(null);
        }
        return this;
    }

    public Builder setToken(String token) {
        this.token = token;
        if (token != null) {
            this.wdps = null;
            this.setOauthToken(null);
        }
        return this;
    }

    public String getOauthToken() {
        return oauthToken;
    }

    public void setOauthToken(String oauthToken) {
        this.oauthToken = oauthToken;
        if (oauthToken != null) {
            this.wdps = null;
            this.setToken(null);
        }
    }

    /**
     *
     * Llamada al constructor Zendesk
     *
     * @return Objeti Zendesk
     */
    public Zendesk build() {
        if (token != null) {
            return new Zendesk(client, url, username + "/token", token);
        }
        return new Zendesk(client, url, username, wdps);
    }
}
