package com.example.cardslist.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import cardslist.R
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import cardslist.databinding.ActivityMainBinding
import com.example.cardslist.data.model.Card
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<CardViewModel>()

    private val cardAdapter = CardAdapter(emptyList<Card>().toMutableList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpRecyclerView()
        subscribeUi()
    }

    private fun subscribeUi() {

        lifecycleScope.launch {
            viewModel.state
                .filter { !it.cardList.isNullOrEmpty() }
                .distinctUntilChanged()
                .collectLatest {
                    it.cardList?.let {
                            cards -> cardAdapter.updateData(cards)
                            binding.loading.visibility = View.GONE
                            binding.recyclerview.visibility = View.VISIBLE
                            binding.error.visibility = View.GONE
                    }
            }
        }

        lifecycleScope.launch {
            viewModel.state
                .filter { it.inProgress == true }
                .distinctUntilChanged()
                .collectLatest {
                        binding.loading.visibility = View.VISIBLE
                        binding.recyclerview.visibility = View.GONE
                }
        }

        lifecycleScope.launch {
            viewModel.state
                .filter { !it.errorMessage.isNullOrEmpty() && it.cardList.isNullOrEmpty() }
                .distinctUntilChanged()
                .collectLatest {
                    binding.error.apply {
                        text = (it.errorMessage + "\n Click to try again")
                        visibility = View.VISIBLE
                        setOnClickListener {
                            viewModel.fetchCards()
                        }
                    }
                    binding.loading.visibility = View.GONE
                    binding.recyclerview.visibility = View.GONE
                }
        }

    }


    private fun setUpRecyclerView() {
        binding.recyclerview.setHasFixedSize(true)
        val dividerItemDecoration = DividerItemDecoration(
            this, LinearLayoutManager.VERTICAL
        )
        val verticalDivider = ContextCompat.getDrawable(this, R.drawable.divisor)

        if (verticalDivider != null) {
            dividerItemDecoration.setDrawable(verticalDivider)
        }
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.adapter = cardAdapter
        binding.recyclerview.addItemDecoration(dividerItemDecoration)
    }
}
