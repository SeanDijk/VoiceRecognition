package com.hhspls.voicerecognition;

import android.content.Context;
import android.util.Log;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;

/**
 * Created by Sean on 15-5-2017.
 */

public class ApiAi implements ApiInterface {
    private static final String TAG = "ApiAi";
    public final AIConfiguration configuration = new AIConfiguration(
            "",
            AIConfiguration.SupportedLanguages.Dutch,
            AIConfiguration.RecognitionEngine.System); // TODO: 15-5-2017 add client access token

    AIService aiService;

    AIListener aiListener = new AIListener() {
        @Override
        public void onResult(AIResponse response) {
            final Result result = response.getResult();

            Log.i(TAG, "Resolved query: " + result.getResolvedQuery());
            Log.i(TAG, "Action: " + result.getAction());
            Log.i(TAG, "Speech: " + result.getFulfillment().getSpeech());

        }

        @Override
        public void onError(AIError error) {

        }

        @Override
        public void onAudioLevel(float level) {

        }

        @Override
        public void onListeningStarted() {

        }

        @Override
        public void onListeningCanceled() {

        }

        @Override
        public void onListeningFinished() {

        }
    };

    @Override
    public void init(Context context) {
        aiService = AIService.getService(context, configuration);
        aiService.setListener(aiListener);
//        aiService.startListening();

    }

    @Override
    public void startListening() {
        aiService.startListening();
    }

    @Override
    public void stopListening() {
        aiService.stopListening();
    }

    @Override
    public void cancel() {
        aiService.cancel();
    }
}
