package fi.solita.hrnd.domain.utils

enum class Platform {
    iOS,
    Android
}

expect fun getPlatform() : Platform