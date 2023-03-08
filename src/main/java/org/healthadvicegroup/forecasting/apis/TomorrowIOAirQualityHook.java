package org.healthadvicegroup.forecasting.apis;

import com.google.gson.JsonObject;
import com.squareup.okhttp.*;
import org.healthadvicegroup.Main;
import org.healthadvicegroup.forecasting.WeatherLocation;

import java.io.IOException;

public class TomorrowIOAirQualityHook {

    /*
    const options = {
  method: 'GET',
  url: 'https://air-quality.p.rapidapi.com/history/airquality',
  params: {lon: '-78.638', lat: '35.779'},
  headers: {
    'X-RapidAPI-Key': '9fd5c4f730mshd188cc808e821a2p116cccjsnd227999c7970',
    'X-RapidAPI-Host': 'air-quality.p.rapidapi.com'
  }
};
     */

    private final static String key = "NXWKlspInLd4CxaFL8fJy5UQ9Gtrr77x";
    private final static String host = "air-quality.p.rapidapi.com";
    private final static OkHttpClient okHttpClient = new OkHttpClient();

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
            }

            @Override
            public void onResponse(Response response) throws IOException {
                location.updateAirQualityData(Main.getGSON().fromJson(response.body().string(), JsonObject.class));
            }
        });
    }

}
