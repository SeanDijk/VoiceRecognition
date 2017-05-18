package com.hhspls.voicerecognition;

import android.Manifest;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhspls.voicerecognition.api.ApiAi;
import com.hhspls.voicerecognition.api.GoogleSpeech;
import com.hhspls.voicerecognition.api.WitAi;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private ImageView mImageView;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fm = getSupportFragmentManager();
            int id = R.id.content;
            Context c = MainActivity.this;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_api_ai);
                    mImageView.setImageDrawable(getResources().getDrawable(R.drawable.apiai));
                    fm.beginTransaction().replace(id, SpeechFragment.newInstance(new ApiAi(c))).commit();
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_google_speech);
                    mImageView.setImageDrawable(getResources().getDrawable(R.drawable.googlespeech));
                    fm.beginTransaction().replace(id, SpeechFragment.newInstance(new GoogleSpeech(c))).commit();
                    return true;
                case R.id.navigation_wit_api:
                    mTextMessage.setText(R.string.title_wit_api);
                    mImageView.setImageDrawable(getResources().getDrawable(R.drawable.wit_ai_960));
                    fm.beginTransaction().replace(id, SpeechFragment.newInstance(new WitAi(c))).commit();
                    return true;
                case R.id.navigation_notifications2:
                    mTextMessage.setText(R.string.title_notifications2);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        mImageView = (ImageView) findViewById(R.id.logo);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
    }

}
