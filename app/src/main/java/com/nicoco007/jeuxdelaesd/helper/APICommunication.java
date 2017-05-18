/*
 * Copyright 2016–2017 Nicolas Gnyra
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nicoco007.jeuxdelaesd.helper;

import android.content.Context;
import android.util.Log;

import com.nicoco007.jeuxdelaesd.events.EventHandler;
import com.nicoco007.jeuxdelaesd.events.LocationsUpdatedEventArgs;
import com.nicoco007.jeuxdelaesd.model.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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

    public static final EventHandler<LocationsUpdatedEventArgs> onLocationsUpdatedEventHandler = new EventHandler<>();

    private static void initClient() {
        if (client == null) {
            CookieManager manager = new CookieManager();
            manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

            client = new OkHttpClient.Builder()
                    .cookieJar(new JavaNetCookieJar(manager))
                    .build();
        }
    }

    public static void loadLocations(final Context context) {
        loadLocations(context, false);
    }

    // TODO: this is to fix ConcurrentModificationException in MapFragment.onLocationsUpdated(), find better way of fixing
    private static boolean running = false;

    public static void loadLocations(final Context context, final boolean forceRefresh) {
        if (running)
            return;

        final String fileName = "locations.json";

        if (forceRefresh || locations.size() == 0) {
            running = true;

            Log.d(TAG, "Loading locations");

            initClient();

            Request request = new Request.Builder()
                    .url("https://www.jeuxdelaesd.com/api/locations")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException ex) {
                    Log.w(TAG, "Failed to send request: " + ex.getMessage());
                    Log.i(TAG, "Attempting to load cached data from file.");

                    boolean success = false;

                    try {
                        File file = context.getFileStreamPath(fileName);

                        if (!forceRefresh && file != null && file.exists()) {
                            String contents = FileHelper.readString(context, fileName);

                            JSONArray array = new JSONArray(contents);

                            locations.clear();

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                locations.add(Location.fromJson(obj));
                            }

                            success = true;
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "Failed to parse response: " + e.getMessage());
                        e.printStackTrace();
                    } catch (IOException e) {
                        Log.e(TAG, "Failed to read data from file: " + e.getMessage());
                        e.printStackTrace();
                    }

                    onLocationsUpdatedEventHandler.raise(new LocationsUpdatedEventArgs(success, locations));

                    running = false;
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    ResponseBody body = response.body();

                    boolean success = false;

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

                            FileHelper.writeString(context, "locations.json", responseBody);

                            success = true;
                        } catch (JSONException e) {
                            Log.e(TAG, "Failed to parse response: " + e.getMessage());
                            e.printStackTrace();
                        } catch (IOException e) {
                            Log.e(TAG, "Failed to save data to file: " + e.getMessage());
                            e.printStackTrace();
                        }
                    } else {
                        Log.e(TAG, String.format("Request succeeded but server responded with error code %d", response.code()));
                    }

                    onLocationsUpdatedEventHandler.raise(new LocationsUpdatedEventArgs(success, locations));

                    running = false;
                }
            });
        } else {
            onLocationsUpdatedEventHandler.raise(new LocationsUpdatedEventArgs(true, locations));
        }
    }
}
