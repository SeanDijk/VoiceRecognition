package com.hhspls.voicerecognition;

import android.content.Context;

/**
 * Created by Sean on 15-5-2017.
 */

public interface ApiInterface {
    void init(Context context);
    void startListening();
    void stopListening();
    void cancel();

}
