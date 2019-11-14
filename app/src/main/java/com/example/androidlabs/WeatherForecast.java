package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;
import static org.xmlpull.v1.XmlPullParser.TEXT;

public class WeatherForecast extends AppCompatActivity {

    private TextView wind;
    protected static final String ACTIVITY_NAME = "WeatherForecast";
    private ProgressBar progressBar;
    private TextView currentTemp;
    private TextView minTemp;
    private TextView maxTemp;
    private ImageView weatherImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_forecast);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        currentTemp = findViewById(R.id.textViewTemperature);
        minTemp = findViewById(R.id.minTemperature);
        maxTemp = findViewById(R.id.maxTemperature);
        weatherImage = findViewById(R.id.imageViewWeather);
        wind = findViewById(R.id.uvRating);

        ForecastQuery forecast = new ForecastQuery();
        forecast.execute();

        Log.i(ACTIVITY_NAME, "In onCreate()");
    }

    protected static Bitmap getImage(URL url) {

        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                return BitmapFactory.decodeStream(connection.getInputStream());
            } else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean fileExistance(String fileName) {

        File file = getBaseContext().getFileStreamPath(fileName);
        return file.exists();
    }

    private class ForecastQuery extends AsyncTask<String, Integer, String> {

        String current;
        String min;
        String max;
        Bitmap icon;
        String iconName;
        String windUV;


        @Override
        protected String doInBackground(String... params) {
            String queryURL = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric";
            String ret = null;
            try {       // Connect to the server:
                URL url = new URL(queryURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();

                //Set up the XML parser:
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(inputStream, "UTF-8");


                //Iterate over the XML tags:
                int EVENT_TYPE;
                while ((EVENT_TYPE = xpp.getEventType()) != XmlPullParser.END_DOCUMENT) {
                    switch (EVENT_TYPE) {
                        case START_TAG:

                            String tagName = xpp.getName(); // What kind of tag?

                            if (tagName.equals("temperature")) {
                                current = xpp.getAttributeValue(null, "value");
                                publishProgress(25);
                                min = xpp.getAttributeValue(null, "min");
                                publishProgress(50);
                                max = xpp.getAttributeValue(null, "max");
                                publishProgress(75);
                            }
                            if (tagName.equals("weather")) {

                                iconName = xpp.getAttributeValue(null, "icon");
                                URL iconUrl = new URL("http://openweathermap.org/img/w/" + iconName + ".png");
                                String imageFile = iconName + ".png";

                                if (fileExistance(imageFile)) {
                                    FileInputStream fis = null;

                                    try {
                                        fis = openFileInput(imageFile);
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    icon = BitmapFactory.decodeStream(fis);
                                    Log.i(ACTIVITY_NAME, "File imageFile Exists");
                                } else {
                                    icon = getImage(iconUrl);
                                    FileOutputStream outputStream = openFileOutput(iconName + ".png",
                                            Context.MODE_PRIVATE);
                                    icon.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                    outputStream.flush();
                                    outputStream.close();
                                    Log.i(ACTIVITY_NAME, "Added New Image");
                                }
                                Log.i(ACTIVITY_NAME, "filename=" + imageFile);
                                publishProgress(100);
                            }

                            url = new URL("http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389");
                            urlConnection = (HttpURLConnection) url.openConnection();
                            inputStream = urlConnection.getInputStream();

                            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                            StringBuilder sb = new StringBuilder();
                            String line = null;
                            while ((line = reader.readLine()) != null)
                            {sb.append(line + "\n");}
                            String result = sb.toString();
                            JSONObject jObject = new JSONObject(result);

                            double uv = jObject.getDouble("value");
                            windUV = "UV is " + Double.toString(uv);


                            break;

                        case END_TAG:           //This is an end tag: </ ... >
                            break;
                        case TEXT:              //This is text between tags < ... > Hello world </ ... >
                            break;
                    }
                    xpp.next(); // move the pointer to next XML element
                }
            } catch (MalformedURLException mfe) {
                ret = "Malformed URL exception";
            } catch (IOException ioe) {
                ret = "IO Exception. Is the Wifi connected?";
            } catch (XmlPullParserException pe) {
                ret = "XML Pull exception. The XML is not properly formed";
            } catch (JSONException e) {
                ret = "JSON Exception";
            }
            return ret;




        }//end doinBackground




        @Override
        protected void onProgressUpdate(Integer... value) {
            // Set ProgressBar To Visible
            progressBar.setVisibility(View.VISIBLE);
            // Set Progress Bar To Variable value[0] Being Passed
            progressBar.setProgress(value[0]);
            // Log
            Log.i(ACTIVITY_NAME, "In onProgressUpdate");
        }

        @Override
        protected void onPostExecute(String result) {

            String degree = Character.toString((char) 0x00B0);
            // Update GUI with current Temperature
            currentTemp.setText(String.format("%s%s%sC", currentTemp.getText(), current, degree));
            //Update GUI with Min Temperature
            minTemp.setText(minTemp.getText() + min + degree + "C");
            //Update GUI with Max Temperature
            maxTemp.setText(maxTemp.getText() + max + degree + "C");
            //Update GUI with imageView
            weatherImage.setImageBitmap(icon);
            wind.setText(wind.getText() + windUV);


            //Set Visibility Of Progress Bar to Invisible
            progressBar.setVisibility(View.INVISIBLE);


        }
    }//end forecast

}


