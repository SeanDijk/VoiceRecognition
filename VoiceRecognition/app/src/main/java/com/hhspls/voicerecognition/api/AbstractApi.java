package com.hhspls.voicerecognition.api;

import android.content.Context;
import android.util.Log;

import java.io.Serializable;

/**
 * Created by Sean on 15-5-2017.
 */

public abstract class AbstractApi implements Serializable{
    private static final String TAG = "AbstractApi";
    boolean isListening = false;

    public AbstractApi(Context context)
    {

    }

    final public void startListening()
    {
        if(!isListening) {
            isListening = true;
            startListeningImpl();
        }
        else
            Log.d(TAG, "startListening: Already listening");
    }
    final public void stopListening()
    {
        isListening = false;
        stopListeningImpl();
    }
    final public void cancel()
    {
        isListening = false;
        cancelImpl();
    }

    abstract void startListeningImpl();
    abstract void stopListeningImpl();
    abstract void cancelImpl();


    public boolean isListening() {
        return isListening;
    }
}
