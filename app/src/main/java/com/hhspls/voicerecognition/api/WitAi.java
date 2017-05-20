package com.hhspls.voicerecognition.api;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hhspls.voicerecognition.models.WitOutcome;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
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
import java.util.HashMap;
import java.util.Map;

import ai.api.http.HttpClient;

/**
 * Created by HICT-HP on 5/18/2017.
 */

public class WitAi extends AbstractApi {
    private String TAG = "WitAI";
    private String AUTH_TOKEN = "Bearer 2325MDBMKJQS66BM4SDRXBRZS4QE2G2D";
    private String BaseURL = "https://api.wit.ai/";
    private RequestQueue queue;

    //
    //  Toggle between GETRequest (TEST with Hardcoded String) and POSTRequest(SEND RECORDED MP3).
    private boolean test = false;
    private Gson gson;
    private MediaRecorder recorder;
    private Cache cache;
    private Network network;
    private RequestQueue mRequestQueue;

    public WitAi(Context context) {
        super(context);

        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(context);

        // GSON Builder to create classes
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        // Instantiate the cache
        Cache cache = new DiskBasedCache(Environment.getDownloadCacheDirectory(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);

    }


    @Override
    public boolean isTesting(){
        return test;
    }

    @Override
    public void setTesting(boolean testing){
        this.test = testing;
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
        if (test) {
            try {
                String URLParameter = "message?q=Hello";
                String tempString = new GETMethod().execute(BaseURL + URLParameter).get();

                return gson.fromJson(tempString, WitOutcome.class);
            } catch (Exception e) {
                Log.i(TAG, "Error while sending test message");
            }
        } else {
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
        }
        return null;
    }

    @Override
    void cancelImpl() {

    }

    private class POSTMethod extends AsyncTask<String, Void, String> {
        String server_response;
        int responseCode;

        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;
            final String accessToken = AUTH_TOKEN;
            final String header = "Authorization";

            try {
                url = new URL(strings[0]);
                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url.toString(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                System.out.println(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i(TAG, "Error while POSTMethod " + error.getMessage());
                            }
                        }) {

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(header, accessToken);
                        params.put("Accept", "application/json");
                        params.put("Content-Type", "audio/mp3");

                        return params;
                    }

                    @Override
                    public byte[] getBody() {
                        return getLatestRecordingByteArray();
                    }
                };
                // Add the request to the RequestQueue.
                queue.add(stringRequest);
                mRequestQueue.start();
            } catch (Exception e) {
                Log.i(TAG, "Error: " + e);
            }


//            try {
//                url = new URL(strings[0]);
//                urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.setRequestMethod("POST");
//                urlConnection.setRequestProperty(header, accessToken);
//                urlConnection.setRequestProperty("Content-Type", "audio/mp3");
//                urlConnection.setRequestProperty("Transfer-encoding", "chunked");
//                urlConnection.setDoOutput(true);
//                urlConnection.setChunkedStreamingMode(0);
//
//                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
//                byte[] tempArray = getLatestRecordingByteArray();
//                out.write(tempArray);
//
//                System.out.println(urlConnection.getContent().toString());
//
//                responseCode = urlConnection.getResponseCode();
//                if (responseCode == HttpURLConnection.HTTP_OK) {
//                    server_response = readStream(urlConnection.getInputStream());
//                    return server_response;
//                } else {
//                    Log.i(TAG, "Server error response: " + urlConnection.getErrorStream());
//                }
//            } catch (IOException e) {
//                Log.i(TAG, "Error while POSTMethod " + e);
//            } finally {
//                assert urlConnection != null;
//                urlConnection.disconnect();
//            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            Log.e("Response ", responseCode + " " + server_response);
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
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "myrecording.mp3");
            Log.i(TAG, "Returning ByteArray with length: " + file.length());
            return new byte[(int) file.length()];
        } catch (Exception e) {
            Log.i(TAG, "Error while getLatestRecordingByteArray: " + e);
            return null;
        }

    }

    private File getLatestRecording() {
        File sdcard = Environment.getExternalStorageDirectory();
        return new File(sdcard, "myrecording.mp3");
    }

}
