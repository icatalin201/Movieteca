package app.mov.movieteca.model.response;

import com.google.gson.annotations.SerializedName;

public class TokenResponse {

    @SerializedName("success")
    private boolean success;
    @SerializedName("request_token")
    private String requestToken;
    @SerializedName("expires_at")
    private String expiresAt;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(String requestToken) {
        this.requestToken = requestToken;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }
}
