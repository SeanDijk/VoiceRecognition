package com.hhspls.voicerecognition;

import android.content.Context;
import android.graphics.Path;

import com.google.cloud.speech.spi.v1.SpeechClient;
import com.google.cloud.speech.spi.v1.SpeechSettings;
import com.google.protobuf.ByteString;

import java.io.IOException;
import java.nio.file.*;

/**
 * Created by Sean on 15-5-2017.
 */

public class GoogleSpeech implements ApiInterface {

    SpeechClient speechClient;
    @Override
    public void init(Context context) {
        try {
            speechClient = SpeechClient.create();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startListening() {
        
    }

    @Override
    public void stopListening() {

    }

    @Override
    public void cancel() {

    }
}
