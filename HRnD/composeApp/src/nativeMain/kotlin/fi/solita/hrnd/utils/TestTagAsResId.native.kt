package fi.solita.hrnd.utils

import androidx.compose.ui.Modifier

actual fun Modifier.enableTestTagAsResId(): Modifier {
    return this // no need to do anything on native
}