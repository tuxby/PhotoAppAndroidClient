package by.tux.photoapp.models;

public class UserInfoModel {
    private String login;
    private String name;
    private String disc;
    private String imageUrl;

    public UserInfoModel(String login, String name, String disc, String imageUrl) {
        this.login = login;
        this.name = name;
        this.disc = disc;
        this.imageUrl = imageUrl;
    }

    public UserInfoModel() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
