package com.example.messagingapp.model.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("users/register")
    suspend fun signUp(@Body signUpDto: SignUpDto): Response<Unit>

    @POST("users/login")
    suspend fun login(@Body loginDto: LoginDto): Response<Unit>

    @GET("users/all")
    suspend fun getAllUsernames(): Response<List<String>>
}