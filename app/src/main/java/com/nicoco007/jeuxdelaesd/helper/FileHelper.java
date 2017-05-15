package com.nicoco007.jeuxdelaesd.helper;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileHelper {
    private static final String TAG = "FileHelper";

    public static void writeString(Context context, String fileName, String data) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
        writer.write(data);
        writer.close();
    }

    public static String readString(Context context, String fileName) throws IOException {
        InputStream inputStream = context.openFileInput(fileName);

        if (inputStream != null) {
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            inputStream.close();
            return stringBuilder.toString();
        }

        return null;
    }
}
