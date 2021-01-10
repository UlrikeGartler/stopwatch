package at.fhj.msd.stopwatch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import at.fhj.msd.stopwatch.viewmodels.StopWatchViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class StopWatchViewModelTest {
    //subject under test
    private val viewModel = StopWatchViewModel()

    //needed for async live data
    @Rule @JvmField
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun testStartStopButton(){
        assertEquals(R.string.stopwatch_start, viewModel.stopStartText.getOrAwaitValue())
        viewModel.toggleStartStop()
        assertEquals(R.string.stopwatch_stop, viewModel.stopStartText.getOrAwaitValue())
        viewModel.toggleStartStop()
        assertEquals(R.string.stopwatch_start, viewModel.stopStartText.getOrAwaitValue())
    }

    @Before
    fun startTime(){
        viewModel.toggleStartStop()
        viewModel.toggleStartStop()
    }

    @Test
    fun testResetButton(){
        assertEquals("Reset", viewModel.canReset)
    }
}