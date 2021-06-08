package com.atleta.api;

import com.atleta.models.Session;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AtletaApiServices {

    // Register new user
    @FormUrlEncoded
    @POST("notes/user/register")
    Single<Session> register(@Field("device_id") String deviceId);

}
