package com.example.cardslist.ui.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import cardslist.databinding.ActivityDetailsBinding
import coil.api.load
import com.example.cardslist.data.model.Card
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(intent.getParcelableExtra<Card>("card")) {
           if(this != null)
               populateView(this)
            else {
                binding.error.visibility = View.VISIBLE
               binding.detailsList.visibility = View.GONE
           }
        }
    }

    private fun populateView(card: Card){
        card
            .apply {
                binding.attack.text = attack.toString()
                binding.cost.text = cost.toString()
                binding.health.text = health.toString()
                binding.flavor.text = flavor
                binding.faction.text = faction
                binding.name.text = name
                binding.rarity.text = rarity
                binding.text.text = text
                binding.set.text = cardSet
                binding.image.load(img)
            }
    }


}