package com.example.voicerecorder.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders.of


import com.example.voicerecorder.R
import com.example.voicerecorder.databinding.FragmentPlayerBinding

class PlayerFragment : DialogFragment() {
    private lateinit var binding: FragmentPlayerBinding
    private lateinit var viewModel: PlayerViewModel
    private var itemPath: String? = null
    companion object {
        private const val ARG_ITEM_PATH = "recording_item_path"
    }
    fun newInstance(itemPath: String?): PlayerFragment{
        val fragment = PlayerFragment()
        val bundle = Bundle()
        bundle.putString(ARG_ITEM_PATH,itemPath)
        fragment.arguments = bundle
        return fragment
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPlayerBinding.bind(R.layout.fragment_player, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        itemPath = arguments?.getString(PlayerFragment.ARG_ITEM_PATH)
        binding.playerView.showTimeoutMs = 0
        val app = requireNotNull(this.activity).application
        val viewModelFactory = itemPath?.let { PlayerVIewModelFactory(it,app) }
        viewModel = ViewModelProvider.of(this).get(PlayerViewModel::class.java)
        viewModel.itemPath = itemPath
        viewModel.player.observe(viewLifecycleOwner, Observer {
            binding.playerView.player = it
        })
    }

}