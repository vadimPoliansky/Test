package banana.com.test;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by CCRH on 2016-02-25.
 */
public class JsonSendPoints extends AsyncTask<Void , Integer, String> {
    public static final String BASE_URL = "http://10.0.2.2:9000/";

    public interface ApiService {
        @POST("/test")
        Call<JsonPoints> createPoints(@Body JsonPoints pointList);
    }

    protected String doInBackground(Void... a) {
        return "";
    }

    public void run(List<Point> pointList, FingerPaintActivity.MyView _mv){
        final FingerPaintActivity.MyView mv = _mv;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPoints jsonPoints = new JsonPoints(pointList);
        ApiService apiService = retrofit.create(ApiService.class);
        Call<JsonPoints> call = apiService.createPoints(jsonPoints);

        call.enqueue(new Callback<JsonPoints>() {
            @Override
            public void onResponse(Call<JsonPoints> call, retrofit2.Response<JsonPoints> response) {
                Log.i("ZZ1", response.body().toString());
                Log.i("ZZ3", call.toString());
                List<Point> pointList = response.body().getPoints();
                for (Point point : pointList) {
                    Log.i("ZZ4", point.toString());
                }
                FingerPaintActivity fpa = new FingerPaintActivity();
                fpa.drawPolyCall(pointList, mv);
            }

            @Override
            public void onFailure(Call<JsonPoints> call, Throwable t) {
                Log.i("ZZ2", t.toString());
                Log.i("ZZ3", call.toString());
            }
        });
    }


    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        JsonSendPoints getService() {
            return JsonSendPoints.this;
        }
    }

    public void onCreate() {
    }
}
