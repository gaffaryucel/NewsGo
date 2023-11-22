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
import com.gaffaryucel.newsgo.databinding.FragmentLoginBinding
import com.gaffaryucel.newsgo.databinding.FragmentRegisterBinding
import com.gaffaryucel.newsgo.viewmodel.login.LoginViewModel
import com.gaffaryucel.newsgo.viewmodel.login.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var viewModel: RegisterViewModel
    private lateinit var binding : FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        binding = FragmentRegisterBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            val username = binding.editTextUsername.text.toString()
            val email = binding.editTextEmailRegister.text.toString()
            val password = binding.editTextPasswordRegister.text.toString()
            val confirmPassword = binding.editTextConfirmPassword.text.toString()
            if (isPasswordValid(password,confirmPassword)){
                viewModel.signUp(username,email,password)
            }
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
    fun isPasswordValid(password: String,confirmPassword : String): Boolean {
        return password == confirmPassword
    }
}