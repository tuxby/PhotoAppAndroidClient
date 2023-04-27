package by.tux.photoapp.models;

import lombok.AllArgsConstructor;
import lombok.Data;

public class UserLoginModel {
    private String name;
    private String password;

    public UserLoginModel(String user, String password) {
        this.name = user;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
