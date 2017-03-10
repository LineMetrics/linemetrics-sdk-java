package com.linemetrics.api.returntypes;

import com.google.gson.annotations.SerializedName;
import org.apache.http.HttpRequest;

/**
 * Created by Klemens on 03.03.2017.
 */
public class OAuth2Token {

    @SerializedName(value = "access_token")
    private String accessToken;

    @SerializedName(value = "token_type")
    private String tokenType;

    @SerializedName(value = "expires_in")
    private Integer expiresIn;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public void authenticate(final HttpRequest req){
        if(this.accessToken != null){
            req.addHeader("Authorization", this.tokenType + " " + this.accessToken);
        }
    }

    @Override
    public String toString() {
        return String.format("AccessToken: %s, TokenType: %s, ExpiresIn: %s", accessToken, tokenType, String.valueOf(expiresIn));
    }
}
