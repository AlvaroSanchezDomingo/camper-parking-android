package org.wit.parking.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.wit.parking.databinding.CardParkingBinding
import org.wit.parking.models.ParkingModel
import timber.log.Timber


interface ParkingListener {
    fun onParkingClick(parking: ParkingModel)
}

class ParkingAdapter constructor(private var parkings: List<ParkingModel>, private val listener: ParkingListener) :
    RecyclerView.Adapter<ParkingAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardParkingBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val parking = parkings[holder.adapterPosition]
        holder.bind(parking, listener)
    }

    override fun getItemCount(): Int = parkings.size

    class MainHolder(private val binding : CardParkingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(parking: ParkingModel, listener: ParkingListener) {
            binding.parkingTitle.text = parking.title
            binding.description.text = parking.description
            binding.username.text = parking.username
            Timber.i("image in adapter: ${parking.image}")
            Picasso.get()
                .load(parking.image)
                .resize(200,200)
                .into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onParkingClick(parking) }
        }
    }
}