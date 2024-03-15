import platform.UIKit.UIViewController

interface ScannerFactory {
    companion object {
        var shared: ScannerFactory? = null
    }

    fun makeController(passValue : (String) -> Unit): UIViewController
}