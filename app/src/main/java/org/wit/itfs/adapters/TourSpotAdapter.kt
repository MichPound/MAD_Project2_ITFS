package org.wit.itfs.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.wit.itfs.databinding.CardTourSpotBinding
import org.wit.itfs.models.TourSpotModel

interface TourSpotListener {
    fun onTourSpotClick(tourSpot: TourSpotModel)
}

class TourSpotAdapter constructor(private var tourSpots: List<TourSpotModel>, private val listener: TourSpotListener) :
    RecyclerView.Adapter<TourSpotAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardTourSpotBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val tourSpot = tourSpots[holder.adapterPosition]
        holder.bind(tourSpot, listener)
    }

    override fun getItemCount(): Int = tourSpots.size

    class MainHolder(private val binding : CardTourSpotBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tourSpot: TourSpotModel, listener: TourSpotListener) {
            binding.tourSpotTitle.text = tourSpot.title
//            binding.tourSpotDescription.text = tourSpot.desc
            Picasso.get()
                .load(tourSpot.image)
                .into(binding.tourSpotImage)
            binding.root.setOnClickListener { listener.onTourSpotClick(tourSpot) }
        }
    }
}