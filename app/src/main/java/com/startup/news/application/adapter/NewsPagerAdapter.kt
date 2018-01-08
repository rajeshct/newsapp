package com.startup.news.application.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.startup.news.application.model.viewmodel.NewsTabModel


/**
 * Created by rajesh on 27/12/17.
 */
class NewsPagerAdapter(fragmentManager: FragmentManager) : SmartFragmentStatePagerAdapter(fragmentManager) {
    lateinit var slidePages: MutableList<NewsTabModel>

    override fun getItem(position: Int): Fragment? {
        return slidePages[position].fragment
    }

    override fun getCount(): Int = slidePages.size


    override fun getPageTitle(position: Int): CharSequence? {
        return slidePages[position].title
    }


    fun initializeList(slidePages: MutableList<NewsTabModel>) {
        this.slidePages = slidePages
    }

    fun add(temp: NewsTabModel) {
        slidePages.add(0,temp)
        notifyDataSetChanged()
    }
}