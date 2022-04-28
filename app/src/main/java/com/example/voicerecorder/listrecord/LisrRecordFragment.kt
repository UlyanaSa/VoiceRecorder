package com.example.voicerecorder.listrecord

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.voicerecorder.R
import com.example.voicerecorder.database.RecordDatabase
import com.example.voicerecorder.databinding.FragmentLisrRecordBinding

class LisrRecordFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentLisrRecordBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_lisr_record, container, false
        )
        val application: Application = requireNotNull(this.activity).application
        val dataBase = RecordDatabase.getInstance(application).recordDatabaseDAO
        val viewModelFactory = ListRecordViewModelFactory(dataBase)
        val listRecordViewModel = ViewModelProviders.of(
            this, viewModelFactory).get(ListRecordViewModel::class.java)
        binding.listRecorderViewModel = listRecordViewModel
        val adapter = ListRecordAdapter()
        binding.recyclerView.adapter = adapter
        listRecordViewModel.records.observe(viewLifecycleOwner, Observer {
            it?.let{ adapter.data = it}
        })
        binding.lifecycleOwner = this
        return binding.root
    }


}