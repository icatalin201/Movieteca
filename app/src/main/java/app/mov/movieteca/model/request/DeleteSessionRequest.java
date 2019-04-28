package app.mov.movieteca.model.request;

import com.google.gson.annotations.SerializedName;

public class DeleteSessionRequest {

    @SerializedName("session_id")
    private String sessionId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
