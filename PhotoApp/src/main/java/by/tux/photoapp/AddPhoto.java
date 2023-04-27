package by.tux.photoapp;

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
import android.widget.Toast;

import by.tux.photoapp.api.ApiClient;
import by.tux.photoapp.models.PostModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public class AddPhoto extends AppCompatActivity {

    static final int GALLERY_REQUEST = 1; // return from image picker

    private ImageView AddPhotoAct_ImageViewImage;
    private EditText AddPhotoAct_EditTextDisc;
    private Button AddPhotoAct_ButtonAddImage;
    private boolean imageIsSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);
        this.setTitle("Новое изображение");

        AddPhotoAct_ImageViewImage = findViewById(R.id.AddPhotoAct_ImageViewImage);
        AddPhotoAct_ImageViewImage.setImageResource(R.drawable.default_image);
        AddPhotoAct_ButtonAddImage = findViewById(R.id.AddPhotoAct_ButtonAddImage);
        AddPhotoAct_EditTextDisc = findViewById(R.id.AddPhotoAct_EditTextDisc);

        AddPhotoAct_ImageViewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/**");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST);
            }
        });
        AddPhotoAct_ButtonAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!imageIsSelected){
                    Toast.makeText(getApplicationContext(),"Изображение не выбрано", Toast.LENGTH_SHORT).show();
                }
                Bitmap bitmap = ((BitmapDrawable) (AddPhotoAct_ImageViewImage.getDrawable())).getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

                String imageUrlUUID = String.valueOf(UUID.randomUUID());
                PostModel postModel = new PostModel(0,0,null,null,AddPhotoAct_EditTextDisc.getText().toString(),0,System.currentTimeMillis(),imageUrlUUID);

                ApiClient.addPost(postModel,bitmap,AddPhoto.this);

                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        Bitmap bitmap = null;
        switch(requestCode ) {
            case GALLERY_REQUEST:
                if(resultCode == RESULT_OK && imageReturnedIntent!=null){
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        AddPhotoAct_ImageViewImage.setImageBitmap(bitmap);
                        imageIsSelected = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                        AddPhotoAct_ImageViewImage.setImageResource(R.drawable.default_image);
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