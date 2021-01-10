package at.fhj.msd.stopwatch.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import at.fhj.msd.stopwatch.adapters.LapTimeAdapter
import at.fhj.msd.stopwatch.databinding.FragmentStopwatchBinding
import at.fhj.msd.stopwatch.viewmodels.StopWatchViewModel


class StopwatchFragment : Fragment() {
    private val viewModel: StopWatchViewModel by viewModels()
    private lateinit var binding: FragmentStopwatchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStopwatchBinding.inflate(inflater)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        subscribe()
        return binding.frameLayout
    }

    private fun subscribe() {

        //hook up events
        binding.btnToggleStopWatch.setOnClickListener { viewModel.toggleStartStop() }
        binding.btnReset.setOnClickListener { viewModel.reset() }
        binding.btnTakeLapTime.setOnClickListener {
            viewModel.takeLapTime()
            binding.recyclerView.scrollToPosition(viewModel.lapTimes.value!!.size - 1);
        }
        binding.btnClearLaps.setOnClickListener { viewModel.clearLaps() }

        //observe view-model for changes
        viewModel.stopStartText.observe(viewLifecycleOwner, Observer {
            binding.btnToggleStopWatch.text = getString(it)  //look up resource id in activity
        })
        viewModel.elapsedTimeString.observe(viewLifecycleOwner, Observer {
            binding.tvEllapsed.text = it
        })
        viewModel.canReset.observe(viewLifecycleOwner, Observer {
            binding.btnReset.isEnabled = it
        })
        viewModel.fractionOfMinute.observe(viewLifecycleOwner, Observer {
            binding.pgMinute.progress = it
        })
        viewModel.canClearLaps.observe(viewLifecycleOwner, Observer {
            binding.btnClearLaps.isEnabled = it
        })
        viewModel.isRunning.observe(viewLifecycleOwner, Observer {
            binding.btnTakeLapTime.isEnabled = it
        })

        //create adapter and bind it to view model
        val lapViewAdapter = LapTimeAdapter(viewModel.lapTimes, viewLifecycleOwner)
        //add adapter to recycleview
        binding.recyclerView.adapter = lapViewAdapter

    }

    //factory method for fragment
    companion object {
        fun newInstance() = StopwatchFragment()
    }
}