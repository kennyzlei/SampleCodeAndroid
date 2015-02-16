package com.example.android.samplecode;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Kenny on 2/15/2015.
 */
public class WeatherPullService extends IntentService {
    public WeatherPullService()
    {
        super(WeatherPullService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        Bundle bundle = updateWeather();

        if (bundle != null)
        {
            receiver.send(1, bundle);
        }

    }

    private Bundle updateWeather()
    {
        String weatherJSONString = fetchJSONString("http://weatherapi.market.xiaomi.com/wtr-v2/weather?cityId=101010100&imei=529e2dd3d767bdd3595eec30dd481050&device=pisces&miuiVersion=JXCCNBD20.0&modDevice=&source=miuiWeatherApp");
        JSONObject weatherJSON;
        String city;
        String realtime_temp;
        Bundle bundle = new Bundle();
        try {
            weatherJSON = new JSONObject(weatherJSONString);
            city = weatherJSON.getJSONObject("forecast").getString("city_en");
            realtime_temp = weatherJSON.getJSONObject("realtime").getString("temp");

            bundle.putString("city", city);
            bundle.putString("realtime_temp", realtime_temp);

            return bundle;
        }
        catch (JSONException e)
        {
            Log.e("WeatherFragment", "Error converting string to JSONObject", e);
        }
        return null;
    }

    private String fetchJSONString(String urlString)
    {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // raw JSON response as a string.
        String weatherJSONString = null;

        try
        {
            URL url = new URL(urlString);

            // create connection to url
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // read the input stream into a buffer
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null)
            {
                // no input stream
                weatherJSONString = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null)
            {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0)
            {
                // empty stream
                weatherJSONString = null;
            }

            inputStream.close();

            // finished reading, convert string buffer to string
            weatherJSONString = buffer.toString();
        }
        catch (IOException e)
        {
            Log.e("WeatherPullService", "Error ", e);
            weatherJSONString = null;
        }
        finally
        {
            if (urlConnection != null)
            {
                urlConnection.disconnect();
            }
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (final IOException e)
                {
                    Log.e("WeatherPullService", "Error closing stream", e);
                }
            }
        }
        return weatherJSONString;
    }
}


