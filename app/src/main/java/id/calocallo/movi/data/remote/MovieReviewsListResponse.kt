package id.calocallo.movi.data.remote

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import id.calocallo.movi.utils.BaseEquatable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieReviewsListResponse(
    @SerializedName("reviews") var reviews: MovieReviews?,
) : Parcelable {
    @Parcelize
    data class MovieReviews(
        @SerializedName("page") var page: Int = 0,
        @SerializedName("results") var results: List<MovieReviewsResult> = emptyList(),
        @SerializedName("total_pages") var totalPages: Int = 0,
        @SerializedName("total_results") var totalResults: Int = 0,
    ) : Parcelable {
        @Parcelize
        data class MovieReviewsResult(
            @SerializedName("author") var author: String = "",
            @SerializedName("author_details") var authorDetails: AuthorDetails = AuthorDetails(),
            @SerializedName("content") var content: String = "",
            @SerializedName("created_at") var createdAt: String = "",
            @SerializedName("id") var id: String = "",
            @SerializedName("updated_at") var updatedAt: String = "",
            @SerializedName("url") var url: String = "",
        ) : Parcelable, BaseEquatable(id) {
            @Parcelize
            data class AuthorDetails(
                @SerializedName("name") var name: String = "",
                @SerializedName("username") var username: String = "",
                @SerializedName("avatar_path") var avatarPath: String = "",
                @SerializedName("rating") var rating: Double = 0.0,
            ) : Parcelable
        }
    }
}