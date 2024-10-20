package daniel.brian.originhack.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import daniel.brian.originhack.R
import daniel.brian.originhack.activities.HomeActivity
import daniel.brian.originhack.databinding.FragmentLoginBinding
import daniel.brian.originhack.utils.Resource
import daniel.brian.originhack.viewmodel.LoginViewModel


@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // navigate to signup fragment
        binding.toSignup.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        // navigate to forgot password fragment
        binding.forgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }


        binding.apply {
            btnLogin.setOnClickListener {
                val email = loginEmail.text.toString().trim()
                val password = loginPassword.text.toString()
                viewModel.login(email, password)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.login.collect {
                when (it) {
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_LONG)
                            .show()
                    }

                    is Resource.Loading -> {
                        Snackbar.make(requireView(),"Loading...",
                            Snackbar.LENGTH_LONG).show()
                    }

                    is Resource.Success -> {
                        Intent(requireActivity(), HomeActivity::class.java).also { intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }

                    else -> Unit
                }
            }
        }
    }
}