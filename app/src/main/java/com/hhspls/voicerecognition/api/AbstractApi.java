package com.hhspls.voicerecognition.api;

import android.content.Context;
import android.util.Log;

import java.io.Serializable;


/**
 * Created by Sean on 15-5-2017.
 */

public abstract class AbstractApi implements Serializable {
    private static final String TAG = "AbstractApi";
    boolean isListening = false;
    public ListenListener listener;
    boolean isTesting;

    public AbstractApi(Context context) {
    }

    public void setListener(ListenListener listener) {
        this.listener = listener;
    }

    final public void startListening() {
        if (!isListening) {
            isListening = true;
            startListeningImpl();
        } else {
            Log.d(TAG, "startListening: Already listening");
            listener.createToast("Already listening");
        }
    }

    final public Object stopListening() {
        isListening = false;
        return stopListeningImpl();
    }

    final public void cancel() {
        isListening = false;
        cancelImpl();
    }

    abstract void startListeningImpl();

    abstract Object stopListeningImpl();

    abstract void cancelImpl();


    public boolean isListening() {
        return isListening;
    }

    public interface ListenListener {
        void createToast(String text);
    }

    public boolean isTesting() {
        return isTesting;
    }

    public void setTesting(boolean testing) {
        isTesting = testing;
    }
}
