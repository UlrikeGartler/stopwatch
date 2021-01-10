package at.fhj.msd.stopwatch.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import at.fhj.msd.stopwatch.databinding.LapTimeItemBinding
import at.fhj.msd.stopwatch.model.LapTime

class LapTimeAdapter(
    private val items: LiveData<List<LapTime>>,
    liveCycleOwner: LifecycleOwner
) : RecyclerView.Adapter<LapTimeAdapter.ViewHolder>() {

    class ViewHolder(val binding: LapTimeItemBinding) : RecyclerView.ViewHolder(binding.root)

    init {
        items.observe(liveCycleOwner, Observer {
            //reload data when the list of laps has changed
            this.notifyDataSetChanged()
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LapTimeItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = items.value?.size ?: 0
    override fun onBindViewHolder(holder: LapTimeAdapter.ViewHolder, position: Int) {
        val lap = items.value?.get(position)
        holder.binding.lapDiff.text = lap?.diffTime
        holder.binding.lapNumber.text = lap?.lapNumber.toString()
        holder.binding.lapTime.text = lap?.lapTime
    }
}


