package by.tux.photoapp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;


public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }
    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }
    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}


//public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
//    ImageView imageView;
//    String base64Image;
//
//    public DownloadImageTask(ImageView imageView,String base64Image) {
//        this.imageView = imageView;
//        this.base64Image = base64Image;
//    }
//
//    @Override
//    protected Bitmap doInBackground(String... strings) {
//        byte[] imageBytes = Base64.decode(base64Image, Base64.DEFAULT);
//        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//
//        return decodedImage;
//    }
//
//    protected void onPostExecute(Bitmap result) {
//        imageView.setImageBitmap(result);
//    }
//}