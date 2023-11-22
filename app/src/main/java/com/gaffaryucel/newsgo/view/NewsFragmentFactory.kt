package com.gaffaryucel.newsgo.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.bumptech.glide.RequestManager
import javax.inject.Inject

class NewsFragmentFactory @Inject constructor (
    val glide : RequestManager
) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className){
            NewsDetailsFragment::class.java.name->NewsDetailsFragment(glide)
            HomeFragment::class.java.name->HomeFragment(glide)
            ProfileFragment::class.java.name->ProfileFragment(glide)
            else-> super.instantiate(classLoader, className)
        }
    }
}