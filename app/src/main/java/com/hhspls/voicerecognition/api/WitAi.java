package com.hhspls.voicerecognition.api;

import android.app.Activity;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hhspls.voicerecognition.R;

import java.util.ArrayList;

import ai.wit.sdk.IWitListener;
import ai.wit.sdk.Wit;
import ai.wit.sdk.model.WitOutcome;

/**
 * Created by HICT-HP on 5/18/2017.
 */

public class WitAi extends AbstractApi implements IWitListener {
    private String TAG = "Wit.AI";
    private Wit _wit;
    private boolean isListening;
    private Context context;
    private View rootView;

    public WitAi(Context context) {
        super(context);

        String accessToken = "YOUR CLIENT ACCESS TOKEN HERE";
        _wit = new Wit(accessToken, this);
        _wit.enableContextLocation(context);
        rootView = ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);

        this.context = context;
        isListening = false;

    }

    @Override
    void startListeningImpl() {
        try {
            if (!isListening) {
                isListening = true;
                _wit.toggleListening();
            } else {
                Log.i(TAG, "Already started listening");
            }
        } catch (Exception e) {
            Log.i(TAG, "Error while trying to start listening " + e);
        }
    }

    @Override
    void stopListeningImpl() {
        try {
            if (isListening) {
                isListening = false;
                _wit.toggleListening();
            } else {
                Log.i(TAG, "Already stopped listening");
            }
        } catch (Exception e) {
            Log.i(TAG, "Error while trying to start listening " + e);
        }
    }

    @Override
    void cancelImpl() {

    }

    @Override
    public void witDidGraspIntent(ArrayList<WitOutcome> arrayList, String s, Error error) {

        TextView message = (TextView) rootView.findViewById(R.id.message);
        message.setMovementMethod(new ScrollingMovementMethod());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        if (error != null) {
            message.setText(error.getLocalizedMessage());
            return;
        }
        String jsonOutput = gson.toJson(arrayList);
        message.setText(jsonOutput);
        ((TextView) rootView.findViewById(R.id.message)).setText("Done!");
    }

    @Override
    public void witDidStartListening() {
        ((TextView) rootView.findViewById(R.id.message)).setText("Witting...");
    }

    @Override
    public void witDidStopListening() {
        ((TextView) rootView.findViewById(R.id.message)).setText("Processing...");
    }

    @Override
    public void witActivityDetectorStarted() {
        ((TextView) rootView.findViewById(R.id.message)).setText("Listening");
    }

    @Override
    public String witGenerateMessageId() {
        return null;
    }
}
