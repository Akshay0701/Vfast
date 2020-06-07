package com.example.vfast.notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {


@Headers({
        "Context-Type:application/json",
        "Authorization:key=AAAAV0m1Ubw:APA91bHezX0e28MNShGsJxecknmVqxij5Phb9qpi0dwr5WZw5BXysen-udtc_3ibH6WEBwLW-u3yWv8xWWuUKlTVY1w37rRwU1mh8_uU_W_UpHEb8yp0_uJnyBnQxQEryVIHQFI13qg4"
})

    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);


}
