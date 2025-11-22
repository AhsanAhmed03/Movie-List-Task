package com.app.movies.list.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.movies.list.databinding.ActivityDetailsBinding
import com.app.movies.list.model.Movie
import com.bumptech.glide.Glide

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movie = intent.getParcelableExtra<Movie>("movie")
        movie?.let {
            binding.tvTitle.text = it.title
            binding.tvYear.text = it.release_date ?: ""
            binding.tvDescription.text = it.description ?: ""
            Glide.with(this).load(it.image).into(binding.imgPosterFull)
        }
    }
}