package com.architecturestudy.upbitmarket

import androidx.lifecycle.MutableLiveData
import com.architecturestudy.base.BaseViewModel
import com.architecturestudy.data.upbit.source.UpbitRepository
import com.architecturestudy.util.NumberFormatter

class UpbitViewModel(
    private val upBitRepository: UpbitRepository
) : BaseViewModel() {

    val marketPriceList = MutableLiveData<List<Map<String, String>>>()
    val errMsg = MutableLiveData<Throwable>()

    fun showMarketPrice(market: String) {
        upBitRepository.getMarketPrice(
            market,
            onSuccess = {
                for (i in 0 until it.size) {
                    upBitRepository.saveTicker(it[i])
                }
                marketPriceList.value = NumberFormatter.convertTo(it)
            },
            onFail = {
                errMsg.value = it
            }
        )
    }
}