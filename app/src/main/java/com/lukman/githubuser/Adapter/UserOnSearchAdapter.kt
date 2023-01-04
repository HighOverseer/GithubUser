package com.lukman.githubuser.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lukman.githubuser.Helper.OnItemGetClickced
import com.lukman.githubuser.ItemSearchUser
import com.lukman.githubuser.databinding.ItemLayoutBinding
import de.hdodenhof.circleimageview.CircleImageView

class UserOnSearchAdapter(private val listUsers: ArrayList<ItemSearchUser>, private val onItemGetClicked:OnItemGetClickced) :
    RecyclerView.Adapter<UserOnSearchAdapter.UserViewHolder>() {
    inner class UserViewHolder(val binding: ItemLayoutBinding, private val clickedAtPosition:(Int)->Unit) :
        RecyclerView.ViewHolder(binding.root){
            init {
                itemView.setOnClickListener{
                    clickedAtPosition(adapterPosition)
                }
            }
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding){ itemAdapterPosition ->
            onItemGetClicked.itemGetClicked(listUsers[itemAdapterPosition].login)
        }
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = listUsers[position]
        holder.binding.apply {
            ivPhoto.loadImage(user.avatar_url, holder.itemView.context)
            tvUsername.text = user.login
            tvGithubUrl.text = user.html_url
        }
    }

    override fun getItemCount() = listUsers.size

    private fun CircleImageView.loadImage(url:String, context:Context){
        Glide.with(context)
            .load(url)
            .into(this)
    }


}