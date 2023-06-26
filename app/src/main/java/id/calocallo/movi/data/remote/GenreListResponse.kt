package id.calocallo.movi.data.remote

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GenreListResponse(
    @SerializedName("genres")
    var genres: List<Genre>,
) : Parcelable {
    @Parcelize
    data class Genre(
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("name")
        var name: String = "",
    ) : Parcelable
}
