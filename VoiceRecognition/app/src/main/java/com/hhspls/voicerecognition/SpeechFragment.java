package com.hhspls.voicerecognition;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hhspls.voicerecognition.api.AbstractApi;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SpeechFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpeechFragment extends Fragment {
    private static final String ARG_API_INTERFACE = "ARG_API_INTERFACE";

    AbstractApi apiInterface;


    Button button;


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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;

                if(!apiInterface.isListening()) {
                    apiInterface.startListening();
                    button.setText(getContext().getText(R.string.stop_recording));
                }
                else
                {
                    apiInterface.stopListening();
                    button.setText(getContext().getText(R.string.start_recording));
                }

            }
        });

    }
}
