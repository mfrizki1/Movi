package id.calocallo.movi.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import id.calocallo.movi.R
import id.calocallo.movi.data.remote.MovieListResponse
import id.calocallo.movi.data.remote.MovieReviewsListResponse
import id.calocallo.movi.databinding.ActivityMovieReviewsBinding
import id.calocallo.movi.databinding.ActivityMovieReviewsItemBinding
import id.calocallo.movi.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieReviewsActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMovieReviewsBinding::inflate)
    private val viewModel: MovieReviewsViewModel by viewModel()

    private var movieData: MovieListResponse.MoviesResponse? = MovieListResponse.MoviesResponse()

    private val movieReviewAdapter: GenericAdapter<MovieReviewsListResponse.MovieReviews.MovieReviewsResult> by genericAdapterLazy(
        layoutRes = R.layout.activity_movie_reviews_item,
        onBind = { _, item ->
            setupMovieReviewItemView(item)
        },
        onBindError = {},
    )

    private var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        movieData = intent.parcelable(Constant.MOVIE_DATA)

        setupView()
        observeData()
    }

    private fun setupView() {
        binding.apply {
            setSupportActionBar(layoutMovieUserReviewToolbar.toolbar)
            setupSupportActionBar(supportActionBar, title = movieData?.title.orEmpty())

            rvMovieReviews.apply {
                setDivider(R.color.grey_disabled)
                adapter = movieReviewAdapter
            }
            requestReviewsList()
        }
    }

    /* private fun setupScrollListener() {
         binding.rvMovieReviews.onReachBottomScroll {
             page += 1
             requestReviewsList()
         }
     }*/

    private fun View.setupMovieReviewItemView(data: MovieReviewsListResponse.MovieReviews.MovieReviewsResult?) {
        ActivityMovieReviewsItemBinding.bind(this).run {
            ivMovieDetailReviewUsername.loadImage(
                data?.authorDetails?.avatarPath.orEmpty().trimImageLink(),
                PlaceHolderImage.Profile.toString(),
            )
            tvMovieDetailReviewUsername.text =
                data?.authorDetails?.name?.ifEmpty { data.authorDetails.username }
            tvMovieDetailReviewRating.text = data?.authorDetails?.rating.orNol().toString()
            tvMovieDetailReviewText.text = data?.content
        }
    }

    private fun observeData() {
        viewModel.movieReviewsList.observe(this) { result ->
            val data = result.data?.results
            when {
                result.isLoading -> {
                    pageLoading()
                }

                data?.isNotEmpty() == true -> {
                    pageData()
                    movieReviewAdapter.pushItemsWithClear(data)
                }

                result.error?.isNotEmpty() == true -> {
                    pageError()

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

                    binding.layoutMovieReviewsErrorPage.apply {
                        tvErrorPageTitle.text = errorTitle
                        btnErrorPage.setOnClickListener {
                            btnErrorPage.setOnClickListener {
                                requestReviewsList()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun requestReviewsList() {
        viewModel.requestGetUserReviews(movieData?.id.toString(), page.toString())
    }

    private fun pageLoading() {
        binding.apply {
            pbMovieReviews.root.visible()
            llMovieReviewsData.gone()
            layoutMovieReviewsErrorPage.root.gone()
        }
    }

    private fun pageData() {
        binding.apply {
            pbMovieReviews.root.gone()
            llMovieReviewsData.visible()
            layoutMovieReviewsErrorPage.root.gone()
        }
    }

    private fun pageError() {
        binding.apply {
            pbMovieReviews.root.gone()
            llMovieReviewsData.gone()
            layoutMovieReviewsErrorPage.root.visible()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
