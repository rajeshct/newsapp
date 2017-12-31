package com.startup.news.application.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.util.SparseArray
import android.view.ViewGroup

/**
 * Created by rajesh on 3/9/16.
 */
abstract class SmartFragmentStatePagerAdapter(fragmentManager: FragmentManager)
    : FragmentStatePagerAdapter(fragmentManager) {

    // Sparse array to keep track of registered fragments in memory
    private val registeredFragments = SparseArray<Fragment>()

    // Register the fragment when the item is instantiated
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        registeredFragments.put(position, fragment)
        return fragment
    }

    // Unregister when the item is inactive
    override fun destroyItem(container: ViewGroup, position: Int, tempObject: Any) {
        registeredFragments.remove(position)
        super.destroyItem(container, position, tempObject)
    }

    // Returns the fragment for the position (if instantiated)
    fun getRegisteredFragment(position: Int): Fragment {
        return registeredFragments.get(position)
    }

}
