package id.calocallo.movi.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.calocallo.movi.data.MoviRepository
import id.calocallo.movi.data.remote.GenreListResponse
import id.calocallo.movi.data.remote.MovieListResponse
import id.calocallo.movi.network.Resource
import kotlinx.coroutines.launch

class GenreViewModel(private val repository: MoviRepository) : ViewModel() {
    val genreList: MutableLiveData<Resource<List<GenreListResponse.Genre>>> =
        MutableLiveData(Resource(isLoading = false, data = null, error = null))

    fun requestGetGenreList() {
        viewModelScope.launch {
            genreList.postValue(genreList.value?.copy(isLoading = true))

            try {
                val dataGenreList = repository.getGenreList()
                genreList.postValue(genreList.value?.copy(isLoading = false, data = dataGenreList))
            } catch (e: Exception) {
                genreList.postValue(genreList.value?.copy(isLoading = false, error = e.message))
            }
        }
    }
}
