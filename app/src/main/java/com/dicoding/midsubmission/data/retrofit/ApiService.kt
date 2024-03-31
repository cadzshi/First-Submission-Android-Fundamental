package com.dicoding.midsubmission.data.retrofit

import com.dicoding.midsubmission.data.response.DetailUserResponse
import com.dicoding.midsubmission.data.response.GithubResponse
import com.dicoding.midsubmission.data.response.ItemsItem
import retrofit2.Call
import retrofit2.http.*


interface ApiService {
    @GET("search/users")
    fun getGithubUser(
        @Query("q") login: String
    ): Call<GithubResponse>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollower(
        @Path("username") follower: String?
    ): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") following: String?
    ): Call<List<ItemsItem>>

}