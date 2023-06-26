package id.calocallo.movi.network

data class Resource<T>(
    val isLoading: Boolean = false,
    val data: T? = null,
    val error: String? = null,
)