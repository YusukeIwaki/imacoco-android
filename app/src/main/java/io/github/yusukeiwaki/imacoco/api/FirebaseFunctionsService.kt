package io.github.yusukeiwaki.imacoco.api

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FirebaseFunctionsService {
    @GET("/requestPositioning")
    fun requestPositioning(@Query("uid") uid: String): Call<Response<Void>>
}
