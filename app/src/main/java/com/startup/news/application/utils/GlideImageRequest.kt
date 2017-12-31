package com.startup.news.application.utils

import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.startup.news.application.R


/**
 * Created by admin on 12/27/2017.
 */
object GlideImageRequest {
    fun getRequest(): RequestOptions {
        return RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .priority(Priority.HIGH)
    }
}