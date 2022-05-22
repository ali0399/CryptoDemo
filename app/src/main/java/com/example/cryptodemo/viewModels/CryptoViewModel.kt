package com.example.cryptodemo.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptodemo.models.CryptoListResponse
import com.example.cryptodemo.models.CryptoListResponseItem
import com.example.cryptodemo.network.RetrofitHelper
import com.example.cryptodemo.network.WazirXService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

enum class ApiStatus { LOADING, ERROR, DONE }
private const val TAG="CryptoViewModel"

class CryptoViewModel : ViewModel() {

    val wazirXService = RetrofitHelper.getApiServiceInstance().create(WazirXService::class.java)

    private val _status = MutableLiveData<ApiStatus>()
    private val _cryptoList = MutableLiveData<List<CryptoListResponseItem>>()
    private val _cryptoDetails = MutableLiveData<CryptoListResponseItem>()

    val cryptoList:LiveData<List<CryptoListResponseItem>> get() = _cryptoList
    val cryptoDetail:LiveData<CryptoListResponseItem> get() = _cryptoDetails
    val status: LiveData<ApiStatus> = _status

    fun getTickersList() {
        Log.d(TAG, "getTickersList: start")
        viewModelScope.launch(Dispatchers.IO) {
            _status.postValue(ApiStatus.LOADING)
            try {
                _cryptoList.postValue(wazirXService.getTickersList().body())
                _status.postValue(ApiStatus.DONE)
            }
            catch (e:Exception){
                Log.d(TAG, "getTickersList: error- $e")
                _cryptoList.postValue(CryptoListResponse())
                _status.postValue(ApiStatus.ERROR)
            }
        }
    }

    fun getTicker(symbol:String) {
        Log.d(TAG, "getTickersList: start")
        viewModelScope.launch(Dispatchers.IO) {
            _status.postValue(ApiStatus.LOADING)
            try {
                val result=wazirXService.getTicker(symbol)
                if(result.isSuccessful) {
                    Log.d(TAG, "getTicker: Successfull- ${result.code()}")
                    _cryptoDetails.postValue(wazirXService.getTicker(symbol).body())
                    _status.postValue(ApiStatus.DONE)
                }
                else throw Exception("Api Error Code : ${result.code()}")
            }
            catch (e:Exception){
                Log.d(TAG, "getTickersList: error- $e")
                _status.postValue(ApiStatus.ERROR)
            }
        }
    }


}