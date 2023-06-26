package id.calocallo.movi.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.calocallo.movi.data.MoviRepository
import id.calocallo.movi.data.remote.MovieReviewsListResponse
import id.calocallo.movi.network.Resource
import kotlinx.coroutines.launch

class MovieReviewsViewModel(private val repository: MoviRepository) : ViewModel() {
    val movieReviewsList: MutableLiveData<Resource<MovieReviewsListResponse.MovieReviews>> =
        MutableLiveData(Resource(isLoading = false, data = null, error = null))

    fun requestGetUserReviews(movieId: String, page: String) {
        viewModelScope.launch {
            movieReviewsList.postValue(movieReviewsList.value?.copy(isLoading = true))

            try {
                val dataMovieReviews = repository.getMovieReviewsList(movieId, page)
                movieReviewsList.postValue(
                    movieReviewsList.value?.copy(
                        isLoading = false,
                        data = dataMovieReviews,
                    ),
                )
            } catch (e: Exception) {
                movieReviewsList.postValue(
                    movieReviewsList.value?.copy(
                        isLoading = false,
                        error = e.message,
                    ),
                )
            }
        }
    }
}