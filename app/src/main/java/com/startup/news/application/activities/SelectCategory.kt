package com.startup.news.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.widget.Toast
import com.startup.news.adapter.SelectCategoryAdapter
import com.startup.news.application.MainActivity
import com.startup.news.application.R
import com.startup.news.application.firebaseoperations.FireBaseGetData
import com.startup.news.application.interfaces.viewcallback.ICategoryCallback
import com.startup.news.application.model.apimodel.CategoryFirebaseResponse
import com.startup.news.application.preaparedata.GetCategorySelection
import kotlinx.android.synthetic.main.activity_select_contents.*
import kotlinx.android.synthetic.main.include_recycler_view.*

/**
 * Created by admin on 11/17/2017.
 */

class SelectCategory : AppCompatActivity(), ICategoryCallback, SelectCategoryAdapter.ICategoryCallback {
    private var categorySelection = 0
    private lateinit var categoryData: MutableList<CategoryFirebaseResponse>
    private var isVisibleToUser = false
    private lateinit var selectCategoryAdapter: SelectCategoryAdapter

    override fun onPause() {
        isVisibleToUser = false
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        isVisibleToUser = true
    }

    override fun isCategorySelected(isSelected: Boolean) {
        if (isSelected)
            categorySelection++
        else
            categorySelection--
        btContinue.visibility = if (categorySelection > 0) View.VISIBLE else View.GONE
    }

    override fun categories(tempData: List<CategoryFirebaseResponse>) {
        if (isVisibleToUser) {
            this.categoryData.addAll(tempData)
            this.selectCategoryAdapter.notifyDataSetChanged()
        }
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
        if (isVisibleToUser)
            noData.visibility = View.VISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_contents)
        initialAction()
    }

    private fun initialAction() {
        categoryData = mutableListOf()
        selectCategoryAdapter = SelectCategoryAdapter(categoryData, this)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = selectCategoryAdapter

        FireBaseGetData.instance.getDataFromFireBase(this)
        btContinue.setOnClickListener {
            if (categorySelection > 0) {
                GetCategorySelection().categorySelection(data = categoryData)
                val intent = Intent(SelectCategory@ this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
    }

}
