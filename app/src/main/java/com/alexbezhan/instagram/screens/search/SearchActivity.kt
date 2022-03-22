package com.alexbezhan.instagram.screens.search

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.alexbezhan.instagram.R
import com.alexbezhan.instagram.databinding.ActivitySearchBinding
import com.alexbezhan.instagram.screens.common.BaseActivity
import com.alexbezhan.instagram.screens.common.ImagesAdapter
import com.alexbezhan.instagram.screens.common.setupAuthGuard
import com.alexbezhan.instagram.screens.common.setupBottomNavigation
//import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : BaseActivity(), TextWatcher {
    private lateinit var mAdapter: ImagesAdapter
    private lateinit var mViewModel: SearchViewModel
    private var isSearchEntered = false
    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_search)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "onCreate")

        setupAuthGuard {uid ->
            setupBottomNavigation(uid,1)
            mAdapter = ImagesAdapter()
            binding.searchResultsRecycler.layoutManager = GridLayoutManager(this, 3)
            binding.searchResultsRecycler.adapter = mAdapter

            mViewModel = initViewModel()
            mViewModel.posts.observe(this, Observer{it?.let{posts ->
                mAdapter.updateImages(posts.map { it.image })
            }})

            binding.searchInput.addTextChangedListener(this)
            mViewModel.setSearchText("")
        }
    }

    override fun afterTextChanged(s: Editable?) {
        if (!isSearchEntered) {
            isSearchEntered = true
            Handler().postDelayed({
                isSearchEntered = false
                mViewModel.setSearchText(binding.searchInput.text.toString())
            }, 500)
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

    companion object {
        const val TAG = "SearchActivity"
    }
}
