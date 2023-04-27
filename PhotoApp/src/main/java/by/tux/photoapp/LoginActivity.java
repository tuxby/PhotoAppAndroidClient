package by.tux.photoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import by.tux.photoapp.api.ApiClient;

public class LoginActivity extends AppCompatActivity {

    private TextView textViewResult;
    private EditText editTextLogin, editTextPassword;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setTitle("Страница входа");

        editTextLogin = findViewById(R.id.RegistrationAct_EditTextDisc);
        editTextPassword = findViewById(R.id.RegistrationAct_EditTextPassword);
        editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        textViewResult = findViewById(R.id.RegistrationAct_TextViewAlert);
        buttonLogin = findViewById(R.id.LoginAct_ButtonLogin);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textLogin = editTextLogin.getText().toString();
                String textPassword = editTextPassword.getText().toString();

                String token = ApiClient.login(textLogin, textPassword);

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("jwtToken", token);
                editor.apply();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
