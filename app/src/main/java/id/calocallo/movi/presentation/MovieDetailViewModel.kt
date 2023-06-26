package id.calocallo.movi.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.calocallo.movi.data.MoviRepository
import id.calocallo.movi.data.remote.MovieDetailResponse
import id.calocallo.movi.network.Resource
import kotlinx.coroutines.launch

class MovieDetailViewModel(private val repository: MoviRepository) : ViewModel() {
    val movieDetail: MutableLiveData<Resource<MovieDetailResponse>> =
        MutableLiveData(Resource(isLoading = false, data = null, error = null))

    fun requestMovieDetail(movieId: String, appendToResponse: String) {
        viewModelScope.launch {
            movieDetail.postValue(movieDetail.value?.copy(isLoading = true))

            try {
                val dataMovieDetail = repository.getMovieDetail(movieId, appendToResponse)
                movieDetail.postValue(
                    movieDetail.value?.copy(
                        isLoading = false,
                        data = dataMovieDetail,
                    ),
                )
            } catch (e: Exception) {
                movieDetail.postValue(movieDetail.value?.copy(isLoading = false, error = e.message))
            }
        }
    }
}