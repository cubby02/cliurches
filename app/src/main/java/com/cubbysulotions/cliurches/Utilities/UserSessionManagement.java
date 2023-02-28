package com.cubbysulotions.cliurches.Utilities;

public class UserSessionManagement {
    int id;
    String username;
    String acc_type;

    public UserSessionManagement(int id, String username, String acc_type) {
        this.id = id;
        this.username = username;
        this.acc_type = acc_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAcc_type() {
        return acc_type;
    }

    public void setAcc_type(String acc_type) {
        this.acc_type = acc_type;
    }
}
