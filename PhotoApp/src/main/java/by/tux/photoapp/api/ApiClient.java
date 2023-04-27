package by.tux.photoapp.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import by.tux.photoapp.FeedActivity;
import by.tux.photoapp.models.PostModel;
import by.tux.photoapp.models.UserInfoModel;
import by.tux.photoapp.models.UserRegisterModel;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class ApiClient {
    private static OkHttpClient okHttpClient;

    public static void init() {
        StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(gfgPolicy);

        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build();
        }
    }

    public static String login(String user,String password)  {

        String loginResult=null;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", user);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        loginResult = post(Url.loginUrl, String.valueOf(jsonObject));
        Log.d("Tag", "Login result:  "+ loginResult);

        if (loginResult!=null){
            if (!loginResult.equals("")) {
                try {
                    JSONObject jsonObjectResult = new JSONObject(loginResult);
                    String jwtToken =  jsonObjectResult.get("jwtToken").toString();
                    Log.d("Tag", "jwtToken: " + jwtToken);
                    return jwtToken;
                } catch (JSONException e) {
                    Log.d("Tag", "Error parse token");
                    e.printStackTrace();
                    return null;
                }
            }
        }
        return null;
    }

    public static String register(UserRegisterModel userRegisterModel, Context context)  {
        String uploadImageResult=null;
        String registerResult=null;
        String imageUrlUUID = String.valueOf(UUID.randomUUID());
        uploadImageResult = uploadImage(Url.imageUploadUrl,userRegisterModel.getAvatarImageBitmap(),imageUrlUUID,context);
        Log.d("Tag", "uploadImageResult :  " + uploadImageResult);

        RequestBody formBody = new FormBody.Builder()
                .add("login", userRegisterModel.getLogin())
                .add("password", userRegisterModel.getPassword())
                .add("name", userRegisterModel.getName())
                .add("imageUrl", imageUrlUUID)
                .build();

        Request request = new Request.Builder()
                .url(Url.registerUrl)
                .post(formBody)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            Log.d("Tag", "Register result:  " +registerResult);
            return   response.body().string();
        }
        catch (IOException e) {
            e.printStackTrace();
            return "fail";
        }
    }

    public static UserInfoModel myProfile(Context context) {
        String  myProfileResult = get(Url.myProfileUrl,context);
        if (myProfileResult.equals("TokenNotFound") || myProfileResult.equals("fail")){
               return null;
        }
        else {
            try{
                JSONObject jsonObject = new JSONObject(myProfileResult);
                UserInfoModel userInfoModel = new UserInfoModel(
                        jsonObject.getString("login"),
                        jsonObject.getString("name"),
                        jsonObject.getString("disc"),
                        jsonObject.getString("imageUrl"));
                return userInfoModel;
            }
            catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static String editProfile(UserInfoModel userInfoModel, Context context, Bitmap bitmap)  {
        String editResult=null;
        String filename = userInfoModel.getImageUrl();
        uploadImage(Url.imageUploadUrl,bitmap,filename,context);

        RequestBody requestBody = new FormBody.Builder()
                .add("login", userInfoModel.getLogin())
                .add("name", userInfoModel.getName())
                .add("disc", userInfoModel.getDisc())
                .add("imageUrl", userInfoModel.getImageUrl())
                .build();
        editResult = postWithToken(Url.editUrl, requestBody ,context);
        Log.d("Tag", "Register result:  " +editResult);
        return editResult;
    }

    public static String addPost(PostModel postModel, Bitmap bitmap, Context context)  {
        String addPostResult = null;
        uploadImage(Url.imageUploadUrl,bitmap,postModel.getImageUrl(),context);

        RequestBody requestBody = new FormBody.Builder()
                .add("disc", postModel.getDisc())
                .add("imageUrl", postModel.getImageUrl())
                .build();

        addPostResult = postWithToken(Url.addPost,requestBody,context);
        return addPostResult;
    }

    public static List<PostModel> getFeed(Context context)  {
        ArrayList<PostModel> feedList = new ArrayList<>();
        String feedResult = get(Url.feedPost,context);
        try{
            JSONArray jsonArray = new JSONArray(feedResult);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                PostModel postModel = new PostModel(
                        Long.parseLong( jsonObject.get("id").toString() ),
                        Long.parseLong( jsonObject.get("authorId").toString() ),
                        jsonObject.get("authorLogin").toString(),
                        jsonObject.get("authorName").toString(),
                        jsonObject.get("disc").toString(),
                        Long.parseLong( jsonObject.get("likes").toString() ),
                        Long.parseLong( jsonObject.get("publishTime").toString() ),
                        jsonObject.get("imageUrl").toString()
                );
                feedList.add(postModel);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return feedList;
    }

    public static String uploadImage(String url, Bitmap bitmap , String filename, Context context)  {
        init();
        String uploadResponse = null;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", filename, RequestBody.create(MediaType.parse("image/png"), byteArray))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient();
        try {
            Response response = client.newCall(request).execute();
            uploadResponse = response.body().toString();
        }
        catch (IOException e) {
            e.printStackTrace();
            return "fail";
        }
        return uploadResponse;
    }

    public static String post(String url, String json)  {
        init();
        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            return response.body().string();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String postWithToken(String url, RequestBody requestBody , Context context)  {
        init();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);


        if (sharedPreferences.getAll().get("jwtToken")==null){
            return "TokenNotFound";
        }else {
            String jwtToken = (String) sharedPreferences.getAll().get("jwtToken");
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "Bearer " + jwtToken)
                    .post(requestBody)
                    .build();
            try {
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();
            }
            catch (IOException e) {
                e.printStackTrace();
                return "fail";
            }
        }
    }

    public static String get(String url,Context context) {
        init();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (sharedPreferences.getAll().get("jwtToken")==null){
            return "TokenNotFound";
        }else {
            String jwtToken = (String) sharedPreferences.getAll().get("jwtToken");
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "Bearer " + jwtToken)
                    .build();
            try {
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();
            }
            catch (IOException e) {
                e.printStackTrace();
                return "fail";
            }
        }
    }

    public static List<PostModel> getMyFeed(Context context) {
        ArrayList<PostModel> feedList = new ArrayList<>();
        String feedResult = get(Url.myPosts,context);
        try{
            JSONArray jsonArray = new JSONArray(feedResult);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                PostModel postModel = new PostModel(
                        Long.parseLong( jsonObject.get("id").toString() ),
                        Long.parseLong( jsonObject.get("authorId").toString() ),
                        jsonObject.get("authorLogin").toString(),
                        jsonObject.get("authorName").toString(),
                        jsonObject.get("disc").toString(),
                        Long.parseLong( jsonObject.get("likes").toString() ),
                        Long.parseLong( jsonObject.get("publishTime").toString() ),
                        jsonObject.get("imageUrl").toString()
                );
                feedList.add(postModel);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return feedList;
    }
}
