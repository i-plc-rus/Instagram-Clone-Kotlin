package ru.iplc.gallery.screens.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import ru.iplc.gallery.R
import ru.iplc.gallery.databinding.ActivityLoginBinding
import ru.iplc.gallery.screens.common.BaseActivity
import ru.iplc.gallery.screens.common.coordinateBtnAndInputs
import ru.iplc.gallery.screens.home.HomeActivity
import ru.iplc.gallery.screens.register.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
//import kotlinx.android.synthetic.main.activity_login.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener

class LoginActivity : BaseActivity(), KeyboardVisibilityEventListener, View.OnClickListener {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        //setContentView(R.layout.activity_login)
        setContentView(binding.root)
        Log.d(TAG, "onCreate")

        KeyboardVisibilityEvent.setEventListener(this, this)
        coordinateBtnAndInputs(binding.loginBtn, binding.emailInput, binding.passwordInput)
        binding.loginBtn.setOnClickListener(this)
        binding.createAccountText.setOnClickListener(this)

        mViewModel = initViewModel()
        mViewModel.goToHomeScreen.observe(this, Observer {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        })
        mViewModel.goToRegisterScreen.observe(this, Observer {
            startActivity(Intent(this, RegisterActivity::class.java))
        })
        mAuth = FirebaseAuth.getInstance()

    }

    override fun onClick(view: View) {
        Log.d(TAG,"click")
        when (view.id) {
            R.id.login_btn ->
                mViewModel.onLoginClick(
                        email = binding.emailInput.text.toString(),
                        password = binding.passwordInput.text.toString()
                )
            R.id.create_account_text -> mViewModel.onRegisterClick()

        }
    }

    override fun onVisibilityChanged(isKeyboardOpen: Boolean) {
        if (isKeyboardOpen) {
            binding.createAccountText.visibility = View.GONE
        } else {
            binding.createAccountText.visibility = View.VISIBLE
        }
    }

    companion object {
        const val TAG = "LoginActivity"
    }
}
