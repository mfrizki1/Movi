package id.calocallo.movi

import android.app.Application
import id.calocallo.movi.data.MoviRepository
import id.calocallo.movi.data.sources.MoviDataSource
import id.calocallo.movi.network.MoviWebServices
import id.calocallo.movi.network.RetrofitProvider
import id.calocallo.movi.presentation.GenreViewModel
import id.calocallo.movi.presentation.MovieDetailViewModel
import id.calocallo.movi.presentation.MovieListViewModel
import id.calocallo.movi.presentation.MovieReviewsViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

class MoviApplication : Application() {
    private val nameIODispatcher = "IODispatcher"

    override fun onCreate() {
        super.onCreate()

        val ioDispatcher = module {
            single(named(nameIODispatcher)) {
                Dispatchers.IO
            }
        }

        val webServiceModule = module {
            single<MoviWebServices> {
                RetrofitProvider.retrofit().create(MoviWebServices::class.java)
            }
        }

        val dataSourceModule = module {
            single { MoviDataSource(get(), get(named(nameIODispatcher))) }
        }

        val repositoryModule = module {
            single { MoviRepository(get()) }
        }

        val viewModelModule = module {
            viewModel { GenreViewModel(get()) }
            viewModel { MovieListViewModel(get()) }
            viewModel { MovieDetailViewModel(get()) }
            viewModel { MovieReviewsViewModel(get()) }
        }

        val moviModules = module {
            includes(
                ioDispatcher,
                webServiceModule,
                dataSourceModule,
                repositoryModule,
                viewModelModule,
            )
        }

        startKoin {
            // androidLogger(Level.DEBUG)
            androidContext(this@MoviApplication)
            modules(moviModules)
        }
    }
}
