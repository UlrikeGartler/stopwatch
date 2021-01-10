package at.fhj.msd.stopwatch.model

/**
 * Represents a recorded time lap time
 */
class LapTime(val lapNumber: Int, val timeMs: Long, val diff: Long?) {
    val lapTime: String //could also be `by lazy { ...}`
        get() = TimeUtil.formatElapsedMs(timeMs)
    val diffTime: String
        get() {
            if (diff == null) return ""
            val sign = if (diff > 0) "+" else ""
            return String.format("%s %.2f", sign, diff.toFloat() / 1000F)
        }
}