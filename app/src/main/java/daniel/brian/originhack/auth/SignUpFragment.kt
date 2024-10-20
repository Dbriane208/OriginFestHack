package daniel.brian.originhack.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import daniel.brian.originhack.R
import daniel.brian.originhack.activities.HomeActivity
import daniel.brian.originhack.activities.MainActivity
import daniel.brian.originhack.data.User
import daniel.brian.originhack.databinding.FragmentSignUpBinding
import daniel.brian.originhack.utils.RegisterValidation
import daniel.brian.originhack.utils.Resource
import daniel.brian.originhack.viewmodel.IntroductionViewModel
import daniel.brian.originhack.viewmodel.IntroductionViewModel.Companion.SHOPPING_ACTIVITY
import daniel.brian.originhack.viewmodel.IntroductionViewModel.Companion.SPLASH_ACTIVITY
import daniel.brian.originhack.viewmodel.SignUpViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext

@Suppress("DEPRECATION")
@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding

    private val viewModel by viewModels<SignUpViewModel>()
    private val introViewModel by viewModels<IntroductionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // navigate to the login fragment
        binding.toLogin.setOnClickListener{
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }


        lifecycleScope.launchWhenStarted {
            introViewModel.navigate.collectLatest {
                when(it){
                    SHOPPING_ACTIVITY ->{
                        Intent(requireActivity(), HomeActivity::class.java).also { intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }
                    SPLASH_ACTIVITY -> {
                        val intent = Intent(context, MainActivity::class.java)
                        startActivity(intent)
                    }
                    else -> Unit
                }
            }
        }

        binding.apply {
            btnRegister.setOnClickListener {
                introViewModel.startButtonClick()
                val user = User(
                    username.text.toString().trim(),
                    email.text.toString().trim(),
                    phone.text.toString().trim()
                )

                val password = password.text.toString()
                viewModel.createAccountWithEmailAndPassword(user, password)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.register.collect{
                when(it){
                    is Resource.Error -> {
                        Snackbar.make(requireView(), "Similar email exists.Try another email!", Snackbar.LENGTH_LONG).show()
                    }
                    is Resource.Loading ->{
                        Snackbar.make(requireView(),"Loading...",
                            Snackbar.LENGTH_LONG).show()
                    }
                    is Resource.Success ->{
                        Snackbar.make(requireView(),"Registration Successful!", Snackbar.LENGTH_LONG).show()
                        Intent(requireActivity(), HomeActivity::class.java).also { intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.validation.collect{validation ->
                if (validation.email is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.email.apply {
                            requestFocus()
                            error = validation.email.message
                        }
                    }
                }

                if (validation.password is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.password.apply {
                            requestFocus()
                            error = validation.password.message
                        }
                    }
                }
            }
        }
    }
}