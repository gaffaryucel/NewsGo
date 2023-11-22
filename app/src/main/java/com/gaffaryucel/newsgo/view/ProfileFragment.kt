package com.gaffaryucel.newsgo.view

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.gaffaryucel.newsgo.R
import com.gaffaryucel.newsgo.adapter.NewsAdapter
import com.gaffaryucel.newsgo.adapter.SavedNewsAdapter
import com.gaffaryucel.newsgo.databinding.FragmentHomeBinding
import com.gaffaryucel.newsgo.databinding.FragmentProfileBinding
import com.gaffaryucel.newsgo.model.SavedNewsModel
import com.gaffaryucel.newsgo.viewmodel.HomeViewModel
import com.gaffaryucel.newsgo.viewmodel.ProfileViewModel
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment @Inject constructor(
    val glide: RequestManager
) : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private val adapter by lazy { SavedNewsAdapter(glide) }

    private var isCollapsed = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeLiveData()
        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (verticalOffset == 0) {
                // AppBar tamamen açık durumda
                if (isCollapsed) {
                    // Daha önce collapse durumundaydı ve şimdi açık
                    // Burada istediğiniz fonksiyonu çağırabilirsiniz
                    binding.goBackButton.visibility = View.VISIBLE
                    binding.profilePhotoIcon.visibility = View.INVISIBLE
                    binding.userNameTv.visibility = View.VISIBLE
                    binding.userMailTv.visibility = View.VISIBLE
                    binding.collapsedNameText.visibility = View.INVISIBLE
                    isCollapsed = false
                }
            } else if (Math.abs(verticalOffset) >= appBarLayout.totalScrollRange) {
                // AppBar tamamen kapalı durumda
                if (!isCollapsed) {
                    // Daha önce açık durumundaydı ve şimdi kapalı
                    // Burada istediğiniz fonksiyonu çağırabilirsiniz
                    binding.goBackButton.visibility = View.INVISIBLE
                    binding.profilePhotoIcon.visibility = View.VISIBLE
                    binding.userNameTv.visibility = View.INVISIBLE
                    binding.userMailTv.visibility = View.INVISIBLE
                    binding.collapsedNameText.visibility = View.VISIBLE
                    isCollapsed = true
                }
            }
        })
        binding.goBack.setOnClickListener {
            findNavController().popBackStack()
        }
        adapter.onClickSave = { news ->
            viewModel.saveNews(news)
        }
        adapter.onClickDelete = { news ->
            viewModel.deleteNews(news)
            observeLiveData()
        }
        adapter.onClick = { news->
            val action = ProfileFragmentDirections.actionProfileFragmentToNewsDetailsFragment(
                author = news.author ?: "unknown",
                content = news.content ?: "unknown",
                description = news.description ?: "unknown",
                publishedAt = news.publishedAt ?: "unknown",
                source = news.source ?: "unknown",
                title = news.title ?: "unknown",
                url = news.url ?: "unknown",
                urlToImage = news.urlToImage ?: "unknown"
            )
            findNavController().navigate(action)
        }

    }
    private fun setupRecyclerView(){
        binding.savedNewsRv.layoutManager = LinearLayoutManager(requireContext())
        binding.savedNewsRv.adapter = adapter
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun observeLiveData(){
        viewModel.savedNews.observe(viewLifecycleOwner, Observer {
            adapter.newsList = it
            adapter.notifyDataSetChanged()
        })
        viewModel.userData.observe(viewLifecycleOwner, Observer {userData->
            binding.apply {

            }
        })
    }
}