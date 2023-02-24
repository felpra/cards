package com.example.cardslist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cardslist.databinding.CarditemBinding
import coil.api.load
import com.example.cardslist.data.model.Card
import android.content.Intent
import com.example.cardslist.ui.details.DetailsActivity

class CardAdapter(
    private var cardArrayList: MutableList<Card>
) :
    RecyclerView.Adapter<CardAdapter.RecyclerViewViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewViewHolder {

        val binding = CarditemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecyclerViewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerViewViewHolder, position: Int) {
        with(holder){
            with(cardArrayList[position]){
                binding.title.text = name
                binding.description.text = flavor
                binding.image.load(img)
            }
            this.itemView.apply {
                setOnClickListener {
                    context.startActivity(
                        Intent(context, DetailsActivity::class.java)
                            .putExtra("card", cardArrayList[position])
                    )
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return cardArrayList.size
    }

    inner class RecyclerViewViewHolder(val binding: CarditemBinding) :
        RecyclerView.ViewHolder(binding.root)


    fun updateData(list: List<Card>) {
        cardArrayList.clear()
        cardArrayList.addAll(list)
        notifyDataSetChanged()
    }
}
