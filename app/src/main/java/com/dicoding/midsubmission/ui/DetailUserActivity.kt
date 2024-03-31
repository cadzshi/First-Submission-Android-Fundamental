package com.dicoding.midsubmission.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.midsubmission.R
import com.dicoding.midsubmission.adapter.SectionsPagerAdapter
import com.dicoding.midsubmission.data.response.DetailUserResponse
import com.dicoding.midsubmission.databinding.ActivityDetailUserBinding
import com.dicoding.midsubmission.viewmodel.DetailUserViewModel
import com.dicoding.midsubmission.viewmodel.DetailUserViewModel.Companion.KEY_USER
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailUserBinding
    private val detailUserViewModel by viewModels<DetailUserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val user = if (Build.VERSION.SDK_INT >= 33) {
            intent.getStringExtra(KEY_USER)
        } else {
            @Suppress("DEPRECATION")
            intent.getStringExtra(KEY_USER)
        }

        if (user != null){
            detailUserViewModel.getDetailUser(user)
        }
        detailUserViewModel.detailUser.observe(this) { detailUser ->
            userData(detailUser)
        }
        detailUserViewModel.showLoading.observe(this) {
            showLoading(it)
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = user.toString()

        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        supportActionBar?.title = user
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
    }

    @SuppressLint("SetTextI18n")
    private fun userData(value: DetailUserResponse?){
        binding.tvUserName.text = value?.login
        binding.tvNickName.text = value?.name
        Glide.with(this@DetailUserActivity)
            .load(value?.avatarUrl)
            .into(binding.civUserPhoto)
        binding.tvFollowing.text = "${value?.following} following"
        binding.tvFollower.text = "${value?.followers} follower"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar3.visibility = View.VISIBLE
        } else {
            binding.progressBar3.visibility = View.GONE
        }
    }

    companion object {
        @StringRes
        val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}