package com.hhspls.voicerecognition.api;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hhspls.voicerecognition.models.WitOutcome;

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
import java.io.OutputStream;
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

    //
    //  Toggle between GETRequest (TEST with Hardcoded String) and POSTRequest(SEND RECORDED MP3).
    private boolean test = true;
    private Gson gson;

    private MediaRecorder recorder;

    public WitAi(Context context) {
        super(context);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    @Override
    void startListeningImpl() {
        try {
            if (test) {
                //Do nothing
            } else {
                recorder = new MediaRecorder();
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                recorder.setOutputFile(Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + "/myrecording.mp3");
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                recorder.prepare();
                recorder.start();
            }
        } catch (Exception e) {
            Log.i(TAG, "Error while recording " + e);
        }
    }

    @Override
    WitOutcome stopListeningImpl() {
        //TODO: End audio stream when button is clicked or after 10 seconds because max allowed 10 sec. Then send POST Request
        if (!test) {
            try {
                recorder.stop();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String currentDateandTime = sdf.format(new Date());
                String URLParameter = "speech?v=" + currentDateandTime;
                String tempString = new POSTMethod().execute(BaseURL + URLParameter).get();

                return gson.fromJson(tempString, WitOutcome.class);
            } catch (Exception e) {
                Log.i(TAG, "Error while stopListening " + e);
            }
        } else {
            try {
                String URLParameter = "message?q=Hello";
                String tempString = new GETMethod().execute(BaseURL + URLParameter).get();

                return gson.fromJson(tempString, WitOutcome.class);
            } catch (Exception e) {
                Log.i(TAG, "Error while sending test message");
            }
        }
        return null;
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

                OutputStream outputStream = urlConnection.getOutputStream();
                outputStream.write(getLatestRecordingByteArray());

                int responseCode = urlConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    server_response = readStream(urlConnection.getInputStream());
                    return server_response;
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
//            Log.e("Response", "" + server_response);
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
                    return server_response;
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
//            Log.e("Response", "" + server_response);
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
        Log.i(TAG, "readStream response: " + response.toString());
        return response.toString();
    }

    private byte[] getLatestRecordingByteArray() {
        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard, "myrecording.mp3");
        return new byte[(int) file.length()];
    }

    private File getLatestRecording() {
        File sdcard = Environment.getExternalStorageDirectory();
        return new File(sdcard, "myrecording.mp3");
    }
}
