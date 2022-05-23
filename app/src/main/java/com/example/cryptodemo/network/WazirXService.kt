package com.example.cryptodemo.network

import com.example.cryptodemo.models.CryptoListResponse
import com.example.cryptodemo.models.CryptoListResponseItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

private const val BaseURL="https://api.wazirx.com"
interface WazirXService {

    @GET("sapi/v1/tickers/24hr")
    suspend fun getTickersList():Response<List<CryptoListResponseItem>>

    @GET("sapi/v1/ticker/24hr")
    suspend fun getTicker(@Query("symbol")symbol:String):Response<CryptoListResponseItem>




}