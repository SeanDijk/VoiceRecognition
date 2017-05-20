package com.hhspls.voicerecognition.api;

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

public class ApiAi extends AbstractApi {
    private static final String TAG = "ApiAi";
    public final AIConfiguration configuration = new AIConfiguration(
            "a29126364251489493ec9d78b6e3c420",
            AIConfiguration.SupportedLanguages.English,
            AIConfiguration.RecognitionEngine.System);

    AIService aiService;

    AIListener aiListener = new AIListener() {
        @Override
        public void onResult(AIResponse response) {
            listener.createToast("onResult");

            final Result result = response.getResult();

            Log.i(TAG, "Resolved query: " + result.getResolvedQuery());
            Log.i(TAG, "Action: " + result.getAction());
            Log.i(TAG, "Speech: " + result.getFulfillment().getSpeech());
            listener.createToast("Resolved query: " + result.getResolvedQuery());
        }

        @Override
        public void onError(AIError error) {
            listener.createToast("onError");
            listener.createToast(error.getMessage());

        }

        @Override
        public void onAudioLevel(float level) {
//            listener.createToast("onAudioLevel");

        }

        @Override
        public void onListeningStarted() {
//            listener.createToast("onStarted");

        }

        @Override
        public void onListeningCanceled() {
//            listener.createToast("onStopped");

        }

        @Override
        public void onListeningFinished() {
            listener.createToast("onFinished");

        }
    };

    public ApiAi(Context context) {
        super(context);
        aiService = AIService.getService(context, configuration);
        aiService.setListener(aiListener);
    }


    @Override
    void startListeningImpl() {
        aiService.startListening();
        listener.createToast("start listening");

    }

    @Override
    Object stopListeningImpl() {
        aiService.stopListening();
        listener.createToast("stop listening");
        return null;
    }

    @Override
    void cancelImpl() {
        aiService.cancel();
    }
}
