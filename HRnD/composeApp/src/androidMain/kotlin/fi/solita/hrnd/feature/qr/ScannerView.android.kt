package fi.solita.hrnd.feature.qr

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.CompoundBarcodeView
import io.github.aakira.napier.Napier

@Composable
actual fun Scanner(modifier: Modifier, passResult: (String) -> Unit) {
    val context = LocalContext.current
    val activity = LocalContext.current.applicationContext

    var hasCamPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CAMERA,
            ) == PackageManager.PERMISSION_GRANTED,
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            Napier.i { "permission request result $granted" }
            hasCamPermission = granted
        },
    )

    LaunchedEffect(key1 = Unit) {
        launcher.launch(android.Manifest.permission.CAMERA)
    }

    val compoundBarcodeView = remember(hasCamPermission) {
        Napier.i { "compoundBarcoudView, permission: $hasCamPermission" }
        if (hasCamPermission) {
            CompoundBarcodeView(context).apply {
                val capture = CaptureManager(context as Activity, this)
                capture.initializeFromIntent(context.intent, null)
                capture.decode()
                resume()
                this.decodeContinuous { result ->
                    Napier.i("QRCode scanned $result")
                    pause() // we found it, pause scanning.
                    passResult(result.text)
                }
            }
        } else {
            Napier.i { "null here :(" }
            null
        }
    }

    // Observe the lifecycle events and update compoundBarcodeView accordingly
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val lifecycle: Lifecycle = lifecycleOwner.lifecycle
    val observer = LifecycleEventObserver { _, event ->
        Napier.i { "Lifecycle event : $event" }
        when (event) {
            Lifecycle.Event.ON_PAUSE -> {
                compoundBarcodeView?.pause()
            }

            Lifecycle.Event.ON_RESUME -> {
                compoundBarcodeView?.resume()
            }

            else -> {}
        }
    }

    DisposableEffect(lifecycleOwner) {
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.error),
        contentAlignment = Alignment.Center,
    ) {
        if (hasCamPermission) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                compoundBarcodeView?.let {
                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = {
                            compoundBarcodeView
                        },
                    )
                }
            }
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "This feature requires camera permissions.")
                Button(
                    onClick = {
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", activity.packageName, null),
                        ).also { intent ->
                            activity.startActivity(intent)
                        }
                    },
                ) {
                    Text(text = "Open App Settings")
                }
            }
        }
    }
}