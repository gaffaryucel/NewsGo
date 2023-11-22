package com.gaffaryucel.newsgo.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.gaffaryucel.newsgo.R
import com.gaffaryucel.newsgo.adapter.NewsAdapter
import com.gaffaryucel.newsgo.databinding.FragmentHomeBinding
import com.gaffaryucel.newsgo.model.Article
import com.gaffaryucel.newsgo.model.SavedNewsModel
import com.gaffaryucel.newsgo.viewmodel.HomeViewModel
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment @Inject constructor(
    val glide: RequestManager
) : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding : FragmentHomeBinding
    private val adapter by lazy { NewsAdapter(glide) }

    private var selectedCategory = ""
    private var selectedTab : TabLayout.Tab? = null

    private val breakingNews =   ArrayList<Article>()
    private val sportsNews =     ArrayList<Article>()
    private val technologyNews = ArrayList<Article>()
    private val educationNews =  ArrayList<Article>()
    private val healthNews =     ArrayList<Article>()
    private val scienceNews =    ArrayList<Article>()
    private var searchResult =   ArrayList<Article>()

    private var queryText = ""
    private var fromDateText = ""
    private var sortByText = ""
    private var sourceText = ""

    private var searchJob: Job? = null

    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupRecyclerView()
        observeLiveData()
    }
    private fun setupView(){
        observeSavedList()
        binding.personIcon.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeFragment_to_profileFragment
            )
        }
        binding.searchIcon.setOnClickListener {
            binding.searchView.visibility = View.VISIBLE
            binding.searchIcon.visibility = View.INVISIBLE
            binding.tabLayout.visibility = View.GONE
        }

        binding.dismissIv.setOnClickListener {
            binding.tabLayout.visibility = View.VISIBLE
            binding.searchIcon.visibility = View.VISIBLE
            binding.searchView.visibility = View.INVISIBLE
            binding.searchView.setText("")
        }
        val categories = listOf("Breaking News","Sport", "Technology","Education", "Health", "Science")

        categories.forEach { category ->
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(category))
        }

        // TabLayout'da seçilen kategori değiştikçe RecyclerView içeriğini güncelleyin
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    setRecyclerViewData(categories[it.position])
                    selectedTab= it
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val searchText = s.toString()
                if (searchText.isNotEmpty()){
                    searchJob?.cancel()
                    searchJob = lifecycleScope.launch {
                        delay(1000)
                        viewModel.searchNews(searchText)
                        observeSearchResult()
                    }
                }
            }
        })

        adapter.onClick = {news->
            val action = HomeFragmentDirections.actionHomeFragmentToNewsDetailsFragment(
                author = news.author ?: "unknown",
                content = news.content ?: "unknown",
                description = news.description ?: "unknown",
                publishedAt = news.publishedAt ?: "unknown",
                source = news.source.name ?: "unknown",
                title = news.title ?: "unknown",
                url = news.url ?: "unknown",
                urlToImage = news.urlToImage ?: "unknown"
            )
            findNavController().navigate(action)
        }
        adapter.onClickSave = { news ->
            val newsToSave = viewModel.convertArticleToSavedNewsModel(news)
            viewModel.saveNews(newsToSave)
        }
        adapter.onClickDelete = { news ->
            val newsToSave = viewModel.convertArticleToSavedNewsModel(news)
            viewModel.deleteNews(newsToSave)
        }
        binding.filtersIv.setOnClickListener{
            showPopup()
        }
        binding.fab.setOnClickListener {
            binding.newsRecyclerView.smoothScrollToPosition(0)
        }
    }

    private fun setRecyclerViewData(category: String) {
        selectedCategory = category
        when (category) {
            "Breaking News" -> {
                if (breakingNews != null){
                    adapter.newsList = breakingNews
                }
            }
            "Sport" -> {
                if (sportsNews != null){
                    adapter.newsList = sportsNews
                }
            }
            "Technology" -> {
                if (technologyNews != null){
                    adapter.newsList = technologyNews
                }
            }
            "Education" -> {
                if (educationNews != null){
                    adapter.newsList = educationNews
                }
            }
            "Health" -> {
                if (healthNews != null){
                    adapter.newsList = healthNews
                }
            }
            "Science" -> {
                if (scienceNews != null){
                    adapter.newsList = scienceNews
                }
            }
            else -> {

            }
        }
        viewModel.getNews(category)
    }

    private fun setupRecyclerView(){
        binding.newsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.newsRecyclerView.adapter = adapter
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun observeLiveData(){
        viewModel.breakingNews.observe(viewLifecycleOwner, Observer {
            lifecycleScope.launch {
                for (i in it.articles){
                    if (i.urlToImage != null){
                        breakingNews.add(i)
                    }
                }
                adapter.newsList = breakingNews
            }
        })
        viewModel.sportNews.observe(viewLifecycleOwner, Observer {
            lifecycleScope.launch {
                 for (i in it.articles){
                    if (i.urlToImage != null){
                        sportsNews.add(i)
                    }
                }
                adapter.newsList = sportsNews
                adapter.notifyDataSetChanged()
            }
        })
        viewModel.technologyNews.observe(viewLifecycleOwner, Observer {
            lifecycleScope.launch {
                for (i in it.articles){
                    if (i.urlToImage != null){
                        technologyNews.add(i)
                    }
                }
                adapter.newsList = technologyNews
                adapter.notifyDataSetChanged()
            }
        })
        viewModel.educationNews.observe(viewLifecycleOwner, Observer {
            lifecycleScope.launch {
                for (i in it.articles){
                    if (i.urlToImage != null){
                        educationNews.add(i)
                    }
                }
                adapter.newsList = educationNews
                adapter.notifyDataSetChanged()
            }
        })
        viewModel.healthNews.observe(viewLifecycleOwner, Observer {
            lifecycleScope.launch {
                for (i in it.articles){
                    if (i.urlToImage != null){
                        healthNews.add(i)
                    }
                }
                adapter.newsList = healthNews
                adapter.notifyDataSetChanged()
            }
        })
        viewModel.scienceNews.observe(viewLifecycleOwner, Observer {
            lifecycleScope.launch {
                for (i in it.articles){
                    if (i.urlToImage != null){
                        scienceNews.add(i)
                    }
                }
                adapter.newsList = scienceNews
                adapter.notifyItemInserted(0)
            }
        })
    }
    private fun observeSavedList(){
        viewModel.savedNews.observe(viewLifecycleOwner, Observer {
            lifecycleScope.launch {
                adapter.savedNewsTitleList = it as ArrayList<SavedNewsModel>
            }
        })
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun observeSearchResult(){
        viewModel.searchResult.observe(viewLifecycleOwner, Observer {
            searchResult = ArrayList<Article>()
            lifecycleScope.launch {
                for (i in it.articles){
                    if (i.urlToImage != null){
                        searchResult.add(i)
                    }
                }
                adapter.newsList = searchResult
                adapter.notifyDataSetChanged()
            }
        })
    }

    @SuppressLint("MissingInflatedId")
    private fun showPopup(){
        val inflater = LayoutInflater.from(requireContext())
        val popupView = inflater.inflate(R.layout.popup_menu, null)
        val popupWindow = PopupWindow(
            popupView,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.popup_anim)
        popupWindow.animationStyle = android.R.style.Animation_Dialog
        popupWindow.contentView.startAnimation(animation)

        popupWindow.showAtLocation(
            requireActivity().findViewById(R.id.fragmentContainerView),
            Gravity.TOP or Gravity.CENTER_HORIZONTAL,
            0,
            0
        )
        val query_et = popupView.findViewById<EditText>(R.id.query_et)
        val from_date_button = popupView.findViewById<Button>(R.id.from_date_button)
        val from_date_text_view = popupView.findViewById<TextView>(R.id.from_date_text_view)
        val sort_by_spinner = popupView.findViewById<Spinner>(R.id.sort_by_spinner)
        val source_spinner = popupView.findViewById<Spinner>(R.id.source_spinner)
        val confirm_button = popupView.findViewById<Button>(R.id.confirm_button)

        confirm_button.setOnClickListener {
            queryText = query_et.text.toString()
            fromDateText = from_date_text_view.text.toString()
            sort_by_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                    sortByText = sort_by_spinner.selectedItem.toString()

                    // selectedSortByText, seçilen öğenin metin değerini içerir
                    // Bu değeri istediğiniz gibi kullanabilirsiniz
                }

                override fun onNothingSelected(parentView: AdapterView<*>) {
                    // Bir şey seçilmediğinde yapılacak işlemler
                }
            }
            source_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                    sourceText = sort_by_spinner.selectedItem.toString()
                }
                override fun onNothingSelected(parentView: AdapterView<*>) {
                }
            }
            popupWindow.dismiss()
            viewModel.filter(queryText,fromDateText,sortByText,sourceText)
            observeSearchResult()
        }
        from_date_button.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Tarih seçildiğinde yapılacak işlemler
                    calendar.set(selectedYear, selectedMonth, selectedDay)
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val formattedDate = dateFormat.format(calendar.time)
                    from_date_text_view.text = formattedDate
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.tabLayout.selectTab(selectedTab)
        observeSavedList()
    }
}