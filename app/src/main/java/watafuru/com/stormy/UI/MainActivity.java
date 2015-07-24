package watafuru.com.stormy.UI;

import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import watafuru.com.stormy.R;
import watafuru.com.stormy.weather.Current;
import watafuru.com.stormy.weather.Day;
import watafuru.com.stormy.weather.Forecast;
import watafuru.com.stormy.weather.Hour;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private Forecast mForecast;


    @InjectView(R.id.timeLabel) TextView mTimeLabel;
    @InjectView(R.id.temperatureLabel) TextView mTemperatureLabel;
    @InjectView(R.id.humidityValue) TextView mHumidityValue;
    @InjectView(R.id.precipValue) TextView mPrecipValue;
    @InjectView(R.id.summaryLabel) TextView mSummaryLabel;
    @InjectView(R.id.iconImageView) ImageView mIconImageView;
    @InjectView(R.id.refreshImageView) ImageView mRefreshImageView;
    @InjectView(R.id.progressBar) ProgressBar mProgressBar;
    @InjectView(R.id.locationLabel) TextView mLocationLabel;
    @InjectView(R.id.degreeImageView) ImageView mDegreeImageView;
    @InjectView(R.id.RelativeLayoutMain) RelativeLayout mRelativeLayoutMain;
    @InjectView(R.id.humidityrainLayout) LinearLayout mLinearLayout;

    private Boolean mToggle = true;


    final double mLatitude = 37.8267;
    final double mLongitude = -122.423;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        mProgressBar.setVisibility(View.INVISIBLE);



        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast(mLatitude, mLongitude);
            }
        });

        getForecast(mLatitude,mLongitude);
        Log.d(TAG,"Main UI is running!");

    }

    private void getForecast(double latitude, double longitude) {
        String apiKey = "7b9d2da8bda836a53c0d801645aeb6fb";


        String url = "https://api.forecast.io/forecast/" + apiKey + "/"
                + latitude +"," + longitude;

        if(isNetworkAvaliable()) {

            toggleRefresh();

            //Connects and retrieves data
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    alertUserAboutError();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    try {
                        String jsonData = response.body().string();
                        if (response.isSuccessful()) {
                            //sets mCurrentWeather
                            mForecast = parseForecastDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });
                        } else {
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }
            });
        } else {
            Toast.makeText(this, R.string.NETWORK_UNAVALIABLE, Toast.LENGTH_LONG).show();
        }
    }

    private void toggleRefresh() {
        if (mProgressBar.getVisibility() == View.INVISIBLE) {

            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);

            //mIconImageView.setVisibility(View.INVISIBLE);
            YoYo.with(Techniques.FadeOut).duration(700).playOn(mIconImageView);
            //mSummaryLabel.setVisibility(View.INVISIBLE);
            YoYo.with(Techniques.FadeOut).duration(700).playOn(mSummaryLabel);
            //mLocationLabel.setVisibility(View.INVISIBLE);
            YoYo.with(Techniques.FadeOut).duration(700).playOn(mLocationLabel);
            //mTimeLabel.setVisibility(View.INVISIBLE);
            YoYo.with(Techniques.FadeOut).duration(700).playOn(mTimeLabel);
            //mTemperatureLabel.setVisibility(View.INVISIBLE);
            YoYo.with(Techniques.FadeOut).duration(700).playOn(mTemperatureLabel);
            //mDegreeImageView.setVisibility(View.INVISIBLE);
            YoYo.with(Techniques.FadeOut).duration(700).playOn(mDegreeImageView);
            //mHumidityValue.setVisibility(View.INVISIBLE);
            YoYo.with(Techniques.FadeOut).duration(700).playOn(mHumidityValue);
            //mPrecipValue.setVisibility(View.INVISIBLE);
            YoYo.with(Techniques.FadeOut).duration(700).playOn(mPrecipValue);



        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);

            //mIconImageView.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.FadeIn).duration(700).playOn(mIconImageView);
            //mSummaryLabel.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.FadeIn).duration(700).playOn(mSummaryLabel);
            //mLocationLabel.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.FadeIn).duration(700).playOn(mLocationLabel);
            //mTimeLabel.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.FadeIn).duration(700).playOn(mTimeLabel);
            //mTemperatureLabel.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.FadeIn).duration(700).playOn(mTemperatureLabel);
            //mDegreeImageView.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.FadeIn).duration(700).playOn(mDegreeImageView);
            //mHumidityValue.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.FadeIn).duration(700).playOn(mHumidityValue);
            //mPrecipValue.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.FadeIn).duration(700).playOn(mPrecipValue);

        }
    }

    private void updateDisplay() {
        Current c = mForecast.getCurrent();
        mTemperatureLabel.setText(c.getTemperature()+ "");
        mTimeLabel.setText("At " + c.getFormattedTime() + " it will be ");
        mHumidityValue.setText(c.getHumidity() + "");
        mPrecipValue.setText(c.getPrecipChance() + "%");
        mSummaryLabel.setText(c.getSummary());

        Drawable myDrawable = getResources().getDrawable(c.getIconId());
        mIconImageView.setImageDrawable(myDrawable);

    }

    private Forecast parseForecastDetails(String jsonData) throws JSONException{
        Forecast forecast = new Forecast();
        forecast.setCurrent(getCurrentDetails(jsonData));

        forecast.setHourlyForecast(getHourlyForecast(jsonData));
        forecast.setDailyForecast(getDailyForecast(jsonData));

        return forecast;
    }

    private Day[] getDailyForecast(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject daily = forecast.getJSONObject("daily");
        JSONArray data = daily.getJSONArray("data");

        Day[] days = new Day[data.length()];
        for (int i = 0; i < data.length() ; i++) {
            JSONObject jsonDay = data.getJSONObject(i);
            Day day = new Day();
            day.setSummary(jsonDay.getString("summary"));
            day.setTemperatureMax(jsonDay.getDouble("temperatureMax"));
            day.setIcon(jsonDay.getString("icon"));
            day.setTime(jsonDay.getLong("time"));
            day.setTimezone(timezone);
            days[i] = day;
        }
        return days;
    }

    private Hour[] getHourlyForecast(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject hourly = forecast.getJSONObject("hourly");
        JSONArray data = hourly.getJSONArray("data");

        Hour[] hours = new Hour[data.length()];
        for (int i = 0; i < data.length() ; i++) {
            JSONObject jsonHour = data.getJSONObject(i);
            Hour hour = new Hour();
            hour.setSummary(jsonHour.getString("summary"));
            hour.setTemperature(jsonHour.getDouble("temperature"));
            hour.setIcon(jsonHour.getString("icon"));
            hour.setTime(jsonHour.getLong("time"));
            hour.setTimezone(timezone);
            hours[i] = hour;
        }
        return hours;
    }

    private Current getCurrentDetails(String jsonData) throws JSONException {
        Current weather = new Current();

        //sets up a current weather object with the jsonData
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");

        //gets all the data from currently
        JSONObject currently = forecast.getJSONObject("currently");

        //plugs in data from currently
        String icon = currently.getString("icon");
        long time = currently.getLong("time");
        double temperature = currently.getDouble("temperature");
        double humidity = currently.getDouble("humidity");
        double precipChance = currently.getDouble("precipProbability");
        String summary = currently.getString("summary");

        //sets up the CurrentWeather object to have correct values
        weather.setIcon(icon);
        weather.setTime(time);
        weather.setTemperature(temperature);
        weather.setHumidity(humidity);
        weather.setPrecipChance(precipChance);
        weather.setSummary(summary);
        weather.setTimezone(timezone);

        Log.d(TAG,weather.getFormattedTime());
        return weather;
    }

    private boolean isNetworkAvaliable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        boolean isAvaliable = false;
        if(info != null && info.isConnected()) {
            isAvaliable = true;
        }
        return isAvaliable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(),"error_dialog" );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
