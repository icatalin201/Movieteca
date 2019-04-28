package app.mov.movieteca.model.response;

import com.google.gson.annotations.SerializedName;

public class Account {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("include_adult")
    private boolean includeAdult;
    @SerializedName("username")
    private String username;
    private AccountAvatar avatar;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIncludeAdult() {
        return includeAdult;
    }

    public void setIncludeAdult(boolean includeAdult) {
        this.includeAdult = includeAdult;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public AccountAvatar getAvatar() {
        return avatar;
    }

    public void setAvatar(AccountAvatar avatar) {
        this.avatar = avatar;
    }
}
