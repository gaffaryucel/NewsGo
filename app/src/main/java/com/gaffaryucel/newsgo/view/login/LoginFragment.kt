package com.gaffaryucel.newsgo.view.login

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.gaffaryucel.newsgo.MainActivity
import com.gaffaryucel.newsgo.R
import com.gaffaryucel.newsgo.databinding.FragmentHomeBinding
import com.gaffaryucel.newsgo.databinding.FragmentLoginBinding
import com.gaffaryucel.newsgo.viewmodel.HomeViewModel
import com.gaffaryucel.newsgo.viewmodel.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding : FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            viewModel.signIn(email,password)
        }
        observeLiveData()
    }
    private fun observeLiveData(){
        viewModel.authState.observe(viewLifecycleOwner, Observer {
            if (it){
                val intent = Intent(requireContext(), MainActivity::class.java)
                requireActivity().finish()
                startActivity(intent)
            }
        })

    }
}