package id.calocallo.movi.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import id.calocallo.movi.BuildConfig
import id.calocallo.movi.R
import id.calocallo.movi.data.remote.GenreListResponse
import id.calocallo.movi.data.remote.MovieListResponse
import id.calocallo.movi.databinding.ActivityMoviesListBinding
import id.calocallo.movi.databinding.ActivityMoviesListItemBinding
import id.calocallo.movi.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MoviesListActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMoviesListBinding::inflate)

    private val viewModel: MovieListViewModel by viewModel()

    private val movieListAdapter: GenericAdapter<MovieListResponse.MoviesResponse> by
        genericAdapterLazy(
            layoutRes = R.layout.activity_movies_list_item,
            onBind = { _, item ->
                setupMoviesItemAdapter(item)
            },
            onBindError = { },

        )

    private var genreData: GenreListResponse.Genre? = GenreListResponse.Genre()
    private var genreId: String = ""

    private var page = 1
    private var totalPage: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        genreData = intent.parcelable(Constant.GENRE_DATA)
        genreId = genreData?.id.orNol().toString()

        scrollListener()
        setupView()
        observeData()
    }

    @SuppressLint("SetTextI18n")
    private fun setupView() {
        binding.apply {
            layoutMoviesListErrorPage.root.gone()

            rvMoviesList.adapter = movieListAdapter

            tvMoviesListTitle.text = "Berikut list film sesuai genre ${genreData?.name.orEmpty()}"

            rvMoviesList.adapter = movieListAdapter

            requestGetMoviesList()
        }
    }

    private fun View.setupMoviesItemAdapter(data: MovieListResponse.MoviesResponse) {
        ActivityMoviesListItemBinding.bind(this).run {
            var imageUrl = ""
            if (data.posterPath.isNotEmpty()) {
                imageUrl = BuildConfig.SMALL_IMAGE_URL + data.posterPath
            }
            ivMoviesListItem.loadImage(imageUrl)
            tvMoviesListItemTitle.text = data.title
            tvMovieListItemRating.text = data.voteAverage.orNol().toString()
            tvMoviesListItemYear.text = convertStringToDate(
                dateTime = data.releaseDate,
                inputDateFormat = "yyyy-MM-dd",
                outputDateFormat = "yyyy",
            )

            root.setOnClickListener {
                val bundle = bundleOf(Constant.MOVIE_DATA to data)
                intentTo(MovieDetailActivity::class.java.name, bundle)
            }
        }
    }

    private fun requestGetMoviesList(refresh: Boolean = false) {
        viewModel.requestGetMovieList(refresh, page, genres = genreId)
    }

    private fun scrollListener() {
        binding.rvMoviesList.onReachBottomScroll {
            page += 1
            requestGetMoviesList()
        }
    }

    private fun observeData() {
        binding.apply {
            viewModel.movieList.observe(this@MoviesListActivity) { result ->
                when {
                    result.isLoading -> {
                        layoutMoviesListErrorPage.root.gone()
                        if (page == 1) {
                            pbMoviesList.root.gone()
                            movieListAdapter.pushLoadingWithClear()
                        } else {
                            pbMoviesList.root.visible()
                        }
                    }
                    result.data?.moviesResponse?.isEmpty() == true -> {
                        pbMoviesList.root.gone()
                        layoutMoviesListErrorPage.root.gone()
                    }
                    result.data?.moviesResponse?.isNotEmpty() == true -> {
                        pbMoviesList.root.gone()
                        layoutMoviesListErrorPage.root.gone()
                        totalPage = result.data.totalPages.orNol()

                        if (page == 1) {
                            movieListAdapter.pushItemsWithClear(result.data.moviesResponse)
                        } else {
                            movieListAdapter.pushItems(result.data.moviesResponse)
                        }
                    }
                    result.error?.isNotEmpty() == true -> {
                        pbMoviesList.root.gone()
                        layoutMoviesListErrorPage.root.visible()
                        val errorTitle = when {
                            result.error.contains("No address associated", ignoreCase = true) -> {
                                getString(R.string.no_connection)
                            }
                            result.error.contains("timeout", ignoreCase = true) -> {
                                getString(R.string.no_connection)
                            }
                            else -> {
                                "Unknown Error"
                            }
                        }
                        layoutMoviesListErrorPage.apply {
                            tvErrorPageTitle.text = errorTitle
                            btnErrorPage.setOnClickListener {
                                btnErrorPage.setOnClickListener {
                                    requestGetMoviesList(true)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
