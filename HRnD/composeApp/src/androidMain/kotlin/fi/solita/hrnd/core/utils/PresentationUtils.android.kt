package fi.solita.hrnd.core.utils

import android.content.res.Configuration
import fi.solita.hrnd.HrndApp
import io.github.aakira.napier.Napier

/**Returns true when screen is in portrait mode.*/
actual fun isPortrait(): Boolean {
    val context = HrndApp.appContext
    val orientation: Int = context.resources.configuration.orientation
    return orientation == Configuration.ORIENTATION_PORTRAIT
}