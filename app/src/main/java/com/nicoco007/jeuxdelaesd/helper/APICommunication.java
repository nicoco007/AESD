package com.nicoco007.jeuxdelaesd.helper;

import android.util.Log;

import com.nicoco007.jeuxdelaesd.events.EventHandler;
import com.nicoco007.jeuxdelaesd.onlinemodel.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class APICommunication {
    private static final String TAG = "APICommunication";

    private static ArrayList<Location> locations = new ArrayList<>();

    private static OkHttpClient client;

    public static final EventHandler<ArrayList<Location>> onLocationsUpdatedEventHandler = new EventHandler<>();

    private static void initClient() {
        if (client == null) {
            CookieManager manager = new CookieManager();
            manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

            client = new OkHttpClient.Builder()
                    .cookieJar(new JavaNetCookieJar(manager))
                    .build();
        }
    }

    public static void loadLocations() {
        loadLocations(false);
    }

    public static void loadLocations(boolean forceRefresh) {
        if (forceRefresh || locations.size() == 0) {
            Log.d(TAG, "Loading locations");

            initClient();

            Request request = new Request.Builder()
                    .url("https://www.jeuxdelaesd.com/api/locations")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    ResponseBody body = response.body();

                    if (response.isSuccessful() && body != null) {
                        Log.w(TAG, "Request successful");

                        try {
                            String responseBody = body.string();

                            JSONArray array = new JSONArray(responseBody);

                            locations.clear();

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                locations.add(Location.fromJson(obj));
                            }

                            onLocationsUpdatedEventHandler.raise(locations);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.w(TAG, String.format("Request failed. Response code: %d", response.code()));
                        Log.w(TAG, response.body().string());
                    }
                }
            });
        } else {
            onLocationsUpdatedEventHandler.raise(locations);
        }
    }
}
