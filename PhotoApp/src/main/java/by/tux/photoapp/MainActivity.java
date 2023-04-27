package by.tux.photoapp;

import static by.tux.photoapp.api.Url.getImageUrl;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import by.tux.photoapp.api.ApiClient;
import by.tux.photoapp.models.UserInfoModel;
import by.tux.photoapp.util.DownloadImageTask;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private TextView textViewLogin, textViewName, textViewDisc;
    private Button buttonEditProfile, buttonMyPhotos, buttonAllPhotos, buttonAddPhoto, buttonLogout;
    private CircleImageView circleImageView;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Мой профиль");

        textViewLogin = findViewById(R.id.MainAct_TextViewLogin);
        textViewName = findViewById(R.id.MainAct_TextViewName);
        textViewDisc = findViewById(R.id.MainAct_TextViewDisc);
        buttonEditProfile = findViewById(R.id.MainAct_ButtonEditProfile);
        buttonMyPhotos = findViewById(R.id.MainAct_ButtonMyPhotos);
        buttonAllPhotos = findViewById(R.id.MainAct_ButtonAllPhotos);
        buttonAddPhoto = findViewById(R.id.MainAct_ButtonAddPhoto);
        buttonLogout = findViewById(R.id.MainAct_ButtonLogout);
        circleImageView = findViewById(R.id.MainAct_CircleImageViewAvatar);
        circleImageView.setImageResource(R.drawable.default_avatar);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        if (!sharedPreferences.contains("jwtToken")) {
            Intent intent = new Intent(MainActivity.this, ChooseType.class);
            startActivity(intent);
            finish();
        }else {
            UserInfoModel userInfoModel = ApiClient.myProfile(MainActivity.this);
            if (userInfoModel == null){
                Intent intent = new Intent(MainActivity.this, ChooseType.class);
                startActivity(intent);
                finish();
            }
            else {
                textViewLogin.setText(userInfoModel.getLogin());
                textViewName.setText(userInfoModel.getName());
                textViewDisc.setText(userInfoModel.getDisc());
                circleImageView.setImageResource(R.drawable.default_avatar);

                if (!userInfoModel.getImageUrl().equals(null)){
                    new DownloadImageTask(circleImageView).execute(getImageUrl(userInfoModel.getImageUrl()));
                }


                circleImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        circleImageView.setImageResource(R.drawable.default_avatar);
                        if (!userInfoModel.getImageUrl().equals(null)){
                            new DownloadImageTask(circleImageView).execute(getImageUrl(userInfoModel.getImageUrl()));
                        }
                    }
                });

                buttonEditProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, EditProfile.class);
                        startActivity(intent);
                    }
                });

                buttonMyPhotos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, MyPostsActivity.class);
                        startActivity(intent);
                    }
                });

                buttonAddPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, AddPhoto.class);
                        startActivity(intent);
                    }
                });

                buttonAllPhotos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, FeedActivity.class);
                        startActivity(intent);
                    }
                });

                buttonLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showConfirmLogoutDialog() ;
                    }
                    private void showConfirmLogoutDialog() {
                        AlertDialog.Builder builder;
                        builder = new AlertDialog.Builder(MainActivity.this);

                        builder.setMessage("После выхода, вам необходимо будет войти с помощью логина и пароля, или повторно зарегистрироваться")
                                .setCancelable(false)
                                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
//                                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                                        sharedPreferences.edit().remove("jwtToken").commit();
                                        Intent intent = new Intent(MainActivity.this, ChooseType.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Toast.makeText(getApplicationContext(),"Выход отменен",Toast.LENGTH_SHORT).show();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.setTitle("Выход из профиля");
                        alert.show();
                    }
                });
            }
        }
    }



//    @Override
//    protected void onStart() {
//        super.onStart();
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
//        if (sharedPreferences.getAll().get("jwtToken") == null) {
//            Intent intent = new Intent(MainActivity.this, ChooseType.class);
//            startActivity(intent);
//            finish();
//        } else {
//            UserInfoModel userInfoModel = ApiClient.myProfile(MainActivity.this);
//            if (userInfoModel == null) {
//                Intent intent = new Intent(MainActivity.this, ChooseType.class);
//                startActivity(intent);
//                finish();
//            } else {
//                textViewLogin.setText(userInfoModel.getLogin());
//                textViewName.setText(userInfoModel.getName());
//                circleImageView.setImageResource(R.drawable.default_avatar);
//                if (!userInfoModel.getImageUrl().equals(null)){
//                    new DownloadImageTask(circleImageView).execute(userInfoModel.getImageUrl());
//                }
//            }
//        }
//    }

}

