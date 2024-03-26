package fi.solita.hrnd.benchmark

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.PowerCategory
import androidx.benchmark.macro.PowerCategoryDisplayLevel
import androidx.benchmark.macro.PowerMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.time.Duration.Companion.seconds

/**
 * This is an example startup benchmark.
 *
 * It navigates to the device's home screen, and launches the default activity.
 *
 * Before running this benchmark:
 * 1) switch your app's active build variant in the Studio (affects Studio runs only)
 * 2) add `<profileable android:shell="true" />` to your app's manifest, within the `<application>` tag
 *
 * Run this benchmark from Studio to see startup measurements, and captured system traces
 * for investigating your app's performance.
 */
@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalMetricApi::class)
class BenchmarkTests {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun hotStartup() = benchmarkRule.measureRepeated(
        packageName = "fi.solita.hrnd.benchmark",
        metrics = listOf(StartupTimingMetric()),
        iterations = 5,
        startupMode = StartupMode.HOT
    ) {
        pressHome()
        Thread.sleep(500)
        startActivityAndWait()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @Test
    fun coldStartBenchmark() = benchmarkRule.measureRepeated(
        packageName = "fi.solita.hrnd.benchmark",
        metrics = listOf(
            StartupTimingMetric(),
            FrameTimingMetric(),
            PowerMetric(type = PowerMetric.Type.Battery())
        ),
        iterations = 5,
        startupMode = StartupMode.COLD
    ) {
        pressHome()
        startActivityAndWait()
        scrollDown()
        runBlocking {
            delay(2.seconds)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @Test
    fun warmStartBenchmark() = benchmarkRule.measureRepeated(
        packageName = "fi.solita.hrnd.benchmark",
        metrics = listOf(
            StartupTimingMetric(),
            FrameTimingMetric(),
            PowerMetric(type = PowerMetric.Type.Battery())
        ),
        iterations = 5,
        startupMode = StartupMode.WARM
    ) {
        pressHome()
        startActivityAndWait()
        scrollDown()
        runBlocking {
            delay(2.seconds)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @Test
    fun batteryUsageTest() = benchmarkRule.measureRepeated(
        packageName = "fi.solita.hrnd.benchmark",
        metrics = listOf(
            PowerMetric(type = PowerMetric.Type.Battery()),
            PowerMetric(type = PowerMetric.Type.Power(powerCategories = mapOf(
                PowerCategory.CPU to PowerCategoryDisplayLevel.BREAKDOWN,
                PowerCategory.CPU to PowerCategoryDisplayLevel.TOTAL,
                PowerCategory.GPU to PowerCategoryDisplayLevel.BREAKDOWN,
                PowerCategory.GPU to PowerCategoryDisplayLevel.TOTAL,
                PowerCategory.DISPLAY to PowerCategoryDisplayLevel.BREAKDOWN,
                PowerCategory.DISPLAY to PowerCategoryDisplayLevel.TOTAL,
            ))),
            PowerMetric(type = PowerMetric.Type.Energy(energyCategories = mapOf(
                PowerCategory.CPU to PowerCategoryDisplayLevel.BREAKDOWN,
                PowerCategory.CPU to PowerCategoryDisplayLevel.TOTAL,
                PowerCategory.GPU to PowerCategoryDisplayLevel.BREAKDOWN,
                PowerCategory.GPU to PowerCategoryDisplayLevel.TOTAL,
                PowerCategory.DISPLAY to PowerCategoryDisplayLevel.BREAKDOWN,
                PowerCategory.DISPLAY to PowerCategoryDisplayLevel.TOTAL,
            ))),
        ),
        iterations = 3,
        startupMode = StartupMode.WARM
    ) {
        pressHome()
        startActivityAndWait()
        device.findObject(By.res("fab_qr")).click()
        runBlocking {
            delay(5.seconds)
        }
    }
}

fun MacrobenchmarkScope.scrollDown() {
    // Add elements to the screen
    // Scroll down
    val list = device.findObject(By.res("patient_list"))
    list.setGestureMargin(device.displayWidth / 5)
    list.fling(Direction.DOWN)
}

fun MacrobenchmarkScope.scrollUp() {
    // Add elements to the screen
    // Scroll down
    val list = device.findObject(By.res("patient_list"))
    list.setGestureMargin(device.displayWidth / 5)
    list.fling(Direction.UP)
}