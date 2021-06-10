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
    @POST("sign-up")
    Single<Session> register(@Field("name") String name, @Field("email") String email, @Field("password") String password, @Field("number") String number,  @Field("gender") String gender , @Field("dob") String dob,  @Field("sports") String sports);

}
