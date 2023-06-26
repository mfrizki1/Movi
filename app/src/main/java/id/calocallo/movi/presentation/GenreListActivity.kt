package id.calocallo.movi.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import id.calocallo.movi.R
import id.calocallo.movi.data.remote.GenreListResponse
import id.calocallo.movi.databinding.ActivityGenreListBinding
import id.calocallo.movi.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class GenreListActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityGenreListBinding::inflate)

    private val viewModel: GenreViewModel by viewModel()

    private var genreAdapter = GenreAdapter()

    private var tempGenre = GenreListResponse.Genre()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
        observeData()
    }

    private fun setupView() {
        binding.apply {
            handlingButton()

            viewModel.requestGetGenreList()
        }
    }

    private fun handlingButton() {
        binding.apply {
            fabGenreNext.apply {
                if (tempGenre.name.isEmpty()) {
                    isEnabled = false
                    setBackgroundColor(getColor(R.color.grey_disabled))
                } else {
                    isEnabled = true
                }

                setOnClickListener {
                    val bundle = bundleOf(Constant.GENRE_DATA to tempGenre)
                    intentTo(MoviesListActivity::class.java.name, bundle)
                }
            }
        }
    }

    private fun observeData() {
        viewModel.genreList.observe(this) { result ->
            binding.apply {
                when {
                    result.isLoading -> {
                        pageLoading()
                    }
                    result.data?.isNotEmpty() == true -> {
                        pageData()
                        genreAdapter.setData(result.data)
                        rvGenreList.adapter = genreAdapter
                        genreAdapter.onClick { genre ->
                            tempGenre = genre
                            handlingButton()
                        }
                    }
                    result.error?.isNotEmpty() == true -> {
                        pageError()

                        val errorTitle = when {
                            result.error.contains("No address associated", ignoreCase = true) -> {
                                getString(R.string.no_connection)
                            }
                            result.error.contains("timeout", ignoreCase = true) -> {
                                getString(R.string.no_connection)
                            }
                            else -> {
                                "Unknown Error"
                            }
                        }

                        layoutGenreListErrorPage.apply {
                            tvErrorPageTitle.text = errorTitle
                            btnErrorPage.setOnClickListener {
                                btnErrorPage.setOnClickListener {
                                    viewModel.requestGetGenreList()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun pageLoading() {
        binding.apply {
            pbGenre.root.visible()
            rvGenreList.gone()
            layoutGenreListErrorPage.root.gone()
        }
    }

    private fun pageData() {
        binding.apply {
            pbGenre.root.gone()
            rvGenreList.visible()
            layoutGenreListErrorPage.root.gone()
        }
    }

    private fun pageError() {
        binding.apply {
            pbGenre.root.gone()
            rvGenreList.gone()
            layoutGenreListErrorPage.root.visible()
        }
    }
}
