package com.startup.news.application.preaparedata

import com.startup.news.application.constants.AppConstants
import com.startup.news.application.model.apimodel.NewsApiResponse
import com.startup.news.application.mvp.interactor.NewsInteractor
import com.startup.news.application.network.GenericResponseModel

/**
 * Created by admin on 12/27/2017.
 */
object SuccessResponseWithData {

    fun sendCallback(iErrorSuccessCallback: NewsInteractor.IErrorSuccessCallback?,
                     genericResponseModel: GenericResponseModel<NewsApiResponse>) {
        if (iErrorSuccessCallback != null) {
            if (genericResponseModel.isSuccess == AppConstants.RESULT_OK) {
                if (isValidList(genericResponseModel.results)) {
                    iErrorSuccessCallback.onSuccess(genericResponseModel.results)
                } else {
                    iErrorSuccessCallback.onError(AppConstants.NO_DATA)
                }
            } else {
                iErrorSuccessCallback.onError(genericResponseModel.message)
            }
        }
    }

    private fun isValidList(objects: Any?): Boolean {
        if (objects == null) return false
        if (objects is List<*>) {
            return !objects.isEmpty() && objects[0] != null
        }
        return false
    }

}