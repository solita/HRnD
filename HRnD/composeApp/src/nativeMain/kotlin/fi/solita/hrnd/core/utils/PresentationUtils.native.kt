package fi.solita.hrnd.core.utils

import platform.UIKit.UIApplication
import platform.UIKit.UIInterfaceOrientationLandscapeLeft
import platform.UIKit.UIInterfaceOrientationLandscapeRight
import platform.UIKit.UIWindow

/**Returns true when screen is in portrait mode.*/
actual fun isPortrait(): Boolean {
    val window = UIApplication.sharedApplication.windows.first() as? UIWindow
    return when (window?.windowScene?.interfaceOrientation) {
        UIInterfaceOrientationLandscapeLeft, UIInterfaceOrientationLandscapeRight -> false // landscape
        else -> true // portrait
    }
}