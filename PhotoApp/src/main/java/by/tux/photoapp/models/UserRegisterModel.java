package by.tux.photoapp.models;

import android.graphics.Bitmap;

public class UserRegisterModel {
    private String login;
    private String password;
    private String name;
    private Bitmap AvatarImageBitmap;

    public UserRegisterModel(String login, String password, String name, Bitmap AvatarImageBitmap) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.AvatarImageBitmap = AvatarImageBitmap;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getAvatarImageBitmap() {
        return AvatarImageBitmap;
    }

    public void setAvatarImageBitmap(Bitmap avatarImageBitmap) {
        AvatarImageBitmap = avatarImageBitmap;
    }
}
