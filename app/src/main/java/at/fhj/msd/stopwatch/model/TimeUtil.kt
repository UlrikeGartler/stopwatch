package at.fhj.msd.stopwatch.model

object TimeUtil {
    /**
     * formats time in the pattern mm:ss.ff
     */
    fun formatElapsedMs(timeMs: Long): String {
        val seconds = (timeMs / 1000) % 60
        val minutes = timeMs / 60_000
        val subSeconds = (timeMs % 1000) / 10
        return String.format("%02d:%02d.%02d", minutes, seconds, subSeconds)
    }
}