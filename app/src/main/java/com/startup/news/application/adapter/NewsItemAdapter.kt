package com.startup.news.application.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.startup.news.application.R
import com.startup.news.application.model.apimodel.NewsApiResponse
import com.startup.news.application.utils.GlideImageRequest
import kotlinx.android.synthetic.main.row_news_item.view.*

/**
 * Created by admin on 12/27/2017.
 */
class NewsItemAdapter(private val newsData: MutableList<NewsApiResponse>) : RecyclerView.Adapter<NewsItemAdapter.NewsViewHolder>() {

    var inewsCallback: RecyclerViewINewsCallback? = null

    fun initializeCallback(iNewsCallback: RecyclerViewINewsCallback) {
        this.inewsCallback = iNewsCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): NewsViewHolder {
        return NewsViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.row_news_item, parent, false))
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        with(newsData[position])
        {
            holder.newsImage.setImage(urlToImage)
            holder.newsTitle.text = title
            holder.newsDescription.text = description
            publishedAt?.let {
                if (it.contains("T"))
                    holder.newsDate.text = it.substring(0, it.indexOf("T"))
                else
                    holder.newsDate.text = it
            }
            if (author != null)
                holder.newsSource.text = holder.itemView.context.getString(R.string.source, author)
        }
    }

    override fun getItemCount(): Int = newsData.size

    inner class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val newsImage: ImageView = view.imageNews
        val newsTitle: TextView = view.text_title
        val newsDescription: TextView = view.text_news
        val newsDate: TextView = view.text_date
        val newsSource: TextView = view.text_source
        private val newsUrl: TextView = view.text_url

        init {
            newsUrl.setOnClickListener {
                val position = adapterPosition
                if (position > -1) {
                    inewsCallback?.openUrl(newsData[position].url)
                }
            }
        }
    }

    private fun ImageView.setImage(url: String?) {
        if (url != null)
            Glide.with(this).load(url).apply(GlideImageRequest.getRequest()).into(this)
    }

    interface RecyclerViewINewsCallback {
        fun openUrl(url: String?)
    }
}