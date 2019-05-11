package com.example.yonseiapp.activities;

import android.app.Activity;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Utils {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient client = new OkHttpClient();
    private static Call post(String url, String json, Callback callback) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    public static void post(JSONObject params, final PostCallBack cb){
        post("http://lattecode.site:8080", params.toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (cb != null)
                    cb.onResponse(null, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response)  {
                if (cb == null)
                    return ;
                try {
                    if (response.isSuccessful()) {
                        String responseStr = response.body().string();
                        cb.onResponse(new JSONObject(responseStr), null);
                        // Do what you want to do with the response.
                    } else {
                        cb.onResponse(null, response.message());
                    }
                } catch (Exception e) {
                    cb.onResponse(null, e.getMessage());
                }

            }
        });
    }

    public interface PostCallBack {
        void onResponse(JSONObject ret, String errMsg);
    }

    public static void toast(final Activity act, final String msg) {
        act.runOnUiThread(new Runnable() {public void run() {
            Toast.makeText(act, msg, Toast.LENGTH_LONG).show();
        }});
    }
}
