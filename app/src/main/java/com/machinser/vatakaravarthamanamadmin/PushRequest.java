package com.machinser.vatakaravarthamanamadmin;



import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by asnim on 07/07/17.
 */

public interface PushRequest {


    @POST("/vatakaravarthamanam/sendnotification")
    Call<ResponseOnPush> sendOrdinaryNotifiction(@Body Notf notf);
}
