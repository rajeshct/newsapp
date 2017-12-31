package com.startup.news.application.activities

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.support.customtabs.CustomTabsIntent
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.View
import android.widget.Toast
import com.startup.news.application.R
import com.startup.news.application.adapter.NewsItemAdapter
import com.startup.news.application.interfaces.viewcallback.INewsCallback
import com.startup.news.application.model.apimodel.NewsApiResponse
import com.startup.news.application.mvp.presenter.NewsPresenter
import com.startup.news.application.searchprovider.AppAuthority.AUTHORITY
import com.startup.news.application.searchprovider.AppAuthority.MODE
import kotlinx.android.synthetic.main.activity_news_search_screen.*
import kotlinx.android.synthetic.main.include_message_layout.*
import kotlinx.android.synthetic.main.include_recycler_view.*


class SearchActivity : AppCompatActivity(), NewsItemAdapter.RecyclerViewINewsCallback, INewsCallback, SearchView.OnQueryTextListener {

    private lateinit var newsPresenter: NewsPresenter
    private lateinit var newsItemAdapter: NewsItemAdapter
    private lateinit var newsData: MutableList<NewsApiResponse>
    private var isVisibleToUser = false


    override fun onPause() {
        isVisibleToUser = false
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        isVisibleToUser = true
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_search_screen)
        initialSetUp()
    }

    private fun initialSetUp() {
        pullToRefresh.isEnabled = false
        newsPresenter = NewsPresenter()
        newsPresenter.initializeCallback(this)
        newsData = mutableListOf()
        newsItemAdapter = NewsItemAdapter(newsData)
        newsItemAdapter.initializeCallback(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = newsItemAdapter
        showSearchMessage()
        searchView.setOnQueryTextListener(this)
        iv_close.setOnClickListener { finish() }
        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            newsPresenter.searchNews(query)
            saveQuery(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    override fun showProgress() {
        if (isVisibleToUser)
            loading.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        if (isVisibleToUser)
            loading.visibility = View.GONE
    }

    override fun showMessage(message: String) {
        if (isVisibleToUser)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun noData() {
        if (isVisibleToUser) {
            noData.visibility = View.VISIBLE
            imageContent.setImageResource(R.drawable.ic_no_data)
            textData.text = getString(R.string.noData)
        }
    }

    private fun showSearchMessage() {
        noData.visibility = View.VISIBLE
        imageContent.setImageResource(R.drawable.ic_search)
        textData.text = getString(R.string.search_message)
    }


    override fun newsData(data: MutableList<NewsApiResponse>) {
        if (isVisibleToUser) {
            noData.visibility = View.GONE
            newsData.clear()
            newsData.addAll(data)
            newsItemAdapter.notifyDataSetChanged()
        }
    }

    override fun openUrl(url: String?) {
        if (url != null && isVisibleToUser) {
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(this, Uri.parse(url))
        }
    }

    private fun saveQuery(data: String) {
        val suggestions = SearchRecentSuggestions(this, AUTHORITY, MODE)
        suggestions.saveRecentQuery(data, null)
    }

    override fun onNewIntent(intent: Intent) {
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            doMySearch(query)
        }
    }

    private fun doMySearch(query: String?) {
        if (query != null) {
            searchView.setQuery(query, true)
        }
    }

}
