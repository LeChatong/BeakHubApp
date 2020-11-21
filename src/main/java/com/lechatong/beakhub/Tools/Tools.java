package com.lechatong.beakhub.Tools;

import android.content.Context;
import android.net.ConnectivityManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Tools {
    public static boolean isOnline(Context Context) {
        ConnectivityManager cm =
                (ConnectivityManager) Context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public static DateFormat FormatDateHeureMin() {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm");
    }

    public static DateFormat FormatDate() {
        return new SimpleDateFormat("dd-MM-yyyy");
    }

    public static DateFormat FormatDateAnneeBegin() {
        return new SimpleDateFormat("yyyy-MM-dd");
    }

    public static DateFormat FormatDateAnneeBeginHeureMin() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm");
    }

    public static String getMyObject(String url, String MethodeEnvoi) {
        StringBuilder builder = new StringBuilder();
        String readUrl = null;
        try {
            URL urlReq = new URL(url);
            HttpURLConnection connection;
            connection = (HttpURLConnection) urlReq.openConnection();
            connection.setRequestMethod(MethodeEnvoi);
            connection.setRequestProperty("Content-Type", "application/json; charset=utf8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Method", MethodeEnvoi);
            connection.setConnectTimeout(50000);
            //connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream is;
                is = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                    builder.append(line + "\n");
                }
                is.close();
                connection.disconnect();
                readUrl = builder.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readUrl;
    }
}
