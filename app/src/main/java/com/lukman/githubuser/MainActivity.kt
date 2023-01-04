package com.lukman.githubuser

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.res.ResourcesCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.lukman.githubuser.Adapter.UserOnSearchAdapter
import com.lukman.githubuser.Helper.OnItemGetClickced
import com.lukman.githubuser.Helper.SettingPreference
import com.lukman.githubuser.Helper.ViewModelFactory
import com.lukman.githubuser.SingleEvent.Event
import com.lukman.githubuser.ViewModel.MainViewModel
import com.lukman.githubuser.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Setting")
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel:MainViewModel
    private lateinit var adapter: UserOnSearchAdapter
    private var lastQuery: String = ""
    private lateinit var pref:SettingPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel = obtainViewModel(this@MainActivity)
        pref = SettingPreference.getInstance(dataStore)

        val getThemeSettingObserver = Observer<Boolean>{ isDarkModeActive->
            if(isDarkModeActive){
                val isAlreadyInDarkMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                if(isAlreadyInDarkMode == Configuration.UI_MODE_NIGHT_YES){
                    binding.tvInfo.setTextColor(ResourcesCompat.getColor(resources, R.color.white, null))
                }else if(isAlreadyInDarkMode == Configuration.UI_MODE_NIGHT_NO){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    binding.tvInfo.setTextColor(ResourcesCompat.getColor(resources, R.color.white, null))
                }
            }else{
                val isAlreadyNotInDarkMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                if(isAlreadyNotInDarkMode == Configuration.UI_MODE_NIGHT_NO){
                    binding.tvInfo.setTextColor(ResourcesCompat.getColor(resources, R.color.black, null))
                }else if(isAlreadyNotInDarkMode == Configuration.UI_MODE_NIGHT_YES){
                    binding.tvInfo.setTextColor(ResourcesCompat.getColor(resources, R.color.black, null))
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }


            }
        }
        mainViewModel.getThemeSetting(pref).observe(this, getThemeSettingObserver)


        val listUsersOnSearchObserver = Observer<ArrayList<ItemSearchUser>> {
            if (it != null && MainViewModel.isRvAllowedToDisplay) {
                adapter = UserOnSearchAdapter(it, object : OnItemGetClickced{
                    override fun itemGetClicked(userFavoriteUsername: String) {
                        toDetailActivity(userFavoriteUsername)
                    }
                })
                setUpRecyclerView()
            } else {
                binding.rvUsers.visibility = View.GONE
            }
        }
        mainViewModel.listUsers.observe(this, listUsersOnSearchObserver)

        val errorMessageObserver = Observer<Event<String>> { singleEvent ->
            singleEvent.getContentIfnotHandled().let {
                Snackbar.make(
                    window.decorView.rootView,
                    it.toString(),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        mainViewModel.errorMessage.observe(this, errorMessageObserver)

        val isLoadingObserver = Observer<Boolean> {
            setLoading(it)
        }
        mainViewModel.isLoading.observe(this, isLoadingObserver)

        val queryObserver = Observer<String> {
            lastQuery = it
        }
        mainViewModel.query.observe(this, queryObserver)

    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu?.findItem(R.id.menu_search)
        val searchView = searchItem?.actionView as android.widget.SearchView


        val onActionExpandListener = object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                searchView.setQuery("", false)
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                binding.apply {
                    rvUsers.visibility = View.GONE
                    tvInfo.visibility = View.VISIBLE
                    MainViewModel.isRvAllowedToDisplay = false
                }
                return true
            }
        }

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = getString(R.string.cari_seseorang)
        searchView.onActionViewExpanded()
        searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query!=null){
                    mainViewModel.getListUsersOnSearchBar(query)
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
        searchItem.setOnActionExpandListener(onActionExpandListener)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val searchItem = menu?.findItem(R.id.menu_search)
        val searchView = searchItem?.actionView as android.widget.SearchView
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            if (MainViewModel.isRvAllowedToDisplay && !searchView.isActivated) {
                searchItem.expandActionView()
                searchView.setQuery(lastQuery, false)
            }
        }, 200L)
        val menuDarkTheme = menu.findItem(R.id.menu_gantiTema)

        checkDarkMode(menuDarkTheme)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_favorites -> {
                toFavoriteActivity()
            }
            R.id.menu_gantiTema ->{
                val isChecked = !item.isChecked
                mainViewModel.saveThemeSetting(pref, isChecked)
                item.isChecked = isChecked
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun toDetailActivity(username: String) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_USER, username)
        startActivity(intent)
    }
    private fun setUpRecyclerView() {
        binding.apply {
            tvInfo.visibility = View.GONE
            rvUsers.visibility = View.VISIBLE
            rvUsers.adapter = adapter
            rvUsers.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUsers.setHasFixedSize(true)
        }
    }

    private fun toFavoriteActivity(){
        val intent = Intent(this, FavoritesActivity::class.java)
        startActivity(intent)
    }

    private fun setLoading(bool: Boolean) {
        if (bool) {
            binding.progressBar.visibility = View.VISIBLE
            binding.rvUsers.visibility = View.GONE
            binding.tvInfo.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun checkDarkMode(menuItem: MenuItem){
        val darkModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when(darkModeFlags){
            Configuration.UI_MODE_NIGHT_YES ->{
                menuItem.isChecked = true
                Log.d("TAG", "yes")
            }
            else -> {
                Log.d("TAG", "no")
            }
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): MainViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[MainViewModel::class.java]
    }

}