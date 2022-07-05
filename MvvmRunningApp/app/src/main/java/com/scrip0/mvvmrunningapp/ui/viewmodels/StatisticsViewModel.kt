package com.scrip0.mvvmrunningapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.scrip0.mvvmrunningapp.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
	private val mainRepository: MainRepository
) : ViewModel() {

	val totalTimeRun = mainRepository.getTotalTimeInMillis()
	val totalDistance = mainRepository.getTotalDistance()
	val totalCaloriesBurned = mainRepository.getTotalCaloriesBurned()
	val totalAvgSpeed = mainRepository.getTotalAvgSpeed()

	val runsSortedByDate = mainRepository.getAllRunsSorterByDate()
}