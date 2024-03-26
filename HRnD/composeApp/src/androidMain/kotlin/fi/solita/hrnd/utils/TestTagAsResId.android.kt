package fi.solita.hrnd.utils

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId

@OptIn(ExperimentalComposeUiApi::class)
actual fun Modifier.enableTestTagAsResId(): Modifier {
    return this.semantics { testTagsAsResourceId = true }
}