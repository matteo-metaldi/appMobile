package ch.supsi.dti.isin.meteoapp.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Objects;
import java.util.UUID;

import ch.supsi.dti.isin.meteoapp.R;
import ch.supsi.dti.isin.meteoapp.model.LocationsHolder;
import ch.supsi.dti.isin.meteoapp.model.Location;

public class DetailLocationFragment extends Fragment {
    private static final String ARG_LOCATION_ID = "location_id";

    private Location mLocation;

    private TextView mIdTextView;
    private ImageView imageView;
    private TextView tempAttuale;
    private TextView tempMinMax;
    private TextView windSpeed;
    private TextView humidityText;
    private TextView pressureText;

    // TextView tvResult;
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appId = "e156fc1592d15fd93d5e9c27c6fec654";
    DecimalFormat df = new DecimalFormat("#.#");

    public static DetailLocationFragment newInstance(UUID locationId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_LOCATION_ID, locationId);

        DetailLocationFragment fragment = new DetailLocationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID locationId = (UUID) getArguments().getSerializable(ARG_LOCATION_ID);
        mLocation = LocationsHolder.get(getActivity()).getLocation(locationId);
        //setInformation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail_location, container, false);

        mIdTextView = v.findViewById(R.id.id_textView);
        //mIdTextView.setText(mLocation.getName());
        tempAttuale = v.findViewById(R.id.id_temp_attuale);
        imageView = v.findViewById(R.id.imageView2);
        tempMinMax = v.findViewById(R.id.id_maxmin_temp);
        windSpeed = v.findViewById(R.id.id_wind_data);
        humidityText = v.findViewById(R.id.id_umidity_data);
        pressureText = v.findViewById(R.id.id_pressure_data);
        setInformation();
        return v;
    }

    private void setInformation() {
        //String city = (String) data.getSerializableExtra("return_city");
        String tempUrl = "";
        String city = "New York";
        if (city.equals("")) {
            //mTextViewResult.setText("City field can not be empty!");
        } else {
            tempUrl = url + "?q=" + city + "&appid=" + appId;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                        String idIcon = jsonObjectWeather.getString("icon");
                        Picasso.with(getContext()).load("https://openweathermap.org/img/wn/" + idIcon + "@2x.png")
                                .into(imageView);
                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                        double temp = jsonObjectMain.getDouble("temp") - 273.15;
                        double tempMin = jsonObjectMain.getDouble("temp_min") - 273.15;
                        double tempMax = jsonObjectMain.getDouble("temp_max") - 273.15;
                        float pressure = jsonObjectMain.getInt("pressure");
                        int humidity = jsonObjectMain.getInt("humidity");
                        JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                        String wind = jsonObjectWind.getString("speed");
                        JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                        String countryName = jsonObjectSys.getString("country");
                        String cityName = jsonResponse.getString("name");

                        mIdTextView.setText(cityName + "(" + countryName + ")");
                        tempAttuale.setText(df.format(temp) + "°C");
                        tempMinMax.setText(df.format(tempMin) + " °C / " + df.format(tempMax) + "°C");
                        windSpeed.setText(wind + "m/s");
                        humidityText.setText(humidity + "%");
                        pressureText.setText(pressure + " hPa");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
            requestQueue.add(stringRequest);
        }
    }
}

