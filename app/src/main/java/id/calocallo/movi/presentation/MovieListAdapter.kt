package id.calocallo.movi.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.calocallo.movi.BuildConfig
import id.calocallo.movi.data.remote.MovieListResponse
import id.calocallo.movi.databinding.ActivityMoviesListItemBinding
import id.calocallo.movi.utils.convertStringToDate
import id.calocallo.movi.utils.loadImage
import id.calocallo.movi.utils.orNol

class MovieListAdapter() :
    RecyclerView.Adapter<MovieListAdapter.MovieListHolder>() {

    private var movieList: MutableList<MovieListResponse.MoviesResponse> = mutableListOf()

    lateinit var onClick: (MovieListResponse.MoviesResponse) -> Unit
    fun onClick(mOnClick: (MovieListResponse.MoviesResponse) -> Unit) {
        onClick = mOnClick
    }

    inner class MovieListHolder(private val binding: ActivityMoviesListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MovieListResponse.MoviesResponse) {
            binding.apply {
                var imageUrl = ""
                if (!data.posterPath.isNullOrEmpty()) {
                    imageUrl = BuildConfig.SMALL_IMAGE_URL + data.posterPath
                }
                ivMoviesListItem.loadImage(imageUrl)
                tvMoviesListItemTitle.text = data.title
                tvMovieListItemRating.text = data.voteAverage.orNol().toString()
                tvMoviesListItemYear.text = convertStringToDate(
                    dateTime = data.releaseDate,
                    inputDateFormat = "yyyy-MM-dd",
                    outputDateFormat = "yyyy",
                )

                root.setOnClickListener {
                    onClick(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListHolder {
        val binding = ActivityMoviesListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return MovieListHolder(binding)
    }

    override fun getItemCount(): Int = movieList.size

    override fun onBindViewHolder(holder: MovieListHolder, position: Int) {
        holder.bind(movieList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<MovieListResponse.MoviesResponse>) {
        movieList = data.toMutableList()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataPagination(moviesResponse: List<MovieListResponse.MoviesResponse>) {
        this.movieList.addAll(moviesResponse)
        notifyDataSetChanged()
    }
}