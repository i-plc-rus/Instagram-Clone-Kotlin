package ru.iplc.gallery.screens.common

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import ru.iplc.gallery.R
import ru.iplc.gallery.databinding.DialogPasswordBinding

//import kotlinx.android.synthetic.main.dialog_password.view.*

class PasswordDialog : DialogFragment() {
    private lateinit var mListener: Listener
    private lateinit var binding: DialogPasswordBinding
    interface Listener {
        fun onPasswordConfirm(password: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as Listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity!!.layoutInflater.inflate(R.layout.dialog_password, null)
        binding = DialogPasswordBinding.bind(view)
        return AlertDialog.Builder(context!!)
                .setView(view)
                .setPositiveButton(android.R.string.ok, {_, _ ->
                    mListener.onPasswordConfirm(binding.passwordInput.text.toString())
                })
                .setNegativeButton(android.R.string.cancel, {_, _ ->
                    // do nothing
                })
                .setTitle(R.string.please_enter_password)
                .create()
    }
}