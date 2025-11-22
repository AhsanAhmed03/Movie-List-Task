package com.app.movies.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.movies.list.databinding.ItemMovieBinding
import com.app.movies.list.model.Movie
import com.bumptech.glide.Glide

class MoviesAdapter(
    private val onItemClicked: (Movie) -> Unit,
    private val onFavClicked: (String, Boolean) -> Unit
) : ListAdapter<Movie, MoviesAdapter.MovieVH>(Diff) {

    private var favIds: Set<String> = emptySet()

    fun submitFavorites(ids: Set<String>) {
        favIds = ids
        notifyDataSetChanged()
    }

    object Diff : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(old: Movie, new: Movie) = old.id == new.id
        override fun areContentsTheSame(old: Movie, new: Movie) = old == new
    }

    inner class MovieVH(val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieVH {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieVH(binding)
    }

    override fun onBindViewHolder(holder: MovieVH, position: Int) {
        val movie = getItem(position)
        with(holder.binding) {
            tvTitle.text = movie.title
            tvYear.text = movie.release_date ?: ""

            Glide.with(imgPoster.context)
                .load(movie.image)
                .into(imgPoster)

            val isFav = favIds.contains(movie.id)
            btnFav.setImageResource(if (isFav) R.drawable.ic_heart_filled else R.drawable.ic_heart_border)

            root.setOnClickListener { onItemClicked(movie) }
            btnFav.setOnClickListener { onFavClicked(movie.id, isFav) }
        }
    }
}