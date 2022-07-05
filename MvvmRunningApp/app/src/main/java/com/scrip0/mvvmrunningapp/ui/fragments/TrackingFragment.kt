package com.scrip0.mvvmrunningapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.scrip0.mvvmrunningapp.R
import com.scrip0.mvvmrunningapp.db.Run
import com.scrip0.mvvmrunningapp.other.Constants.ACTION_PAUSE_SERVICE
import com.scrip0.mvvmrunningapp.other.Constants.ACTION_START_OR_RESUME_SERVICE
import com.scrip0.mvvmrunningapp.other.Constants.ACTION_STOP_SERVICE
import com.scrip0.mvvmrunningapp.other.Constants.MAP_ZOOM
import com.scrip0.mvvmrunningapp.other.Constants.POLYLINE_COLOR
import com.scrip0.mvvmrunningapp.other.Constants.POLYLINE_WIDTH
import com.scrip0.mvvmrunningapp.other.TrackingUtility
import com.scrip0.mvvmrunningapp.services.Polyline
import com.scrip0.mvvmrunningapp.services.TrackingService
import com.scrip0.mvvmrunningapp.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_tracking.*
import java.util.*
import javax.inject.Inject
import kotlin.math.round

@AndroidEntryPoint
class TrackingFragment : Fragment(R.layout.fragment_tracking) {

	private val viewModel: MainViewModel by viewModels()

	private var isTracking = false
	private var pathPoints = mutableListOf<Polyline>()

	private var map: GoogleMap? = null

	private var curTimeInMillis = 0L

	private var menu: Menu? = null

	@set:Inject
	var weight = 80f

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initFragmentMenu()

		mapView.onCreate(savedInstanceState)
		btnToggleRun.setOnClickListener {
			toggleRun()
		}

		btnFinishRun.setOnClickListener {
			zoomToSeeWholeTrack()
			endRunAndSaveToDb()
		}

		mapView.getMapAsync {
			map = it
			addAllPolylines()
			moveCameraToUser()
		}

		subscribeToObservers()
	}

	private fun initFragmentMenu() {
		val menuHost: MenuHost = requireActivity()

		menuHost.addMenuProvider(object : MenuProvider {
			override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
				menuInflater.inflate(R.menu.toolbar_tracking_menu, menu)
				this@TrackingFragment.menu = menu
			}

			override fun onPrepareMenu(menu: Menu) {
				super.onPrepareMenu(menu)
				if (curTimeInMillis > 0L) {
					menu.getItem(0).isVisible = true
				}
			}

			override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
				return when (menuItem.itemId) {
					R.id.miCancelTracking -> {
						showCancelTrackingDialog()
						true
					}
					else -> false
				}
			}
		})
	}

	private fun subscribeToObservers() {
		TrackingService.isTracking.observe(viewLifecycleOwner) {
			updateTracking(it)
		}

		TrackingService.pathPoints.observe(viewLifecycleOwner) {
			pathPoints = it
			addLatestPolyline()
			moveCameraToUser()
		}

		TrackingService.timeRunInMillis.observe(viewLifecycleOwner) {
			curTimeInMillis = it
			val formattedTime = TrackingUtility.getFormattedStopWatchTime(curTimeInMillis, true)
			tvTimer.text = formattedTime
		}
	}

	private fun toggleRun() {
		if (isTracking) {
			menu?.getItem(0)?.isVisible = true
			sendCommandToService(ACTION_PAUSE_SERVICE)
		} else {
			sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
		}
	}

	private fun showCancelTrackingDialog() {
		val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
			.setTitle("Cancel the run")
			.setMessage("Are you sure to cancel the current run and delete all its data?")
			.setIcon(R.drawable.ic_delete)
			.setPositiveButton("Yes") { _, _ ->
				stopRun()
			}
			.setNegativeButton("No") { dialogInterface, _ ->
				dialogInterface.cancel()
			}
			.create()
		dialog.show()
	}

	private fun stopRun() {
		sendCommandToService(ACTION_STOP_SERVICE)
		findNavController().navigate(R.id.action_trackingFragment_to_runFragment)
	}

	private fun updateTracking(isTracking: Boolean) {
		this.isTracking = isTracking
		if (!isTracking) {
			btnToggleRun.text = "Start"
			btnFinishRun.visibility = View.VISIBLE
		} else {
			btnToggleRun.text = "Stop"
			menu?.getItem(0)?.isVisible = true
			btnFinishRun.visibility = View.GONE
		}
	}

	private fun moveCameraToUser() {
		if (pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
			map?.animateCamera(
				CameraUpdateFactory.newLatLngZoom(
					pathPoints.last().last(),
					MAP_ZOOM
				)
			)
		}
	}

	private fun zoomToSeeWholeTrack() {
		val bounds = LatLngBounds.Builder()
		for (polyline in pathPoints) {
			for (pos in polyline) {
				bounds.include(pos)
			}
		}

		map?.moveCamera(
			CameraUpdateFactory.newLatLngBounds(
				bounds.build(),
				mapView.width,
				mapView.height,
				(mapView.height * 0.05).toInt()
			)
		)
	}

	private fun endRunAndSaveToDb() {
		map?.snapshot { bmp ->
			var distanceInMeters = 0
			for (polyline in pathPoints) {
				distanceInMeters += TrackingUtility.calculatePolylineLength(polyline).toInt()
			}
			val avgSpeed =
				round((distanceInMeters / 1000f) / (curTimeInMillis / 1000f / 3600f) * 10f) / 10f
			val dateTimestamp = Calendar.getInstance().timeInMillis
			val caloriesBurned = ((distanceInMeters / 1000) * weight).toInt()
			val run =
				Run(bmp, dateTimestamp, avgSpeed, distanceInMeters, curTimeInMillis, caloriesBurned)
			viewModel.insertRun(run)
			Snackbar.make(
				requireActivity().findViewById(R.id.rootView),
				"Run saved successfully",
				Snackbar.LENGTH_LONG
			).show()
			stopRun()
		}
	}

	private fun addAllPolylines() {
		for (polyline in pathPoints) {
			val polylineOptions = PolylineOptions()
				.color(POLYLINE_COLOR)
				.width(POLYLINE_WIDTH)
				.addAll(polyline)
			map?.addPolyline(polylineOptions)
		}
	}

	private fun addLatestPolyline() {
		if (pathPoints.isNotEmpty() && pathPoints.last().size > 1) {
			val preLastLatLng = pathPoints.last()[pathPoints.last().size - 2]
			val lastLatLng = pathPoints.last().last()
			val polylineOptions = PolylineOptions()
				.color(POLYLINE_COLOR)
				.width(POLYLINE_WIDTH)
				.add(preLastLatLng)
				.add(lastLatLng)
			map?.addPolyline(polylineOptions)
		}
	}

	private fun sendCommandToService(action: String) =
		Intent(requireContext(), TrackingService::class.java).also {
			it.action = action
			requireContext().startService(it)
		}

	override fun onResume() {
		super.onResume()
		mapView?.onResume()
	}

	override fun onStart() {
		super.onStart()
		mapView?.onStart()
	}

	override fun onStop() {
		super.onStop()
		mapView?.onStop()
	}

	override fun onPause() {
		super.onPause()
		mapView?.onPause()
	}

	override fun onLowMemory() {
		super.onLowMemory()
		mapView?.onLowMemory()
	}

	override fun onDestroy() {
		super.onDestroy()
		mapView?.onDestroy()
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		mapView?.onSaveInstanceState(outState)
	}
}