package id.calocallo.movi.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import id.calocallo.movi.R
import id.calocallo.movi.data.remote.GenreListResponse
import id.calocallo.movi.databinding.ActivityGenreItemBinding

class GenreAdapter : RecyclerView.Adapter<GenreAdapter.GenreHolder>() {

    private var genreList = emptyList<GenreListResponse.Genre>()

    private var selectedItemPosition = -1

    lateinit var isSelectedGenre: (GenreListResponse.Genre) -> Unit
    fun onClick(mIsSelectedGenre: (GenreListResponse.Genre) -> Unit) {
        isSelectedGenre = mIsSelectedGenre
    }

    inner class GenreHolder(private val binding: ActivityGenreItemBinding) : ViewHolder(binding.root) {
        fun bind(genre: GenreListResponse.Genre) {
            binding.apply {
                val ctx = binding.root.context

                tvGenreItemTitle.text = genre.name
                cvGenreItem.setOnClickListener {
                    val previousItem = selectedItemPosition
                    selectedItemPosition = bindingAdapterPosition
                    notifyItemChanged(previousItem)
                    notifyItemChanged(selectedItemPosition)

                    isSelectedGenre(genre)
                }

                if (selectedItemPosition == bindingAdapterPosition) {
                    cvGenreItem.apply {
                        isSelected = true
                        setBackgroundColor(ctx.getColor(R.color.teal_700))
                        tvGenreItemTitle.setTextColor(ctx.getColor(R.color.white))
                    }
                } else {
                    cvGenreItem.apply {
                        isSelected = false
                        setBackgroundColor(ctx.getColor(R.color.white))
                        tvGenreItemTitle.setTextColor(ctx.getColor(R.color.black))
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreHolder {
        val binding =
            ActivityGenreItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GenreHolder(binding)
    }

    override fun getItemCount(): Int = genreList.size

    override fun onBindViewHolder(holder: GenreHolder, position: Int) {
        holder.bind(genreList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(genreList: List<GenreListResponse.Genre>) {
        this.genreList = genreList
        notifyDataSetChanged()
    }
}