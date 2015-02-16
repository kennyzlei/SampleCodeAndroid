package com.example.android.samplecode;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Kenny on 2/15/2015.
 */
public class ImagesFragment extends Fragment {
    // loosely referenced async task for garbage collection
    private WeakReference<DownloadImagesTask> taskWeakReference;
    CustomImageView image1;
    CustomImageView image2;
    CustomImageView image3;

    public ImagesFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_images, container, false);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(3);
    }

    @Override
    public void onStart()
    {
        super.onStart();

        image1 = (CustomImageView) getView().findViewById(R.id.imageView);
        image2 = (CustomImageView) getView().findViewById(R.id.imageView2);
        image3 = (CustomImageView) getView().findViewById(R.id.imageView3);

        DownloadImagesTask task = new DownloadImagesTask(this);
        taskWeakReference = new WeakReference<DownloadImagesTask>(task);
        task.execute();
    }

    private class DownloadImagesTask extends AsyncTask<Void, Void, Bundle> {

        private WeakReference<ImagesFragment> fragmentWeakReference;

        private DownloadImagesTask (ImagesFragment fragment) {
            this.fragmentWeakReference = new WeakReference<ImagesFragment>(fragment);
        }

        @Override
        protected Bundle doInBackground(Void... params) {

            Bundle bundle = new Bundle();
            bundle.putParcelable("0", downloadImage("http://llss.qiniudn.com/forum/image/525d1960c008906923000001_1397820588.jpg"));
            bundle.putParcelable("1", downloadImage("http://llss.qiniudn.com/forum/image/e8275adbeedc48fe9c13cd0efacbabdd_1397877461243.jpg"));
            bundle.putParcelable("2", downloadImage("http://llss.qiniudn.com/uploads/forum/topic/attached_img/5350db2ffcfff258b500dcb2/_____2014-04-18___3.52.33.png"));
            return bundle;
        }

        @Override
        protected void onPostExecute(Bundle response) {
            super.onPostExecute(response);
            if (this.fragmentWeakReference.get() != null) {
                image1.setImageBitmap((Bitmap) response.getParcelable("0"));
                image2.setImageBitmap((Bitmap) response.getParcelable("1"));
                image3.setImageBitmap((Bitmap) response.getParcelable("2"));
            }
        }

        private Bitmap downloadImage(String urlString)
        {
            HttpURLConnection urlConnection = null;
            Bitmap bitmap = null;

            try {
                URL url = new URL(urlString);

                // create connection to url
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // read the input stream into a buffer
                InputStream inputStream = urlConnection.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

                bitmap = BitmapFactory.decodeStream(bufferedInputStream);

                // close streams
                inputStream.close();
                bufferedInputStream.close();
            }
            catch (IOException e)
            {
                Log.e("ImagesFragment", "Error ", e);
            }
            finally
            {
                if (urlConnection != null)
                {
                    urlConnection.disconnect();
                }
            }
            return bitmap;
        }
    }
}
