package com.lukman.githubuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lukman.githubuser.Adapter.FavoriteListAdapter
import com.lukman.githubuser.Helper.OnItemGetClickced
import com.lukman.githubuser.Helper.ViewModelFactory
import com.lukman.githubuser.ViewModel.FavoriteViewModel
import com.lukman.githubuser.data.local.Entity.UserFavorite
import com.lukman.githubuser.databinding.ActivityFavoritesBinding

class FavoritesActivity : AppCompatActivity() {
    private var _binding:ActivityFavoritesBinding?=null
    private val binding get() = _binding
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var adapter:FavoriteListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.listUserFavoriteActionBar)
        favoriteViewModel = obtainViewModel(this)
        val listFavoritesObserver = Observer<List<UserFavorite>>{ listUserFavorite ->
            if(listUserFavorite!=null){
                adapter.setListFavUser(listUserFavorite)
            }
        }
        favoriteViewModel.getUserFavorites().observe(this, listFavoritesObserver)

        adapter = FavoriteListAdapter(object : OnItemGetClickced {
            override fun itemGetClicked(userFavoriteUsername: String) {
                toDetailActivity(userFavoriteUsername)
            }
        })
        binding?.apply {
            rvUsersFavorite.adapter = adapter
            rvUsersFavorite.layoutManager = LinearLayoutManager(this@FavoritesActivity)
            rvUsersFavorite.setHasFixedSize(true)
        }


    }

    private fun obtainViewModel(activity: AppCompatActivity):FavoriteViewModel{
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun toDetailActivity(username: String) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_USER, username)
        startActivity(intent)
    }
}