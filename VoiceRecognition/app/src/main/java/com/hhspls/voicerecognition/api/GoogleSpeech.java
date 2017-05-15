package com.hhspls.voicerecognition.api;

import android.content.Context;

import com.google.cloud.speech.spi.v1.SpeechClient;

import java.io.IOException;
import java.nio.file.*;

/**
 * Created by Sean on 15-5-2017.
 */

public class GoogleSpeech extends AbstractApi {

    SpeechClient speechClient;

    public GoogleSpeech(Context context)
    {
        super(context);
        try {
            speechClient = SpeechClient.create();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    void startListeningImpl() {

    }

    @Override
    void stopListeningImpl() {

    }

    @Override
    void cancelImpl() {

    }
}
