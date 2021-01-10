package at.fhj.msd.stopwatch.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import at.fhj.msd.stopwatch.BuildConfig
import at.fhj.msd.stopwatch.R
import at.fhj.msd.stopwatch.model.LapTime
import at.fhj.msd.stopwatch.model.TimeUtil
import java.util.concurrent.*

//timer precession in MS
const val TICK_RATE_MS = 25L

class StopWatchViewModel : ViewModel() {

    private val timer: ScheduledExecutorService = Executors.newScheduledThreadPool(1)
    private val lapTimesList = mutableListOf<LapTime>()
    private var timerTask: ScheduledFuture<*>? = null

    val isRunning = MutableLiveData<Boolean>(false)
    val speedFactor = BuildConfig.SPEEDFACTOR //use this to speed up the clock for easier debugging

    /**
    elapsed time in ms
     */
    val elapsed = MutableLiveData<Long>(0L)
    val lapTimes = MutableLiveData(lapTimesList as List<LapTime>)
    val canClearLaps = Transformations.map(lapTimes) { it.isNotEmpty() }

    /**
     * Text of the start/stop button of the stop watch as resource id
     */
    val stopStartText = Transformations.map(isRunning) {
        if (it) R.string.stopwatch_stop else R.string.stopwatch_start
    }

    /**
     * can we reset the stop watch
     */
    val canReset = Transformations.map(elapsed) {
        it > 0
    }

    /**
     * percentage of current minute, used to animate stop watch
     */
    val fractionOfMinute = Transformations.map(elapsed) {
        (((it / 1000L) % 60L).toFloat() / 60F) * 100F
    }

    /**
     * elapsed time as formatted string
     */
    val elapsedTimeString = Transformations.map(elapsed) { TimeUtil.formatElapsedMs(it) }

    //we keep `handleTick` public for easier unit test (bad style)
    fun handleTick() {
        val curMs = elapsed.value ?: 0L
        elapsed.postValue(curMs + TICK_RATE_MS * speedFactor)
    }

    /**
     * clear recorder lap times
     */
    fun clearLaps() {
        lapTimesList.clear()
        lapTimes.notifyObservers()
    }


    /**
     * Store the current time and reset
     */
    fun takeLapTime() {
        //create a new lap time, take time diff from prev lap if exists (otherwise null)
        val prevTime = lapTimesList.lastOrNull()
        val elapsedLap: Long = elapsed.value!!
        val lapDiff = if (prevTime != null) elapsedLap - prevTime.timeMs else null
        val lapTime = LapTime(lapTimesList.size + 1, elapsedLap, lapDiff)
        lapTimesList.add(lapTime)
        lapTimes.notifyObservers()
        elapsed.value = 0
    }

    /**
     * Reset both measured time and laps
     */
    fun reset() {
        elapsed.value = 0
        clearLaps()
    }

    /**
     * Starts stops the watch based on its state
     */
    fun toggleStartStop() {
        if (isRunning.value!!) stop()
        else start()
    }

    private fun start() {
        stop()
        timerTask = timer.scheduleAtFixedRate(
            Runnable { handleTick() },
            TICK_RATE_MS,
            TICK_RATE_MS,
            TimeUnit.MILLISECONDS
        )
        isRunning.value = true
    }

    private fun stop() {
        if (timerTask == null) return //do nothing
        timerTask?.cancel(true)
        timerTask = null
        isRunning.value = false
    }


}