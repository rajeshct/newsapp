package com.startup.news.application

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.PopupMenu
import com.startup.news.application.activities.SearchActivity
import com.startup.news.application.adapter.NewsPagerAdapter
import com.startup.news.application.fragments.NewsFragmentItem
import com.startup.news.application.interfaces.databasecallback.IDatabaseSuccessFailureCallback
import com.startup.news.application.localdatabase.DatabaseOperation
import com.startup.news.application.model.viewmodel.NewsTabModel
import com.startup.news.fragments.SelectCategory
import com.startup.news.localdatabase.tables.CategoryModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), IDatabaseSuccessFailureCallback {

    private lateinit var newsPagerAdapter: NewsPagerAdapter
    private lateinit var pagerData: MutableList<NewsTabModel>
    private var isVisibleToUser = false
    private var delay = 0L

    override fun onPause() {
        isVisibleToUser = false
        super.onPause()
    }

    override fun onBackPressed() {
        if ((delay - System.currentTimeMillis()) > 0) {
            finish()
        } else {
            Snackbar.make(parentLayout, getString(R.string.backPress), Snackbar.LENGTH_SHORT).show()
        }
        delay = System.currentTimeMillis() + 1500L
    }

    override fun onResume() {
        super.onResume()
        isVisibleToUser = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpTab()
        iv_more.setOnClickListener {
            showPopupMenu()
        }
    }

    private fun showPopupMenu() {
        val popup = PopupMenu(this, iv_more)
        popup.menuInflater.inflate(R.menu.popup, popup.menu)
        popup.setOnMenuItemClickListener {
            if (it.itemId == R.id.menu_search) {
                startActivity(Intent(MainActivity@ this, SearchActivity::class.java))
            } else {
                startActivity(Intent(MainActivity@ this, SelectCategory::class.java))
            }
            return@setOnMenuItemClickListener true
        }
        popup.show()
    }


    private fun setUpTab() {
        DatabaseOperation().getSelectedCategory(this)
        pagerData = mutableListOf()
        newsPagerAdapter = NewsPagerAdapter(supportFragmentManager)
        newsPagerAdapter.initializeList(pagerData)
        slideContent.adapter = newsPagerAdapter
        tabLayout.setupWithViewPager(slideContent)
        newsPagerAdapter.notifyDataSetChanged()
    }


    override fun showMessage(message: String) {
    }

    override fun categoryData(categoryModel: List<CategoryModel>) {
        if (isVisibleToUser && !categoryModel.isEmpty()) {
            categoryModel[0].data?.let {
                it
                        .asSequence()
                        .map { NewsTabModel(it.title, NewsFragmentItem.Instance.getInstance(it)) }
                        .forEach { pagerData.add(it) }
                if (isVisibleToUser) newsPagerAdapter.notifyDataSetChanged()
            }
        }
    }
}