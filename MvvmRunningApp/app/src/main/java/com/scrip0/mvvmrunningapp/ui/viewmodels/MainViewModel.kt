package com.scrip0.mvvmrunningapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scrip0.mvvmrunningapp.db.Run
import com.scrip0.mvvmrunningapp.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
	val mainRepository: MainRepository
) : ViewModel() {

	val runsSortedByDate = mainRepository.getAllRunsSorterByDate()

	fun insertRun(run: Run) = viewModelScope.launch {
		mainRepository.insertRun(run)
	}
}