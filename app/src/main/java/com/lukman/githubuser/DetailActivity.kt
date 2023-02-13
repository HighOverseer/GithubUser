package com.lukman.githubuser


import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.lukman.githubuser.Adapter.ViewPagerAdapter
import com.lukman.githubuser.Helper.SettingPreference
import com.lukman.githubuser.Helper.ViewModelFactory
import com.lukman.githubuser.SingleEvent.Event
import com.lukman.githubuser.ViewModel.DetailViewModel
import com.lukman.githubuser.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private var _binding: ActivityDetailBinding?=null
    private val binding get() = _binding
    private lateinit var detailViewModel:DetailViewModel
    private lateinit var userDetail: UserDetail
    private var mIsFavorite:Boolean = false
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Setting")
    private lateinit var settingPreference: SettingPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        checkDarkMode()
        settingPreference = SettingPreference.getInstance(dataStore)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        detailViewModel = obtainViewModel(this@DetailActivity)

        detailViewModel.getDetailUser(getUsername()!!)

        val userDetailObserver = Observer<UserDetail> {
            setUpLayout(it)
            userDetail = it
            detailViewModel.checkUserFavoriteStatus(it.id)
        }
        detailViewModel.userDetail.observe(this, userDetailObserver)

        val isLoadingObserver = Observer<Boolean> {
            setLoading(it)
        }
        detailViewModel.isLoading.observe(this, isLoadingObserver)

        val errorMessageObserver = Observer<Event<String>> { singleEvent ->
            singleEvent.getContentIfnotHandled().let {
                Snackbar.make(
                    window.decorView.rootView,
                    it.toString(),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        detailViewModel.errorMessage.observe(this, errorMessageObserver)

        val isThisUserFavoriteObserver = Observer<Boolean> { isFavorite ->
            setFbFavorite(isFavorite)
            mIsFavorite = isFavorite
        }
        detailViewModel.isThisUserFavorite.observe(this, isThisUserFavoriteObserver)

        binding?.fbFavorite?.setOnClickListener {
            if(mIsFavorite){
                detailViewModel.setUserFavoriteStatus(false)
            }else{
                detailViewModel.setUserFavoriteStatus(true)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                detailViewModel.addDeleteThisUserFavorite(userDetail, mIsFavorite)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        detailViewModel.addDeleteThisUserFavorite(userDetail, mIsFavorite)
        super.onBackPressed()
    }

    private fun setLoading(bool: Boolean) {
        if (bool) {
            binding?.pbDetail?.visibility = View.VISIBLE
        } else {
            binding?.pbDetail?.visibility = View.GONE
        }
    }

    private fun setUpLayout(userDetail: UserDetail) {
        binding?.apply {
            Glide.with(this@DetailActivity)
                .load(userDetail.avatar_url)
                .into(ivPhotoDetail)
            userDetail.apply {
                tvNamaDetail.text = name
                tvUsernameDetail.text = login
                tvCompanyDetail.text = company
                tvLocationDetail.text = location
            }
            checkOrientation()
            val viewPagerAdapter = ViewPagerAdapter(this@DetailActivity)
            vpDetail.adapter = viewPagerAdapter

            TabLayoutMediator(tbDetail, vpDetail) { tab, position ->
                tab.text = when (position) {
                    0 -> getString(R.string.repositories, userDetail.public_repos)
                    1 -> getString(R.string.following, userDetail.following)
                    2 -> getString(R.string.follower, userDetail.followers)
                    else -> getString(R.string.nulll)
                }
            }.attach()

        }
    }

    private fun checkOrientation(){
        binding?.apply {
            val orientation = resources.configuration.orientation
            if( orientation == Configuration.ORIENTATION_PORTRAIT){
                vpDetail.layoutParams.height = 0
                val params = vpDetail.layoutParams as ConstraintLayout.LayoutParams
                params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                vpDetail.requestLayout()
            }else if(orientation == Configuration.ORIENTATION_LANDSCAPE){
                vpDetail.layoutParams.height = 500
                val params = vpDetail.layoutParams as ConstraintLayout.LayoutParams
                params.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
                vpDetail.requestLayout()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    private fun getUsername() = intent.getStringExtra(EXTRA_USER)

    private fun obtainViewModel(activity: AppCompatActivity):DetailViewModel{
        val factory = ViewModelFactory.getInstance(activity.application, settingPreference)
        return ViewModelProvider(activity, factory)[DetailViewModel::class.java]
    }

    private fun setFbFavorite(isFavorite: Boolean){
        if (isFavorite){
            binding?.fbFavorite?.setImageResource(R.drawable.ic_favorited)
        }else{
            binding?.fbFavorite?.setImageResource(R.drawable.ic_favorite)
        }
    }

    private fun checkDarkMode(){
        val darkModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when(darkModeFlags){
            Configuration.UI_MODE_NIGHT_YES ->{
                binding?.tbDetail?.setSelectedTabIndicatorColor(ResourcesCompat.getColor(resources, R.color.white, null))
            }
        }
    }

    companion object {
        const val EXTRA_USER = "user"
    }
}
