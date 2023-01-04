package com.lukman.githubuser.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lukman.githubuser.Fragment.ListFolFragment
import com.lukman.githubuser.UserRepo
import com.lukman.githubuser.data.remote.ModelnResponse.Followering
import com.lukman.githubuser.databinding.ItemLayoutBinding
import de.hdodenhof.circleimageview.CircleImageView

class ListFolAdapter(private val items: ArrayList<*>, private val fragmentPosition: Int) :
    RecyclerView.Adapter<ListFolAdapter.ListFolViewHolder>() {

    inner class ListFolViewHolder(val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListFolViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListFolViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListFolViewHolder, position: Int) {
        val item = items[position]
        when(fragmentPosition){
            ListFolFragment.FRAGMENT_REPO -> {
                if (item is UserRepo) {
                    holder.binding.apply {
                        setLayoutForRepoPosition(ivPhoto, tvUsername, tvGithubUrl, root, position)
                        tvUsername.text = item.name
                        tvGithubUrl.text = item.html_url
                    }
                }
            }
            ListFolFragment.FRAGMENT_FOLLOWING ->{
                if (item is Followering) {
                    holder.binding.apply {
                        ivPhoto.loadImage(item.avatar_url, holder.itemView.context)
                        tvUsername.text = item.login
                        tvGithubUrl.text = item.html_url
                    }
                }
            }
            ListFolFragment.FRAGMENT_FOLLOWER -> {
                if (item is Followering) {
                    holder.binding.apply {
                        ivPhoto.loadImage(item.avatar_url, holder.itemView.context)
                        tvUsername.text = item.login
                        tvGithubUrl.text = item.html_url
                    }
                }
            }

        }

    }

    private fun CircleImageView.loadImage(url:String, context: Context){
        Glide.with(context)
            .load(url)
            .into(this)
    }

    private fun setLayoutForRepoPosition(iv:CircleImageView, tv1: TextView, tv2:TextView,
                                         root:ConstraintLayout, itemPosition:Int){
        iv.visibility = View.GONE
        var params = tv1.layoutParams as LayoutParams
        params.marginStart = 0
        tv1.requestLayout()
        tv1.textSize = 18f
        params = tv2.layoutParams as LayoutParams
        params.marginStart = 0
        params.bottomMargin = 8
        params.topMargin = 0
        tv2.requestLayout()
        if(itemPosition == items.size-1){
            root.setPadding(16, 8, 16, 18)
        }else{
            root.setPadding(16, 8, 16, 2)
        }

    }

    override fun getItemCount() = items.size
}