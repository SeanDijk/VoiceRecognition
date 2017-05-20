package com.hhspls.voicerecognition;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hhspls.voicerecognition.api.AbstractApi;
import com.hhspls.voicerecognition.models.WitOutcome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ai.wit.sdk.Wit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SpeechFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpeechFragment extends Fragment implements AbstractApi.ListenListener {
    private static final String ARG_API_INTERFACE = "ARG_API_INTERFACE";
    private String TAG = "SpeechFragment";
    AbstractApi apiInterface;
    Button button;
    TextView tvResult;
    TextView tvResultCode;
    ToggleButton toggleButton;
    ImageView imageView;

    public SpeechFragment() {
        // Required empty public constructor
    }

    public static SpeechFragment newInstance(AbstractApi apiInterface) {
        SpeechFragment fragment = new SpeechFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_API_INTERFACE, apiInterface);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            apiInterface = (AbstractApi) getArguments().getSerializable(ARG_API_INTERFACE);
            apiInterface.setListener(this);
//            Toast.makeText(getContext(), "test", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_speech, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button = (Button) view.findViewById(R.id.button);
        tvResult = (TextView) view.findViewById(R.id.tvResult);
        tvResultCode = (TextView) view.findViewById(R.id.tvResultCode);
        toggleButton = (ToggleButton) view.findViewById(R.id.tbRequestMethod);
        imageView = (ImageView) view.findViewById(R.id.logo);

//        try{
//            if(imageView.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.wit_ai_960).getConstantState())){
//                toggleButton.setVisibility(View.VISIBLE);
//            }
//            else{
//                toggleButton.setVisibility(View.INVISIBLE);
//            }
//        }catch (NullPointerException e){
//            //Do nothing
//        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;

                if (!apiInterface.isListening()) {
                    apiInterface.startListening();
                    button.setText(getContext().getText(R.string.stop_recording));
                    tvResultCode.setText("---");
                    tvResult.setText(R.string.test_message);
                } else {
                    try {
                        //TODO: Recognize what class the object is ?
                        WitOutcome witOutcome = (WitOutcome) apiInterface.stopListening();
//                        Object object = apiInterface.stopListening();

                        if (witOutcome != null) {
//                            if (object.equals(WitOutcome.class)) {
//                                WitOutcome witOutcome = (WitOutcome) object;

                            tvResult.setText(witOutcome.get_entities().toString());

//                            } else if (object.equals(GoogleOutcome.class)) {
//                                something like this ? :D
//                            }

                            button.setText(getContext().getText(R.string.start_recording));
//                        }
                        } else {
                            tvResult.setText(R.string.result_error);
                        }
//
                    } catch (Exception e) {
                        Log.i(TAG, "Error while GSON " + e);
                    }
                }
            }
        });

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(apiInterface.isTesting()){
                    apiInterface.setTesting(false);
                }else{
                    apiInterface.setTesting(true);
                }
            }
        });
    }

    @Override
    public void createToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }
}
