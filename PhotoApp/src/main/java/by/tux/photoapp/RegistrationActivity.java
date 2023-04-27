package by.tux.photoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

import by.tux.photoapp.api.ApiClient;
import by.tux.photoapp.models.UserRegisterModel;

public class RegistrationActivity extends AppCompatActivity {

    private ImageView EditProfileAct_CircleImageViewAvatar;
    private EditText EditProfileAct_EditTextLogin, EditProfileAct_EditTextName, EditProfileAct_EditTextPassword, EditProfileAct_EditTextPasswordAgain;
    private Button EditProfileAct_ButtonRegister;
    private TextView EditProfileAct_TextViewInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        this.setTitle("Регистрация пользователя");

        EditProfileAct_EditTextLogin = findViewById(R.id.AdapterAct_TextViewLogin);
        EditProfileAct_EditTextName = findViewById(R.id.EditProfileAct_EditTextName);
        EditProfileAct_EditTextPassword = findViewById(R.id.RegistrationAct_EditTextPassword);
        EditProfileAct_EditTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        EditProfileAct_EditTextPasswordAgain = findViewById(R.id.RegistrationAct_EditTextPasswordAgain);
        EditProfileAct_EditTextPasswordAgain.setTransformationMethod(PasswordTransformationMethod.getInstance());
        EditProfileAct_TextViewInfo = findViewById(R.id.RegistrationAct_TextViewAlert);
        EditProfileAct_ButtonRegister = findViewById(R.id.RegistrationAct_ButtonRegiter);
        EditProfileAct_CircleImageViewAvatar = findViewById(R.id.RegistrationAct_CircleImageViewAvatar);
        EditProfileAct_CircleImageViewAvatar.setImageResource(R.drawable.default_avatar);

        EditProfileAct_ButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String responseLoginAlreadyUsed = "LoginAlreadyUsed";
                if (EditProfileAct_EditTextPassword.getText().toString().equals(EditProfileAct_EditTextPasswordAgain.getText().toString())){
                    if (ifPassword(EditProfileAct_EditTextPassword.getText().toString())) {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        Bitmap bitmap = ((BitmapDrawable) EditProfileAct_CircleImageViewAvatar.getDrawable()).getBitmap();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

                        UserRegisterModel userRegisterModel = new UserRegisterModel(
                                EditProfileAct_EditTextLogin.getText().toString(),
                                EditProfileAct_EditTextPassword.getText().toString(),
                                EditProfileAct_EditTextName.getText().toString(),
                                bitmap);


                        String response = ApiClient.register(userRegisterModel,RegistrationActivity.this);
                        if (!response.equals(responseLoginAlreadyUsed) ){
                            Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            EditProfileAct_TextViewInfo.setText(R.string.not_correct_login);
                        }
                    }else {
                        EditProfileAct_TextViewInfo.setText(R.string.not_correct_passwords);
                    }
                }else {
                    EditProfileAct_TextViewInfo.setText(R.string.not_equal_passwords);
                }
            }
        });

    }

    public boolean ifPassword(String str){
        return ifLength(str) && ifLetters(str) && ifNumbers(str) && ifSpChar(str);
    }

    public boolean ifLength(String str){
        return str.length() > 7 && str.length() < 21;
    }

    public boolean ifLetters(String str){
        for (int i = 0; i < str.length(); i++) {
            if (Character.isAlphabetic(str.charAt(i))){
                return true;
            }
        }
        return false;
    }

    public boolean ifNumbers(String str){
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))){
                return true;
            }
        }
        return false;
    }

    public boolean ifSpChar(String str){
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))&&!Character.isAlphabetic(str.charAt(i))){
                return true;
            }
        }
        return false;
    }

}