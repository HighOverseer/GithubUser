package com.lukman.githubuser.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.lukman.githubuser.Adapter.ListFolAdapter
import com.lukman.githubuser.UserRepo
import com.lukman.githubuser.ViewModel.DetailViewModel
import com.lukman.githubuser.data.remote.ModelnResponse.Followering
import com.lukman.githubuser.databinding.FragmentTablayoutBinding


class ListFolFragment : Fragment() {


    private var _binding: FragmentTablayoutBinding? = null
    private val binding get() = _binding!!
    private val detailViewModel by activityViewModels<DetailViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTablayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val position = checkFragmentPosition()
        val listRepoFolloweringObserver:Observer<*>

        when (position) {
            FRAGMENT_REPO -> {
                listRepoFolloweringObserver= Observer<ArrayList<UserRepo>>{ listRepo ->
                    setUpLayout(listRepo, FRAGMENT_REPO)
                }
                detailViewModel.repoList.observe(viewLifecycleOwner, listRepoFolloweringObserver)
            }
            FRAGMENT_FOLLOWING -> {
                listRepoFolloweringObserver = androidx.lifecycle.Observer<ArrayList<Followering>> { listFollowering ->
                    setUpLayout(listFollowering, FRAGMENT_FOLLOWING)
                }
                detailViewModel.followingList.observe(viewLifecycleOwner, listRepoFolloweringObserver)
            }
            FRAGMENT_FOLLOWER -> {
                listRepoFolloweringObserver = androidx.lifecycle.Observer<ArrayList<Followering>> { listFollowering ->
                    setUpLayout(listFollowering, FRAGMENT_FOLLOWER)
                }
                detailViewModel.followerList.observe(viewLifecycleOwner, listRepoFolloweringObserver)
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setUpLayout(listFollowering: ArrayList<*>, fragmentPosition:Int) {
        val adapter = ListFolAdapter(listFollowering, fragmentPosition)
        binding.apply {
            pbFragment.visibility = View.GONE
            rvDetail.adapter = adapter
            rvDetail.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun checkFragmentPosition(): Int? {
        return arguments?.getInt(EXTRA_POSITION)
    }
    companion object {
        const val EXTRA_POSITION = "position"

        const val FRAGMENT_REPO = 0
        const val FRAGMENT_FOLLOWING = 1
        const val FRAGMENT_FOLLOWER = 2
    }

}