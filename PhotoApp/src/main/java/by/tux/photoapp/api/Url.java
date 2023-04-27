package by.tux.photoapp.api;

public class Url {
    public final static String domainUrl = "https://8868598.ddns.net/photoapp";
    public final static String registerUrl = domainUrl + "/register";
    public final static String loginUrl = domainUrl + "/login";
    public final static String myProfileUrl = domainUrl + "/user/myprofile";
    public final static String editUrl = domainUrl + "/user/edit";
    public final static String imageUrl = domainUrl + "/image/";

    public static String getImageUrl(String shotImageUrl){
        return imageUrl + shotImageUrl;
    }
    public final static String imageUploadUrl = domainUrl + "/image/upload";


    public final static String addPost = domainUrl + "/post/add";
    public final static String getPost = domainUrl + "/post/get";
    public final static String myPosts = domainUrl + "/post/my";
    public final static String feedPost = domainUrl + "/post/feed";




}
