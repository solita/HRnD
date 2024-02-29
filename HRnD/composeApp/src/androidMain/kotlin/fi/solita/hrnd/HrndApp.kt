package fi.solita.hrnd

import android.app.Application
import fi.solita.hrnd.di.initKoin
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class HrndApp : Application() {
  override fun onCreate() {
    super.onCreate()
    Napier.base(DebugAntilog())
    initKoin()
  }
}
