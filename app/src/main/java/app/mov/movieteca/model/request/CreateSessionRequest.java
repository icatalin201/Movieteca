package app.mov.movieteca.model.request;

import com.google.gson.annotations.SerializedName;

public class CreateSessionRequest {

    @SerializedName("request_token")
    private String requestToken;

    public String getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(String requestToken) {
        this.requestToken = requestToken;
    }
}
