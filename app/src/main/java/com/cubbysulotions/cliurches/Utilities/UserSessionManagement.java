package com.cubbysulotions.cliurches.Utilities;

public class UserSessionManagement {
    int id;
    String username;
    String acc_type;

    String admin_name;

    public UserSessionManagement(int id, String username, String acc_type, String admin_name) {
        this.id = id;
        this.username = username;
        this.acc_type = acc_type;
        this.admin_name = admin_name;
    }

    public String getAdmin_name() {
        return admin_name;
    }

    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
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
