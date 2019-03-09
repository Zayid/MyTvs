package com.tvsauto.mytvs.network;

import com.tvsauto.mytvs.holder.SignInResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface ApiService {

    @POST("reporting/vrm/api/test_new/int/gettabledata.php")
    Call<SignInResponse> signIn(@Body RequestBody params);
}
