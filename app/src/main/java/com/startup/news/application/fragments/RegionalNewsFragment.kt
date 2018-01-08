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
import com.startup.news.application.interfaces.databasecallback.ICategoryRecordCallback
import com.startup.news.application.interfaces.viewcallback.INewsCallback
import com.startup.news.application.localdatabase.databaseoperation.CategoryRecordDatabaseOperation
import com.startup.news.application.model.apimodel.CategoryFirebaseResponse
import com.startup.news.application.model.apimodel.NewsApiResponse
import com.startup.news.application.mvp.presenter.NewsPresenter
import kotlinx.android.synthetic.main.include_recycler_view.*

/**
 * Created by admin on 1/2/2018.
 */
class RegionalNewsFragment : Fragment(), INewsCallback, NewsItemAdapter.RecyclerViewINewsCallback, ICategoryRecordCallback {

    //var data: MutableList<NewsSourceResponse>? = null
    private lateinit var newsItemAdapter: NewsItemAdapter
    private lateinit var newsData: MutableList<NewsApiResponse>
    private lateinit var categoryDatabase: CategoryRecordDatabaseOperation

    object RegionalInstance {
        fun getInstance(/*data: MutableList<NewsSourceResponse>?*/): RegionalNewsFragment {
            val regionalFragment = RegionalNewsFragment()
            //regionalFragment.data = data
            return regionalFragment
        }
    }


    override fun showProgress() {
        if (context != null && newsData.isEmpty())
            loading.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        if (context != null)
            loading.visibility = View.GONE
    }

    override fun showMessage(message: String) {
        if (context != null && !message.isBlank())
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun noData() {
        if (context != null && newsData.isEmpty())
            noData.visibility = View.VISIBLE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_news_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryDatabase = CategoryRecordDatabaseOperation()
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
        categoryDatabase.getSelectedCategoryRecords(getString(R.string.regional), this)
    }

    private fun fetchDataFromServer() {
        if (newsData.isEmpty()) {
            val newsPresenter = NewsPresenter()
            newsPresenter.initializeCallback(this)
            newsPresenter.fetchRegionalNews()
        }
    }

    override fun newsData(data: MutableList<NewsApiResponse>) {
        updateRecyclerView(data)
        categoryDatabase.insertCategoryRecord(categoryModel = CategoryFirebaseResponse(getString(R.string.regional), ""), data = data
                , iDatabaseSuccessFailureCallback = this)
    }

    override fun openUrl(url: String?) {
        if (url != null && context != null) {
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(context, Uri.parse(url))
        }
    }

    override fun categoryDataFromDb(data: List<NewsApiResponse>) {
        updateRecyclerView(data)
    }

    private fun updateRecyclerView(data: List<NewsApiResponse>) {
        if (context != null) {
            pullToRefresh.isEnabled = false
            newsData.addAll(data)
            newsItemAdapter.notifyDataSetChanged()
        }
    }

    override fun emptyLocalDatabase() {
        fetchDataFromServer()
    }

}
