package app.mov.movieteca.model.response;

import com.google.gson.annotations.SerializedName;

public class GuestSessionResponse {

    @SerializedName("success")
    private boolean success;
    @SerializedName("guest_session_id")
    private String guestSessionId;
    @SerializedName("expires_at")
    private String expiresAt;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getGuestSessionId() {
        return guestSessionId;
    }

    public void setGuestSessionId(String guestSessionId) {
        this.guestSessionId = guestSessionId;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }
}
