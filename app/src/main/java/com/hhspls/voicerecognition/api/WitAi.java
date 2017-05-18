package com.hhspls.voicerecognition.api;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import ai.api.http.HttpClient;

/**
 * Created by HICT-HP on 5/18/2017.
 */

public class WitAi extends AbstractApi {
    private String TAG = "WitAI";
    private String AUTH_TOKEN = "Bearer 2325MDBMKJQS66BM4SDRXBRZS4QE2G2D";
    private String BaseURL = "https://api.wit.ai/";

    private MediaRecorder recorder;

    public WitAi(Context context) {
        super(context);
    }

    @Override
    void startListeningImpl() {
        try{
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setOutputFile(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/myrecording.mp3");
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            recorder.prepare();
            recorder.start();
        }catch (Exception e){
            Log.i(TAG, "Error while startListening " + e);
        }
    }

    @Override
    void stopListeningImpl() {
        //TODO: End audio stream when button is clicked or after 10 seconds because max. Then send POST Request
        try{
            recorder.stop();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String currentDateandTime = sdf.format(new Date());
            String URLParameter = "speech?v="+ currentDateandTime;

            new POSTMethod().execute(BaseURL + URLParameter);
        }catch (Exception e){
            Log.i(TAG, "Error while stopListening " + e);
        }
    }

    @Override
    void cancelImpl() {

    }

    private class POSTMethod extends AsyncTask<String, Void, String> {
        String server_response;

        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;
            String accessToken = AUTH_TOKEN;
            String header = "Authorization";

            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty(header, accessToken);
                urlConnection.setRequestProperty("Content-Type", "audio/mp3");
                urlConnection.setUseCaches(false);
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                IOUtils.copy(new FileInputStream(getLatestRecording()), urlConnection.getOutputStream());
//                String res = IOUtils.toString(urlConnection.getInputStream());

                int responseCode = urlConnection.getResponseCode();
                Log.i(TAG, "Response_code = " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    server_response = readStream(urlConnection.getInputStream());
                }
            } catch (IOException e) {
                Log.i(TAG, "Error while POSTMethod " + e);
            } finally {
                assert urlConnection != null;
                urlConnection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("Response", "" + server_response);
        }
    }

    private class GETMethod extends AsyncTask<String, Void, String> {
        String server_response;

        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;
            String accessToken = AUTH_TOKEN;
            String header = "Authorization";

            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty(header, accessToken);

                int responseCode = urlConnection.getResponseCode();
                Log.i(TAG, "Response_code = " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    server_response = readStream(urlConnection.getInputStream());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                assert urlConnection != null;
                urlConnection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("Response", "" + server_response);
        }
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuilder response = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.i(TAG, response.toString());
        return response.toString();
    }

    private void startTest() {
        try {
            String URLParameter = "message?q=This%20is%20a%20test";
            new GETMethod().execute(BaseURL + URLParameter);
        } catch (Exception e) {
            Log.i(TAG, "error while starting to listen");
        }
    }

    private File getLatestRecording(){
        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard, "myrecording.mp3");
//        byte[] data = new byte[(int) file.length()];
        return file;
    }
}
