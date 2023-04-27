package by.tux.photoapp;

import static by.tux.photoapp.api.Url.getImageUrl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import by.tux.photoapp.api.ApiClient;
import by.tux.photoapp.models.UserInfoModel;
import by.tux.photoapp.util.DownloadImageTask;

public class EditProfile extends AppCompatActivity {

    private ImageView RegistrationAct_CircleImageViewAvatar;
    private EditText EditProfileAct_EditTextName, EditProfileAct_EditTextDisc;
    private Button EditProfileAct_ButtonSave, EditProfileAct_ButtonClearAvatar;
    private TextView EditProfileAct_TextViewLogin;
    static final int GALLERY_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        this.setTitle("Редактирование профиля");

        RegistrationAct_CircleImageViewAvatar = findViewById(R.id.RegistrationAct_CircleImageViewAvatar);
        EditProfileAct_TextViewLogin = findViewById(R.id.EditProfileAct_TextViewLogin);
        EditProfileAct_EditTextName = findViewById(R.id.EditProfileAct_EditTextName);
        EditProfileAct_EditTextDisc = findViewById(R.id.EditProfileAct_EditTextDisc);
        EditProfileAct_ButtonSave = findViewById(R.id.EditProfileAct_ButtonSave);
        EditProfileAct_ButtonClearAvatar = findViewById(R.id.EditProfileAct_ButtonClearAvatar);

        UserInfoModel userInfoModel = ApiClient.myProfile(EditProfile.this);
        if (userInfoModel == null){
            Intent intent = new Intent(EditProfile.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            EditProfileAct_TextViewLogin.setText(userInfoModel.getLogin());
            EditProfileAct_EditTextName.setText(userInfoModel.getName());
            EditProfileAct_EditTextDisc.setText(userInfoModel.getDisc());
            RegistrationAct_CircleImageViewAvatar.setImageResource(R.drawable.default_avatar);
            if (userInfoModel.getImageUrl()!= null) {
                new DownloadImageTask(RegistrationAct_CircleImageViewAvatar).execute(getImageUrl(userInfoModel.getImageUrl()));
            }

            RegistrationAct_CircleImageViewAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setType("image/**");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST);
                }
            });
            EditProfileAct_ButtonClearAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RegistrationAct_CircleImageViewAvatar.setImageResource(R.drawable.default_avatar);
                }
            });
            EditProfileAct_ButtonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userInfoModel.setLogin(EditProfileAct_TextViewLogin.getText().toString());
                    userInfoModel.setName(EditProfileAct_EditTextName.getText().toString());
                    userInfoModel.setDisc(EditProfileAct_EditTextDisc.getText().toString());
                    userInfoModel.setImageUrl(String.valueOf(UUID.randomUUID()));

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    Bitmap bitmap = ((BitmapDrawable) RegistrationAct_CircleImageViewAvatar.getDrawable()).getBitmap();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

                    ApiClient.editProfile(userInfoModel,EditProfile.this,bitmap);

                    Intent intent = new Intent(EditProfile.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        Bitmap bitmap = null;
        UserInfoModel userInfoModel= new UserInfoModel();
        switch(requestCode ) {
            case GALLERY_REQUEST:
                if(resultCode == RESULT_OK && imageReturnedIntent!=null){
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        RegistrationAct_CircleImageViewAvatar.setImageBitmap(bitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                        RegistrationAct_CircleImageViewAvatar.setImageResource(R.drawable.default_avatar);
                    }
                }

        }
    }

//    private boolean hasImage(@NonNull ImageView view) {
//        Drawable drawable = view.getDrawable();
//        boolean hasImage = (drawable != null);
//        if (hasImage && (drawable instanceof BitmapDrawable)) {
//            hasImage = ((BitmapDrawable)drawable).getBitmap() != null;
//        }
//        return hasImage;
//    }
}