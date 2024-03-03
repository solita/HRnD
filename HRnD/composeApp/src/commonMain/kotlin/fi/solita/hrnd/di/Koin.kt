package fi.solita.hrnd.di

import fi.solita.hrnd.core.data.HealthRepository
import fi.solita.hrnd.feature.list.ListScreenModel
import fi.solita.hrnd.core.data.HealthApi
import fi.solita.hrnd.core.data.HealthRepositoryImpl
import fi.solita.hrnd.core.data.KtorHealthApi
import fi.solita.hrnd.feature.details.DetailsScreenModel
import fi.solita.hrnd.feature.qr.ScanQRScreenModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
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
    single<CoroutineDispatcher> { Dispatchers.IO }
    single<HealthRepository> {
        HealthRepositoryImpl(get(), get())
    }
}

val screenModelsModule = module {
    factoryOf(::ListScreenModel)
    factory<DetailsScreenModel> { p -> DetailsScreenModel(p[0], get()) }
    factoryOf(::ScanQRScreenModel)
}

fun initKoin() {
    startKoin {
        modules(
            dataModule,
            screenModelsModule,
        )
    }
}
