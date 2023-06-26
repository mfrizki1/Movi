package id.calocallo.movi.data.remote

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieDetailResponse(
    @SerializedName("adult")
    var adult: Boolean = false,
    @SerializedName("backdrop_path")
    var backdropPath: String = "",
    @SerializedName("budget")
    var budget: Int = 0,
    @SerializedName("genres")
    var genres: List<GenreListResponse.Genre> = emptyList(),
    @SerializedName("homepage")
    var homepage: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("original_language")
    var originalLanguage: String = "",
    @SerializedName("original_title")
    var originalTitle: String = "",
    @SerializedName("overview")
    var overview: String = "",
    @SerializedName("popularity")
    var popularity: Double = 0.0,
    @SerializedName("poster_path")
    var posterPath: String = "",
    @SerializedName("release_date")
    var releaseDate: String = "",
    @SerializedName("runtime")
    var runtime: Int = 0,
    @SerializedName("status")
    var status: String = "",
    @SerializedName("tagline")
    var tagline: String = "",
    @SerializedName("title")
    var title: String = "",
    @SerializedName("video")
    var video: Boolean = false,
    @SerializedName("vote_average")
    var voteAverage: Double = 0.0,
    @SerializedName("vote_count")
    var voteCount: Int = 0,
    @SerializedName("images")
    var images: Images = Images(),
    @SerializedName("videos")
    var videos: Videos = Videos(),
    @SerializedName("reviews")
    var reviews: MovieReviewsListResponse.MovieReviews = MovieReviewsListResponse.MovieReviews(),
) : Parcelable {

    @Parcelize
    data class Images(
        @SerializedName("backdrops")
        var backdrops: List<Backdrop> = emptyList(),
        @SerializedName("logos")
        var logos: List<Logo> = emptyList(),
        @SerializedName("posters")
        var posters: List<Poster> = emptyList(),
    ) : Parcelable {
        @Parcelize
        data class Backdrop(
            @SerializedName("aspect_ratio")
            var aspectRatio: Double = 0.0,
            @SerializedName("height")
            var height: Int = 0,
            @SerializedName("iso_639_1")
            var iso6391: String = "",
            @SerializedName("file_path")
            var filePath: String = "",
            @SerializedName("vote_average")
            var voteAverage: Double = 0.0,
            @SerializedName("vote_count")
            var voteCount: Int = 0,
            @SerializedName("width")
            var width: Int = 0,
        ) : Parcelable

        @Parcelize
        data class Logo(
            @SerializedName("aspect_ratio")
            var aspectRatio: Double = 0.0,
            @SerializedName("height")
            var height: Int = 0,
            @SerializedName("iso_639_1")
            var iso6391: String = "",
            @SerializedName("file_path")
            var filePath: String = "",
            @SerializedName("vote_average")
            var voteAverage: Double = 0.0,
            @SerializedName("vote_count")
            var voteCount: Int = 0,
            @SerializedName("width")
            var width: Int = 0,
        ) : Parcelable

        @Parcelize
        data class Poster(
            @SerializedName("aspect_ratio")
            var aspectRatio: Double = 0.0,
            @SerializedName("height")
            var height: Int = 0,
            @SerializedName("iso_639_1")
            var iso6391: String = "",
            @SerializedName("file_path")
            var filePath: String = "",
            @SerializedName("vote_average")
            var voteAverage: Double = 0.0,
            @SerializedName("vote_count")
            var voteCount: Int = 0,
            @SerializedName("width")
            var width: Int = 0,
        ) : Parcelable
    }

    @Parcelize
    data class Videos(
        @SerializedName("results")
        var results: List<VideoResult> = emptyList(),
    ) : Parcelable {
        @Parcelize
        data class VideoResult(
            @SerializedName("iso_639_1")
            var iso6391: String = "",
            @SerializedName("iso_3166_1")
            var iso31661: String = "",
            @SerializedName("name")
            var name: String = "",
            @SerializedName("key")
            var key: String = "",
            @SerializedName("site")
            var site: String = "",
            @SerializedName("size")
            var size: Int = 0,
            @SerializedName("type")
            var type: String = "",
            @SerializedName("official")
            var official: Boolean?,
            @SerializedName("published_at")
            var publishedAt: String = "",
            @SerializedName("id")
            var id: String = "",
        ) : Parcelable
    }
}