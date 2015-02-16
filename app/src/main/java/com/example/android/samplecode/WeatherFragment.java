package com.example.android.samplecode;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by Kenny on 2/15/2015.
 */
public class WeatherFragment extends Fragment implements WeatherResultReceiver.Receiver {

    private WeatherResultReceiver receiver;

    public WeatherFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(1);
    }

    @Override
    public void onStart()
    {
        super.onStart();

        // start weather intent service
        receiver = new WeatherResultReceiver(new Handler());
        receiver.setReceiver(this);
        Intent intent = new Intent(getActivity(), WeatherPullService.class);

        // pass receiver to intent
        intent.putExtra("receiver", receiver);

        getActivity().startService(intent);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        TextView citytextView = (TextView) getView().findViewById(R.id.citytextView);
        TextView realtimetempView = (TextView) getView().findViewById(R.id.realtimetempView);

        // update weather ui
        citytextView.setText(capitalize(resultData.getString("city")));
        realtimetempView.setText(resultData.getString("realtime_temp") + " °C");
    }

    private String capitalize(String line)
    {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

}
