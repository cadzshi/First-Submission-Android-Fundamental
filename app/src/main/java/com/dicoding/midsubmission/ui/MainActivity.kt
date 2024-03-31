package com.dicoding.midsubmission.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.midsubmission.adapter.UserAdapter
import com.dicoding.midsubmission.data.response.ItemsItem
import com.dicoding.midsubmission.databinding.ActivityMainBinding
import com.dicoding.midsubmission.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { textView, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val query = textView.text.toString().trim()
                    if (query.isNotEmpty()) {
                        mainViewModel.searchGithubUser(query)
                        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(textView.windowToken, 0)
                        searchView.hide()
                    }
                    true
                } else {
                    false
                }
            }
        }

        mainViewModel.githubUser.observe(this) { githubUser ->
            setUserData(githubUser)
        }
        mainViewModel.searchGithubUser.observe(this) { searchGithubUser ->
            setUserData(searchGithubUser)
        }
        mainViewModel.showLoading.observe(this) {
            showLoading(it)
        }

    }

    private fun setUserData(userData: List<ItemsItem>) {
        val adapter = UserAdapter()
        adapter.submitList(userData)
        binding.rvUser.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}