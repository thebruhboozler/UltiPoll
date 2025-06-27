package com.ultipoll

import com.ultipoll.dataclasses.GitHubBlob
import com.ultipoll.dataclasses.TreeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface ApiService {
    @GET("repos/thebruhboozler/UltiPoll/git/trees/master?recursive=1")
    fun getFiles():Call<TreeResponse>

    @GET("repos/thebruhboozler/UltiPoll/git/blobs/{id}")
    fun getFile(@Path("id") fileId: String):Call<GitHubBlob>
}