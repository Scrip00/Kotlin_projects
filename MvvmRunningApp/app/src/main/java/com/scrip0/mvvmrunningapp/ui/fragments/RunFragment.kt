package com.scrip0.mvvmrunningapp.ui.fragments

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.scrip0.mvvmrunningapp.R
import com.scrip0.mvvmrunningapp.adapters.RunAdapter
import com.scrip0.mvvmrunningapp.other.Constants.REQUEST_CODE_LOCATION_PERMISSION
import com.scrip0.mvvmrunningapp.other.SortType
import com.scrip0.mvvmrunningapp.other.TrackingUtility
import com.scrip0.mvvmrunningapp.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_run.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class RunFragment : Fragment(R.layout.fragment_run), EasyPermissions.PermissionCallbacks {

	private val viewModel: MainViewModel by viewModels()

	private lateinit var runAdapter: RunAdapter

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		requestPermissions()
		setupRecyclerView()

		when (viewModel.sortType) {
			SortType.DATE -> spFilter.setSelection(0)
			SortType.RUNNING_TIME -> spFilter.setSelection(1)
			SortType.DISTANCE -> spFilter.setSelection(2)
			SortType.AVG_SPEED -> spFilter.setSelection(3)
			SortType.CALORIES_BURNED -> spFilter.setSelection(4)
		}

		spFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
			override fun onItemSelected(
				adapterView: AdapterView<*>?,
				view: View?,
				pos: Int,
				id: Long
			) {
				when (pos) {
					0 -> viewModel.sortRuns(SortType.DATE)
					1 -> viewModel.sortRuns(SortType.RUNNING_TIME)
					2 -> viewModel.sortRuns(SortType.DISTANCE)
					3 -> viewModel.sortRuns(SortType.AVG_SPEED)
					4 -> viewModel.sortRuns(SortType.CALORIES_BURNED)
				}
			}

			override fun onNothingSelected(p0: AdapterView<*>?) {}
		}

		viewModel.runs.observe(viewLifecycleOwner) {
			runAdapter.submitList(it)
		}

		fab.setOnClickListener {
			findNavController().navigate(R.id.action_runFragment_to_trackingFragment)
		}
	}

	private fun setupRecyclerView() = rvRuns.apply {
		runAdapter = RunAdapter()
		adapter = runAdapter
		layoutManager = LinearLayoutManager(requireContext())
	}

	private fun requestPermissions() {
		if (TrackingUtility.hasLocationPermissions(requireContext())) {
			return
		}
		EasyPermissions.requestPermissions(
			this,
			"You need to accept location permissions to use this app.",
			REQUEST_CODE_LOCATION_PERMISSION,
			Manifest.permission.ACCESS_COARSE_LOCATION,
			Manifest.permission.ACCESS_FINE_LOCATION,
		)
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			EasyPermissions.requestPermissions(
				this,
				"You need to accept background location permissions to use this app.",
				REQUEST_CODE_LOCATION_PERMISSION,
				Manifest.permission.ACCESS_BACKGROUND_LOCATION
			)
		}
	}

	override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
		if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
			AppSettingsDialog.Builder(this).build().show()
		} else {
			requestPermissions()
		}
	}

	override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

	override fun onRequestPermissionsResult(
		requestCode: Int,
		permissions: Array<out String>,
		grantResults: IntArray
	) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
		EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
	}
}