package id.calocallo.movi.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.calocallo.movi.data.MoviRepository
import id.calocallo.movi.data.remote.MovieListResponse
import id.calocallo.movi.network.Resource
import kotlinx.coroutines.launch

class MovieListViewModel(private val repository: MoviRepository) : ViewModel() {
    val movieList: MutableLiveData<Resource<MovieListResponse>> =
        MutableLiveData(Resource(isLoading = false, data = null, error = null))

    fun requestGetMovieList(refresh: Boolean = false, page: Int, genres: String) {
        viewModelScope.launch {
            movieList.postValue(movieList.value?.copy(isLoading = true))

            try {
                val dataMovieList = repository.getMovieList(refresh, page, genres)
                movieList.postValue(movieList.value?.copy(isLoading = false, data = dataMovieList))
            } catch (e: Exception) {
                movieList.postValue(movieList.value?.copy(isLoading = false, error = e.message))
            }
        }
    }
}