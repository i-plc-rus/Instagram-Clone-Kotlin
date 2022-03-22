package ru.iplc.gallery.screens.register

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.iplc.gallery.R
import ru.iplc.gallery.databinding.FragmentRegisterEmailBinding
import ru.iplc.gallery.screens.common.coordinateBtnAndInputs
//import kotlinx.android.synthetic.main.fragment_register_email.*

class EmailFragment : Fragment() {
    private lateinit var mListener: Listener
    private lateinit var binding: FragmentRegisterEmailBinding
    interface Listener {
        fun onNext(email: String)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register_email, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentRegisterEmailBinding.bind(view)
        coordinateBtnAndInputs(binding.nextBtn, binding.emailInput)

        binding.nextBtn.setOnClickListener {
            val email = binding.emailInput.text.toString()
            mListener.onNext(email)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as Listener
    }
}