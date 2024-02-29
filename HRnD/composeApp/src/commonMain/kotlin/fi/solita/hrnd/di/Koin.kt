package fi.solita.hrnd.di

import fi.solita.hrnd.data.HealthRepository
import fi.solita.hrnd.presentation.screens.list.ListScreenModel
import fi.solita.hrnd.data.HealthApi
import fi.solita.hrnd.data.HealthRepositoryImpl
import fi.solita.hrnd.data.KtorHealthApi
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val dataModule = module {
    single {
        val json = Json { ignoreUnknownKeys = true }
        HttpClient {
            install(ContentNegotiation) {
                json(json, contentType = ContentType.Application.Json)
            }
        }
    }

    single<HealthApi> { KtorHealthApi(get()) }
    single<HealthRepository> {
        HealthRepositoryImpl(get())
    }
}

val screenModelsModule = module {
    factoryOf(::ListScreenModel)
}

fun initKoin() {
    startKoin {
        modules(
            dataModule,
            screenModelsModule,
        )
    }
}
