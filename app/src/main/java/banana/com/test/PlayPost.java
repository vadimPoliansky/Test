package banana.com.test;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PlayPost extends AsyncTask<URL, Integer, String> {
    private Exception exceptionToBeThrown;
    protected String doInBackground(URL... urls) {
        PlayPost example = new PlayPost();
        String json = example.bowlingJson("Jesse", "Jake");
        String response = "";
        try {
            response = example.post("http://10.0.2.2:9000/test", json);
            Log.i("ZZ1:","A" + response);
            Log.i("ZZ3","asdsdasd");
        } catch (Exception e){
            exceptionToBeThrown = e;
            e.printStackTrace();
            response = e.toString();
            Log.i("ZZ2:",response);
        }
        return response;
    }

    protected void onProgressUpdate(Integer... progress) {

    }

    protected void onPostExecute(String result) {

    }


    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        //return response.body().string();
        return response.message();
    }

    String bowlingJson(String player1, String player2) {
        return "{\"test\":\"test1\"}";
    }
}