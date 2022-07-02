package com.scrip0.mvvmrunningapp.ui.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.scrip0.mvvmrunningapp.R
import com.scrip0.mvvmrunningapp.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RunFragment : Fragment(R.layout.fragment_run) {

	private val viewModel: MainViewModel by viewModels()
}