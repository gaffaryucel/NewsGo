package com.gaffaryucel.newsgo.view

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.gaffaryucel.newsgo.R
import com.gaffaryucel.newsgo.databinding.FragmentHomeBinding
import com.gaffaryucel.newsgo.databinding.FragmentNewsDetailsBinding
import com.gaffaryucel.newsgo.model.Article
import com.gaffaryucel.newsgo.model.Source
import com.gaffaryucel.newsgo.viewmodel.HomeViewModel
import com.gaffaryucel.newsgo.viewmodel.NewsDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NewsDetailsFragment @Inject constructor(
    val glide: RequestManager
) : Fragment() {

    private lateinit var viewModel: NewsDetailsViewModel
    private lateinit var binding : FragmentNewsDetailsBinding

    private val SCROLL_THRESHOLD = 350

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(NewsDetailsViewModel::class.java)
        binding = FragmentNewsDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var isFabVisible = false

        val author: String? = arguments?.getString("author")
        val content: String? = arguments?.getString("content")
        val description: String? = arguments?.getString("description")
        val publishedAt: String? =  arguments?.getString("publishedAt")
        val source: String? = arguments?.getString("source")
        val title: String? = arguments?.getString("title")
        val url: String? = arguments?.getString("url")
        val urlToImage: String? =  arguments?.getString("urlToImage")

        val article = viewModel.createNewsItem(
            author = author,
            content = content,
            description = description,
            publishedAt = publishedAt,
            source = source,
            title = title,
            url = url,
            urlToImage = urlToImage
        )
        binding.apply {
            news = article
        }
        glide.load(urlToImage).into(binding.newsDetailsImageView)
        binding.detailsFab.setOnClickListener {
            println("click")
            binding.nestedScrollView.smoothScrollTo(0, 0)
        }
        setupWebView(article.url)
        binding.nestedScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            // Scroll değişikliğini kontrol et
            if (scrollY >= SCROLL_THRESHOLD && !isFabVisible) {
                // Eğer scroll eşiğine ulaşıldıysa ve fab görünmüyorsa, fab'ı göster
                binding.detailsFab.visibility = View.VISIBLE
                isFabVisible = true
            } else if (scrollY < SCROLL_THRESHOLD && isFabVisible) {
                // Eğer scroll eşiği altına düşüldüyse ve fab görünüyorsa, fab'ı gizle
                binding.detailsFab.visibility = View.INVISIBLE
                isFabVisible = false
            }
        }

    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView(url : String){
        val webSettings = binding.webView.settings
        webSettings.javaScriptEnabled = true
        binding.webView.webViewClient = MyWebViewClient()
        binding.webView.loadUrl(url)
    }
    private inner class MyWebViewClient : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            // WebView yüklenmeye başladığında yapılacak işlemler
            // Örneğin, bir ilerleme çubuğu gösterilebilir.
            binding.newsDetailsProgressBar.visibility = View.VISIBLE
            binding.webView.visibility = View.GONE
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            // WebView yüklemesi tamamlandığında yapılacak işlemler
            // Örneğin, ilerleme çubuğunu gizleme veya diğer işlemler.
            binding.newsDetailsProgressBar.visibility = View.GONE
            binding.webView.visibility = View.VISIBLE
            binding.detailsFab.visibility = View.VISIBLE
        }
    }
}