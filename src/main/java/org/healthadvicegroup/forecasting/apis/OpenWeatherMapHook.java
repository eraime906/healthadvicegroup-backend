package org.healthadvicegroup.forecasting.apis;

import com.google.gson.JsonObject;
import com.squareup.okhttp.*;
import org.healthadvicegroup.Main;
import org.healthadvicegroup.forecasting.WeatherLocation;

import java.io.IOException;

public class OpenWeatherMapHook {

    private final static String key = "c9ae5503ab042c15f361988a76d36c7a";
    private final static OkHttpClient okHttpClient = new OkHttpClient();

    /**
     * Requests updated data from OpenWeatherMap for the provided {@link WeatherLocation}
     *
     * @param location the location
     */
    @SuppressWarnings("StringBufferReplaceableByString")
    public static void updateWeatherData(WeatherLocation location) {
        Request.Builder builder = new Request.Builder();
        builder.url(new StringBuilder("https://api.openweathermap.org/data/2.5/weather?")
                        .append(String.format("lat=%s", location.getLatitude()))
                        .append(String.format("&lon=%s", location.getLongitude()))
                        .append(String.format("&appid=%s", key))
                        .toString());
        Call call = okHttpClient.newCall(builder.build());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException ex) {
                ex.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                location.updateWeatherData(Main.getGSON().fromJson(response.body().string(), JsonObject.class));
            }
        });
    }


}
