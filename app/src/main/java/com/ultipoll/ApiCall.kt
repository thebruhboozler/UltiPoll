package com.ultipoll

import android.util.Base64
import android.util.Log
import com.ultipoll.dataclasses.FileDescription
import com.ultipoll.dataclasses.GitHubBlob
import com.ultipoll.dataclasses.TreeResponse
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory


class ApiCall {
    fun getFileDescriptors(callback: (List<FileDescription>) -> Unit) {
        val retrofit = Retrofit.Builder().baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create()).build()

        val service = retrofit.create<ApiService>(ApiService::class.java)

        val call = service.getFiles()

        call.enqueue(object : Callback<TreeResponse> {
            override fun onResponse(call: Call<TreeResponse>, response: Response<TreeResponse>) {
                if (response.isSuccessful) {
                    val files = response.body()?.tree ?: emptyList()
                    callback(files)
                }
            }

            override fun onFailure(call: Call<TreeResponse>, t: Throwable) {
                Log.d("ApiCall", "Failed to get files: ${t.message}")
            }
        })
    }

    fun getFile(fileId: String, callback: (String) -> Unit) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val service = retrofit.create<ApiService>(ApiService::class.java)

        val call = service.getFile(fileId)

        call.enqueue(object: Callback<GitHubBlob> {
            override fun onResponse(
                call: Call<GitHubBlob?>,
                response: Response<GitHubBlob?>
            ) {
                if(response.isSuccessful){
                    val blob = response.body() as GitHubBlob
                    val decoded = Base64.decode(blob.content ?: "" , Base64.DEFAULT)
                    callback(String(decoded))
                }else{
                    Log.d("error" , "error in response")
                }
            }
            override fun onFailure(
                call: Call<GitHubBlob?>,
                t: Throwable
            ) {
                Log.d("onFailure","API call failed: ${t.message}")
            }
        })
    }

}