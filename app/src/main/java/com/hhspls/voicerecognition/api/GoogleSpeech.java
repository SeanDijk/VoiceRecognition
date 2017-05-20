package com.hhspls.voicerecognition.api;

import android.content.Context;



import java.io.IOException;

/**
 * Created by Sean on 15-5-2017.
 */

public class GoogleSpeech extends AbstractApi {

//    SpeechClient speechClient;

    public GoogleSpeech(Context context)
    {
        super(context);
//        try {
//            speechClient = SpeechClient.create();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    void startListeningImpl() {
//        speechClient.longRunningRecognizeAsync(RecognitionConfig.getDefaultInstance(), )
    }

    @Override
    Object stopListeningImpl() {
        return null;
    }

    @Override
    void cancelImpl() {

    }
}
