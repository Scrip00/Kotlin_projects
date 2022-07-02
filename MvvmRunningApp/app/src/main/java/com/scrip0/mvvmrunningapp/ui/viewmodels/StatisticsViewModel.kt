package com.scrip0.mvvmrunningapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.scrip0.mvvmrunningapp.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
	val mainRepository: MainRepository
) : ViewModel() {

}