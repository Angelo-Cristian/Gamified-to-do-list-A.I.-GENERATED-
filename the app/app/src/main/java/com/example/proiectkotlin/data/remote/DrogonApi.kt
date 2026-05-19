package com.example.proiectkotlin.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url

interface DrogonApi {
    @POST("register")
    suspend fun register(@Body credentials: UserCredentials): DrogonResponse

    @GET
    suspend fun login(@Url url: String): UserCredentials

    @POST("update_user")
    suspend fun sendUserStats(@Body stats: UserStats): DrogonResponse

    @GET("get_user")
    suspend fun getUserStats(): UserStats
    
    @GET
    suspend fun getUserByEmail(@Url url: String): UserStats

    @GET
    suspend fun getUserByName(@Url url: String): UserStats

    @POST("send_friend_request")
    suspend fun sendFriendRequest(
        @Query("fromName") fromName: String,
        @Query("toName") toName: String
    ): Response<DrogonResponse>

    @GET("get_pending_requests")
    suspend fun getPendingRequests(
        @Query("userName") userName: String
    ): Response<List<String>>

    @POST("respond_to_request")
    suspend fun respondToFriendRequest(
        @Query("userName") userName: String,
        @Query("friendName") friendName: String,
        @Query("accept") accept: Boolean
    ): Response<DrogonResponse>

    @GET("get_friends")
    suspend fun getFriendsList(
        @Query("userName") userName: String
    ): Response<List<String>>
}
