package org.healthadvicegroup.forecasting.apis;

import com.google.gson.JsonObject;
import com.squareup.okhttp.*;
import org.healthadvicegroup.Main;
import org.healthadvicegroup.forecasting.WeatherLocation;

import java.io.IOException;

public class TomorrowIOAirQualityHook {

    public static boolean rateLimited = false;
    private final static String key = "NXWKlspInLd4CxaFL8fJy5UQ9Gtrr77x";
    private final static OkHttpClient okHttpClient = new OkHttpClient();

    /**
     * Requests updated data from TommorowIO for the provided {@link WeatherLocation}
     *
     * @param location the location
     */
    @SuppressWarnings("StringBufferReplaceableByString")
    public static void updateAirQualityData(WeatherLocation location) {
        //
        Request request = new Request.Builder()
                .url(new StringBuilder("https://api.tomorrow.io/v4/timelines?")
                        .append(String.format("location=%s,%s", location.getLongitude(), location.getLatitude()))
                        .append(String.format("&timesteps=1d&units=metric&apikey=%s&fields=treeIndex,grassIndex,weedIndex", key))
                        .toString())
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException ex) {
                ex.printStackTrace();
                rateLimited = true;
            }

            @Override
            public void onResponse(Response response) throws IOException {
                location.updateAirQualityData(Main.getGSON().fromJson(response.body().string(), JsonObject.class));
            }
        });
    }

}
