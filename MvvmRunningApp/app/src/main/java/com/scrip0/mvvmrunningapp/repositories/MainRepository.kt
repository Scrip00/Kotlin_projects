package com.scrip0.mvvmrunningapp.repositories

import com.scrip0.mvvmrunningapp.db.Run
import com.scrip0.mvvmrunningapp.db.RunDAO
import javax.inject.Inject

class MainRepository @Inject constructor(
	private val runDAO: RunDAO
) {
	suspend fun insertRun(run: Run) = runDAO.insertRun(run)

	suspend fun deleteRun(run: Run) = runDAO.deleteRun(run)

	fun getAllRunsSorterByDate() = runDAO.getAllRunsSortedByDate()

	fun getAllRunsSorterByDistance() = runDAO.getAllRunsSortedByDistance()

	fun getAllRunsSorterByTimeInMillis() = runDAO.getTotalTimeInMillis()

	fun getAllRunsSorterByAvgSpeed() = runDAO.getTotalAvgSpeed()

	fun getAllRunsSorterByCaloriesBurned() = runDAO.getAllRunsSortedByCaloriesBurned()

	fun getTotalAvgSpeed() = runDAO.getTotalAvgSpeed()

	fun getTotalAvgDistance() = runDAO.getTotalDistance()

	fun getTotalCaloriesBurned() = runDAO.getTotalCaloriesBurned()

	fun getTotalTimeInMillis() = runDAO.getTotalTimeInMillis()
}