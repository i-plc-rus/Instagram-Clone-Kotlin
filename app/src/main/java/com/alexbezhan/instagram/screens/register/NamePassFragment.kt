package com.alexbezhan.instagram.screens.register

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alexbezhan.instagram.R
import com.alexbezhan.instagram.databinding.FragmentRegisterNamepassBinding
import com.alexbezhan.instagram.screens.common.coordinateBtnAndInputs
//import kotlinx.android.synthetic.main.fragment_register_namepass.*

class NamePassFragment : Fragment() {
    private lateinit var mListener: Listener
    private lateinit var binding: FragmentRegisterNamepassBinding
    interface Listener {
        fun onRegister(fullName: String, password: String)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register_namepass, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentRegisterNamepassBinding.bind(view)
        coordinateBtnAndInputs(binding.registerBtn, binding.fullNameInput, binding.passwordInput)
        binding.registerBtn.setOnClickListener {
            val fullName = binding.fullNameInput.text.toString()
            val password = binding.passwordInput.text.toString()
            mListener.onRegister(fullName, password)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as Listener
    }
}