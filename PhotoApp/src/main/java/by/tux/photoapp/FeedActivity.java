package by.tux.photoapp;

import static by.tux.photoapp.api.Url.getImageUrl;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import by.tux.photoapp.api.ApiClient;
import by.tux.photoapp.models.PostModel;
import by.tux.photoapp.util.DownloadImageTask;

public class FeedActivity extends AppCompatActivity {
    ListView feedActivityListView;
    private int count = 0;
    List<PostModel> postModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        this.setTitle("Все фото");

        feedActivityListView = findViewById(R.id.FeedAct_ListView);
        postModelList = new ArrayList<>();
        postModelList =  ApiClient.getFeed(FeedActivity.this);

        count = postModelList.size();

        feedActivityListView.setAdapter(new CustomAdapter());
    }

    private class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.image_adapter, null);
            ImageView AdapterAct_ImageView = convertView.findViewById(R.id.AdapterAct_ImageView);
            TextView AdapterAct_TextViewLogin = convertView.findViewById(R.id.AdapterAct_TextViewLogin);
            TextView AdapterAct_TextViewName = convertView.findViewById(R.id.AdapterAct_TextViewName);
            TextView AdapterAct_TextViewDisc = convertView.findViewById(R.id.AdapterAct_EditTextDisc);
            TextView AdapterAct_TextViewDate = convertView.findViewById(R.id.AdapterAct_TextViewDate);

            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
            Date resultdate = new Date(postModelList.get(position).getPublishTime());

            AdapterAct_TextViewLogin.setText(postModelList.get(position).getAuthorLogin());
            AdapterAct_TextViewName.setText(postModelList.get(position).getAuthorName());
            AdapterAct_TextViewDisc.setText(postModelList.get(position).getDisc());
            AdapterAct_TextViewDate.setText(sdf.format(resultdate));
            new DownloadImageTask(AdapterAct_ImageView).execute(getImageUrl(postModelList.get(position).getImageUrl()));

            return convertView;
        }
    }

}