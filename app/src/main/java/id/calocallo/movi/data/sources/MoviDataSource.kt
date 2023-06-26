package id.calocallo.movi.data.sources

import id.calocallo.movi.data.remote.GenreListResponse
import id.calocallo.movi.data.remote.MovieDetailResponse
import id.calocallo.movi.data.remote.MovieListResponse
import id.calocallo.movi.data.remote.MovieReviewsListResponse
import id.calocallo.movi.network.MoviWebServices
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class MoviDataSource(
    private val webServices: MoviWebServices,
    private val ioDispatcher: CoroutineDispatcher,
) {
    suspend fun getGenreList(): GenreListResponse =
        withContext(ioDispatcher) {
            webServices.getGenreList()
        }

    suspend fun getMovieList(page: Int, genres: String): MovieListResponse =
        withContext(ioDispatcher) {
            webServices.getMovieList(page, genres)
        }

    suspend fun getMovieDetail(movieId: String, appendToResponse: String): MovieDetailResponse =
        withContext(ioDispatcher) {
            webServices.getMovieDetail(movieId, appendToResponse)
        }

    suspend fun getMovieReviews(movieId: String, page: String): MovieReviewsListResponse.MovieReviews =
        withContext(ioDispatcher) {
            webServices.getMovieReviewsList(movieId, page)
        }
}
