package com.startup.news.application

import android.content.ActivityNotFoundException
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ShareCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDialogFragment
import android.view.View
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.startup.news.application.activities.SearchActivity
import com.startup.news.application.adapter.NewsPagerAdapter
import com.startup.news.application.constants.AppConstants
import com.startup.news.application.dialog.MoreDialog
import com.startup.news.application.fragments.NewsFragmentItem
import com.startup.news.application.fragments.RegionalNewsFragment
import com.startup.news.application.interfaces.databasecallback.IDatabaseSuccessFailureCallback
import com.startup.news.application.interfaces.viewcallback.INewsSource
import com.startup.news.application.localdatabase.databaseoperation.CategoryDatabaseOperation
import com.startup.news.application.model.apimodel.NewsSourceResponse
import com.startup.news.application.model.viewmodel.NewsTabModel
import com.startup.news.application.mvp.presenter.NewsPresenter
import com.startup.news.application.prefrences.SharedPrefrenceData
import com.startup.news.application.userlocation.FetchUserLocation
import com.startup.news.application.utils.Utils
import com.startup.news.fragments.SelectCategory
import com.startup.news.localdatabase.tables.CategoryModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), IDatabaseSuccessFailureCallback, FetchUserLocation.LocationCallbackToClass,
        INewsSource, MoreDialog.IMoreDialogCallbacks {


    private lateinit var newsPagerAdapter: NewsPagerAdapter
    private lateinit var pagerData: MutableList<NewsTabModel>
    private var delay = 0L
    private var userAddress: FetchUserLocation? = null
    private var moreDialogFragment: AppCompatDialogFragment? = null

    override fun onPause() {
        adView.pause()
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
        adView.resume()
        userAddress?.onResume()
    }


    override fun onDestroy() {
        adView.destroy()
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MobileAds.initialize(this, getString(R.string.adUnitId))
        if (SharedPrefrenceData().getUserCountry().isBlank()) {
            fetchUserAddress(savedInstanceState)
        }
        setUpTab()
        loadBannerAd()
        iv_more.setOnClickListener {
            showPopupMenu()
        }
    }

    private fun fetchUserAddress(savedInstanceState: Bundle?) {
        val userAddress = FetchUserLocation(this, this, true)
        userAddress.beginFetchUserLocation(savedInstanceState)
        savedInstanceState?.let {
            userAddress.onSaveInstanceState(it)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        userAddress?.onActivityResult(requestCode, resultCode)
    }

    private fun loadBannerAd() {
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                adView.visibility = View.VISIBLE
            }

            override fun onAdFailedToLoad(p0: Int) {
                super.onAdFailedToLoad(p0)
                adView.visibility = View.GONE
            }
        }
    }


    private fun showPopupMenu() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val oldFragment = supportFragmentManager.findFragmentByTag(AppConstants.DIALOG_TAG)
        if (oldFragment != null) {
            fragmentTransaction.remove(oldFragment)
        }
        fragmentTransaction.addToBackStack(null)
        moreDialogFragment = MoreDialog.Instance.getInstance(iv_more.x, iv_more.y, iv_more.width, iv_more.height)
        moreDialogFragment?.show(fragmentTransaction, AppConstants.DIALOG_TAG)

    }


    private fun setUpTab() {
        CategoryDatabaseOperation().getSelectedCategory(this)
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
        if (!categoryModel.isEmpty()) {
            categoryModel[0].data?.let {
                if (!SharedPrefrenceData().getUserCountry().isBlank()) {
                    pagerData.add(NewsTabModel(getString(R.string.regional), RegionalNewsFragment.RegionalInstance.getInstance()))
                }
                it.asSequence()
                        .map { NewsTabModel(it.title, NewsFragmentItem.Instance.getInstance(it)) }
                        .forEach {
                            pagerData.add(it)
                        }
                newsPagerAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun userLocation(mCurrentLocation: Location?, address: String?) {
        if (address != null && !address.isBlank()) {
            SharedPrefrenceData().putUserCountry(address)
            val newsPresenter = NewsPresenter()
            newsPresenter.initializeSourceCallback(this)
            newsPresenter.getSource(address)
        }
    }

    override fun error() {
        Utils.showToast(this, getString(R.string.error_unable_to_fetch_address))
    }

    override fun hasSources(data: MutableList<NewsSourceResponse>) {
        if (!pagerData.isEmpty() && pagerData[0].fragment !is RegionalNewsFragment) {
            val temp = NewsTabModel(getString(R.string.regional), RegionalNewsFragment.RegionalInstance.getInstance(/*data*/))
            pagerData.add(0, temp)
            newsPagerAdapter = NewsPagerAdapter(supportFragmentManager)
            newsPagerAdapter.initializeList(pagerData)
            slideContent.adapter = newsPagerAdapter
        }
    }

    override fun openCategory() {
        startActivity(Intent(MainActivity@ this, SelectCategory::class.java))
    }

    override fun openSearch() {
        startActivity(Intent(MainActivity@ this, SearchActivity::class.java))
    }

    override fun openRate() {
        val uri = Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        else
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)))
        }
    }

    override fun openShare() {
        val shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText("${getString(R.string.app_name)}\n {${AppConstants.SHARE_MESSAGE}}\n${AppConstants.APP_SHARE_LINK}")
                .intent
        startActivity(shareIntent)
    }

}
