package com.example.suitmedia.thirdscreen

import com.example.suitmedia.BuildConfig
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface UserInterface {
    @GET("users")
    fun getUsers(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 10
    ): Call<UsersResponse>
}

object UsersService {
    val userInstance: UserInterface

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        userInstance = retrofit.create(UserInterface::class.java)
    }
}