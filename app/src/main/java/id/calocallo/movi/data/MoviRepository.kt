package id.calocallo.movi.data

import id.calocallo.movi.data.remote.GenreListResponse
import id.calocallo.movi.data.remote.MovieDetailResponse
import id.calocallo.movi.data.remote.MovieListResponse
import id.calocallo.movi.data.remote.MovieReviewsListResponse
import id.calocallo.movi.data.sources.MoviDataSource
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class MoviRepository(private val dataSource: MoviDataSource) {

    private val latestGenreMovieMutex = Mutex()

    private var latestGenreList: List<GenreListResponse.Genre> = emptyList()

    suspend fun getGenreList(): List<GenreListResponse.Genre> {
        if (latestGenreList.isEmpty()) {
            val networkResult = dataSource.getGenreList()
            latestGenreMovieMutex.withLock { this.latestGenreList = networkResult.genres }
        }

        return latestGenreMovieMutex.withLock { this.latestGenreList }
    }

    private val latestMovieListMutex = Mutex()
    private var latestMovieList: MovieListResponse? = null
    suspend fun getMovieList(
        refresh: Boolean = false,
        page: Int,
        genres: String,
    ): MovieListResponse {
        val result = dataSource.getMovieList(page, genres)
        return latestMovieListMutex.withLock { result }
    }

    private val latestMovieDetailMutex = Mutex()
    private var latestMovieDetail: MovieDetailResponse? = null
    suspend fun getMovieDetail(movieId: String, appendToResponse: String): MovieDetailResponse {
        val result = dataSource.getMovieDetail(movieId, appendToResponse)
        return latestMovieDetailMutex.withLock { result }
    }

    private val latestUserReviewsListMutex = Mutex()
    suspend fun getMovieReviewsList(movieId: String, page: String): MovieReviewsListResponse.MovieReviews {
        val result = dataSource.getMovieReviews(movieId, page)
        return latestUserReviewsListMutex.withLock { result }
    }
}
