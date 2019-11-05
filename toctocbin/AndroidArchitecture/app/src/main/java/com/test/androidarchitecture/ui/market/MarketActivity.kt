package com.test.androidarchitecture.ui.market

import android.os.Bundle
import androidx.lifecycle.Observer
import com.test.androidarchitecture.R
import com.test.androidarchitecture.adpter.ViewPagerAdapter
import com.test.androidarchitecture.base.BaseActivity
import com.test.androidarchitecture.databinding.ActivityMarketBinding

class MarketActivity
    : BaseActivity<ActivityMarketBinding, MarketViewModel>(R.layout.activity_market){

    private lateinit var viewPagerAdapter: ViewPagerAdapter
    override val vm = MarketViewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.mainTabLayout.setupWithViewPager(binding.mainViewPager)
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        with(binding.mainViewPager) {
            offscreenPageLimit = 3
            adapter = viewPagerAdapter
        }
        setObservableCallBack()
    }

    private fun setObservableCallBack(){
        vm.marketTitle.observe(this, Observer { viewPagerAdapter.setData(it)  })
    }
}