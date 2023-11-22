package com.gaffaryucel.newsgo.view.login

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.gaffaryucel.newsgo.MainActivity
import com.gaffaryucel.newsgo.databinding.FragmentWelcomeBinding
import com.gaffaryucel.newsgo.model.SavedNewsModel
import com.gaffaryucel.newsgo.viewmodel.login.WelcomeViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WelcomeFragment : Fragment() {

    private lateinit var binding : FragmentWelcomeBinding

    private lateinit var viewModel: WelcomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(WelcomeViewModel::class.java)
        binding = FragmentWelcomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginButton.setOnClickListener {
            val action = WelcomeFragmentDirections.actionWelcomeFragmentToLoginFragment()
            Navigation.findNavController(it).navigate(action)
        }

        binding.registerButton.setOnClickListener {
            val action = WelcomeFragmentDirections.actionWelcomeFragmentToRegisterFragment()
            Navigation.findNavController(it).navigate(action)
        }

        observeLiveData()
    }
    private fun observeLiveData(){
        viewModel.currentUser.observe(viewLifecycleOwner, Observer {
            if (it != null){
                val intent = Intent(requireContext(),MainActivity::class.java)
                requireActivity().finish()
                startActivity(intent)
            }
        })

    }
}