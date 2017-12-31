package com.startup.news.application.fragments

import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.startup.news.application.R
import com.startup.news.application.adapter.NewsItemAdapter
import com.startup.news.application.interfaces.viewcallback.INewsCallback
import com.startup.news.application.model.apimodel.CategoryFirebaseResponse
import com.startup.news.application.model.apimodel.NewsApiResponse
import com.startup.news.application.mvp.presenter.NewsPresenter
import kotlinx.android.synthetic.main.include_recycler_view.*

/**
 * Created by rajesh on 27/12/17.
 */

class NewsFragmentItem : Fragment(), INewsCallback, NewsItemAdapter.RecyclerViewINewsCallback {


    private lateinit var category: CategoryFirebaseResponse
    private lateinit var newsItemAdapter: NewsItemAdapter
    private lateinit var newsData: MutableList<NewsApiResponse>

    object Instance {
        fun getInstance(category: CategoryFirebaseResponse): NewsFragmentItem {
            val newsFragment = NewsFragmentItem()
            newsFragment.category = category
            return newsFragment
        }
    }

    override fun showProgress() {
        if (context != null)
            loading.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        if (context != null)
            loading.visibility = View.GONE
    }

    override fun showMessage(message: String) {
        if (context != null)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun noData() {
        if (context != null)
            noData.visibility = View.VISIBLE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_news_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsData = mutableListOf()
        newsItemAdapter = NewsItemAdapter(newsData)
        newsItemAdapter.initializeCallback(this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = newsItemAdapter
        pullToRefresh.setOnRefreshListener {
            if (loading.visibility != View.VISIBLE) {
                fetchDataFromServer()
            } else {
                pullToRefresh.isRefreshing = false
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fetchDataFromServer()
    }

    private fun fetchDataFromServer() {
        if (newsData.isEmpty()) {
            val newsPresenter = NewsPresenter()
            newsPresenter.initializeCallback(this)
            newsPresenter.fetchNewsWithSource(category.source)
        }
    }

    override fun newsData(data: MutableList<NewsApiResponse>) {
        if (context != null) {
            pullToRefresh.isEnabled = false
            newsData.addAll(data)
            newsItemAdapter.notifyDataSetChanged()
        }
    }

    override fun openUrl(url: String?) {
        if (url != null && context != null) {
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(context, Uri.parse(url))
        }
    }
}
