package com.startup.news.application.dialog

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatDialogFragment
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.startup.news.application.R
import com.startup.news.application.customview.revealanimation.animation.CompatViewAnimationUtils
import kotlinx.android.synthetic.main.dialog_more.*


/**
 * Created by admin on 1/6/2018.
 */
class MoreDialog : AppCompatDialogFragment() {

    var xPosition = 0f
    var yPosition = 0f
    var width = 0
    var height = 0
    private var iDialogCallback: IMoreDialogCallbacks? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            iDialogCallback = context as IMoreDialogCallbacks
        } catch (exp: Exception) {
        }
    }

    override fun onDetach() {
        iDialogCallback = null
        super.onDetach()
    }


    override fun onStart() {
        super.onStart()
        dialog?.let {
            val window = dialog.window
            dialog.setOnKeyListener(DialogInterface.OnKeyListener { _, p1, _ ->
                if (p1 == KeyEvent.KEYCODE_BACK) {
                    revealShow(false)
                    return@OnKeyListener true
                }
                false
            })
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }


    object Instance {
        fun getInstance(xPosition: Float, yPosition: Float, width: Int, hegiht: Int): MoreDialog {
            val moreDialog = MoreDialog()
            moreDialog.xPosition = xPosition
            moreDialog.yPosition = yPosition
            moreDialog.width = width
            moreDialog.height = hegiht
            return moreDialog
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_more, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animationLayout.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(p0: View?, p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int, p7: Int, p8: Int) {
                p0?.removeOnLayoutChangeListener(this)
                revealShow(true)
            }
        })

        text_category.setOnClickListener {
            iDialogCallback?.openCategory()
        }

        text_rate.setOnClickListener {
            iDialogCallback?.openRate()
        }

        text_search.setOnClickListener {
            iDialogCallback?.openSearch()
        }

        text_share.setOnClickListener {
            iDialogCallback?.openShare()
        }
    }

    fun revealShow(isShowAnimation: Boolean) {
        if (ViewCompat.isAttachedToWindow(animationLayout)) {

            val w = animationLayout.width
            val h = animationLayout.height

            val endRadius = Math.hypot(w.toDouble(), h.toDouble()).toInt()

            val cx = ((xPosition + width / 2)).toInt()

            val cy = (yPosition + height + 56).toInt()

            if (isShowAnimation) {
                val revealAnimator = CompatViewAnimationUtils.createCircularReveal(animationLayout, cx, animationLayout.bottom, 0f, endRadius.toFloat())
                animationLayout.visibility = View.VISIBLE
                revealAnimator.duration = 700
                revealAnimator.start()
            } else {
                val revealAnimator = CompatViewAnimationUtils.createCircularReveal(animationLayout, cx, cy, endRadius.toFloat(), 0f)
                revealAnimator.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        animationLayout.visibility = View.INVISIBLE
                        dismiss()
                    }
                })
                revealAnimator.duration = 700
                revealAnimator.start()
            }
        }
    }


    interface IMoreDialogCallbacks {
        fun openCategory()
        fun openSearch()
        fun openRate()
        fun openShare()
    }
}