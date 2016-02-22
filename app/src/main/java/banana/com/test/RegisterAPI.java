package banana.com.test;

import android.support.v7.util.SortedList;

import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by CCRH on 2016-02-16.
 */
public interface RegisterAPI {
    @FormUrlEncoded
    @POST("/RetrofitExample/insert.php")
    public void insertUser(
            @Field("name") String name,
            @Field("username") String username,
            @Field("password") String password,
            @Field("email") String email,
            SortedList.Callback<Response> callback);
}
