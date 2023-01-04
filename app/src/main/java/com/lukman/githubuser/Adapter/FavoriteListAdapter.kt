package com.lukman.githubuser.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lukman.githubuser.Helper.OnItemGetClickced
import com.lukman.githubuser.Helper.UserFavDiffCallback
import com.lukman.githubuser.data.local.Entity.UserFavorite
import com.lukman.githubuser.databinding.ItemLayoutBinding

class FavoriteListAdapter(private val onItemGetClicked: OnItemGetClickced):RecyclerView.Adapter<FavoriteListAdapter.FavoriteListViewHolder>() {
    private val listFavUser = ArrayList<UserFavorite>()

    fun setListFavUser(listFavUser:List<UserFavorite>){
        val diffCallback = UserFavDiffCallback(this.listFavUser, listFavUser)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listFavUser.clear()
        this.listFavUser.addAll(listFavUser)
        diffResult.dispatchUpdatesTo(this)
    }

    class FavoriteListViewHolder(val binding:ItemLayoutBinding, clickAtPosition:(Int)->Unit):RecyclerView.ViewHolder(binding.root){
        init {
            itemView.setOnClickListener{
                clickAtPosition(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteListViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteListViewHolder(binding){ itemAdapterPosition ->
            onItemGetClicked.itemGetClicked(listFavUser[itemAdapterPosition].login)
        }
    }

    override fun onBindViewHolder(holder: FavoriteListViewHolder, position: Int) {
        val currenUserFav = listFavUser[position]
        holder.binding.apply {
            Glide.with(holder.itemView.context)
                .load(currenUserFav.avatar_url)
                .into(ivPhoto)
            tvUsername.text = currenUserFav.login
            tvGithubUrl.text = currenUserFav.html_url
        }

    }

    override fun getItemCount(): Int = listFavUser.size


}