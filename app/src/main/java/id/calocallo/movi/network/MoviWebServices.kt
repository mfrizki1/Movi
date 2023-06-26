package id.calocallo.movi.network

import id.calocallo.movi.data.remote.GenreListResponse
import id.calocallo.movi.data.remote.MovieDetailResponse
import id.calocallo.movi.data.remote.MovieListResponse
import id.calocallo.movi.data.remote.MovieReviewsListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviWebServices {

    @GET(EndPoint.GENRE)
    suspend fun getGenreList(): GenreListResponse

    @GET(EndPoint.MOVIE_LIST)
    suspend fun getMovieList(
        @Query("page") page: Int,
        @Query("with_genres") genres: String,
    ): MovieListResponse

    @GET(EndPoint.MOVIE_DETAIL)
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: String,
        @Query("append_to_response") appendToResponse: String,
    ): MovieDetailResponse

    @GET(EndPoint.REVIEW_LIST)
    suspend fun getMovieReviewsList(
        @Path("movie_id") movieId: String,
        @Query("page") page: String,
    ): MovieReviewsListResponse.MovieReviews

    object EndPoint {
        const val GENRE = "genre/movie/list"
        const val MOVIE_LIST = "discover/movie"
        const val MOVIE_DETAIL = "movie/{movie_id}"
        const val REVIEW_LIST = "$MOVIE_DETAIL/reviews"
    }
}
