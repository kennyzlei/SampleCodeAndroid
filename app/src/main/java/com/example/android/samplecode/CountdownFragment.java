package com.example.android.samplecode;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Kenny on 2/15/2015.
 */
public class CountdownFragment extends Fragment{
    public CountdownFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_countdown, container, false);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(2);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        startTimer(11000);
    }


    private void startTimer(long milliseconds)
    {
        new CountDownTimer(milliseconds, 1000) {
            private TextView countdownText = (TextView) getView().findViewById(R.id.countdownView);

            public void onTick(long millisUntilFinished) {
                countdownText.setText(Long.toString(millisUntilFinished / 1000));
            }

            public void onFinish() {
                countdownText.setText("");
            }
        }.start();
    }
}
