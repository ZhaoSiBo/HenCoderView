package com.starts.hencoderview.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**

 *文件描述：.
 *作者：Created by LostStars on 2020/11/17.
 *版本号：1.0

 */
interface ApiService {
    @GET ("users/{user}/repos")
    fun requestUserInfo(@Path("user") user:String):Call<Any>

}