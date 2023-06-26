package id.calocallo.movi.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import id.calocallo.movi.R
import id.calocallo.movi.data.remote.MovieListResponse
import id.calocallo.movi.data.remote.MovieReviewsListResponse
import id.calocallo.movi.databinding.ActivityMovieDetailBinding
import id.calocallo.movi.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieDetailActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMovieDetailBinding::inflate)

    private val viewModel: MovieDetailViewModel by viewModel()

    private var movieData: MovieListResponse.MoviesResponse? = MovieListResponse.MoviesResponse()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        movieData = intent.parcelable(Constant.MOVIE_DATA)

        setupView()
        observeData()
    }

    private fun setupView() {
        binding.apply {
            setSupportActionBar(layoutMovieDetailToolbar.toolbar)
            setupSupportActionBar(
                supportActionBar = supportActionBar,
                title = movieData?.title.orEmpty(),
            )

            requestMovieDetail()

            tvMovieDetailUserReviewSeeMore.setOnClickListener {
                val bundle = bundleOf(Constant.MOVIE_DATA to movieData)
                intentTo(MovieReviewsActivity::class.java.name, bundle)
            }
        }
    }

    private fun requestMovieDetail() {
        viewModel.requestMovieDetail(
            movieId = movieData?.id.toString(),
            appendToResponse = "images,videos,reviews",
        )
    }

    @SuppressLint("SetTextI18n")
    private fun observeData() {
        binding.apply {
            viewModel.movieDetail.observe(this@MovieDetailActivity) { result ->
                when {
                    result.isLoading -> {
                        pageLoading()
                    }

                    result.data != null -> {
                        pageData()
                        val data = result.data
                        val trailerVideo = data.videos.results.filter { videoResult ->
                            videoResult.type == "Trailer"
                        }

                        val userReviewsData = data.reviews.results
                        if (userReviewsData.size.orNol() < 1) {
                            layoutMovieUserReviewItem.root.gone()
                            tvMovieDetailUserReviewSeeMore.gone()
                        } else {
                            layoutMovieUserReviewItem.root.visible()
                            tvMovieDetailUserReviewSeeMore.visible()
                        }

                        tvMovieDetaiUserReviewsTitile.text =
                            "User Reviews (${userReviewsData.size})"

                        val reviewsHighestRating = if (userReviewsData.isNotEmpty()) {
                            userReviewsData.maxBy { dataReviewMax -> dataReviewMax.authorDetails.rating.orNol() }
                        } else {
                            null
                        }

                        ypvMovieDetail.addYouTubePlayerListener(object :
                            AbstractYouTubePlayerListener() {
                            override fun onReady(youTubePlayer: YouTubePlayer) {
                                youTubePlayer.cueVideo(
                                    trailerVideo[0].key,
                                    0f,
                                )
                            }
                        })
                        tvMovieDetailDataDate.text = convertStringToDate(
                            dateTime = data.releaseDate,
                            inputDateFormat = "yyyy-MM-dd",
                            outputDateFormat = "dd MMMM yyyy",
                        )

                        tvMovieDetailDataVote.text = "${"%.1f".format(data.voteAverage)}/10"

                        tvMovieDetailDataSummary.text = data.overview

                        showUserRating(reviewsHighestRating)
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

                        layoutMovieDetailErrorPage.apply {
                            tvErrorPageTitle.text = errorTitle
                            btnErrorPage.setOnClickListener {
                                btnErrorPage.setOnClickListener {
                                    requestMovieDetail()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showUserRating(userReview: MovieReviewsListResponse.MovieReviews.MovieReviewsResult?) {
        binding.layoutMovieUserReviewItem.apply {
            ivMovieDetailReviewUsername.loadImage(
                userReview?.authorDetails?.avatarPath.orEmpty().trimImageLink(),
                PlaceHolderImage.Profile.toString(),
            )
            tvMovieDetailReviewUsername.text =
                if (!userReview?.authorDetails?.name.isNullOrEmpty()) {
                    userReview?.authorDetails?.name
                } else {
                    userReview?.authorDetails?.username
                }
            tvMovieDetailReviewRating.text = userReview?.authorDetails?.rating.orNol().toString()
            tvMovieDetailReviewText.text = userReview?.content
        }
    }

    private fun pageLoading() {
        binding.apply {
            pbMovieDetail.root.visible()
            llMovieDetailData.gone()
            ypvMovieDetail.gone()
            layoutMovieDetailErrorPage.root.gone()
        }
    }

    private fun pageData() {
        binding.apply {
            pbMovieDetail.root.gone()
            llMovieDetailData.visible()
            ypvMovieDetail.visible()
            layoutMovieDetailErrorPage.root.gone()
        }
    }

    private fun pageError() {
        binding.apply {
            pbMovieDetail.root.gone()
            llMovieDetailData.gone()
            ypvMovieDetail.gone()
            layoutMovieDetailErrorPage.root.visible()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}