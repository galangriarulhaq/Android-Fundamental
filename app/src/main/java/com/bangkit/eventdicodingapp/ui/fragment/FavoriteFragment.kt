package com.bangkit.eventdicodingapp.ui.fragment

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.eventdicodingapp.data.local.entity.EventEntity
import com.bangkit.eventdicodingapp.databinding.FragmentFavoriteBinding
import com.bangkit.eventdicodingapp.ui.DetailActivity
import com.bangkit.eventdicodingapp.ui.adapter.EventLargeAdapter
import com.bangkit.eventdicodingapp.ui.factory.FavoriteModelFactory
import com.bangkit.eventdicodingapp.ui.model.FavoriteViewModel
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null

    private val favoriteViewModel by viewModels<FavoriteViewModel>{
        FavoriteModelFactory.getInstance(requireActivity())
    }

    private val binding get() = _binding!!

    private lateinit var eventFavoriteAdapter: EventLargeAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupEventFavorite()
        viewLifecycleOwner.lifecycleScope.launch {
            observeViewModels()
        }
        return root
    }

    private suspend fun observeViewModels() {
        favoriteViewModel.getEventFavorite().observe(viewLifecycleOwner) {eventList ->
            if (eventList.isEmpty()) {
                eventFavoriteAdapter.submitList(emptyList())
            } else {
                eventFavoriteAdapter.submitList(eventList)
            }
        }
        favoriteViewModel.isLoading.observe(viewLifecycleOwner) {isLoading ->
            showLoading(isLoading)
        }

    }

    private fun setupEventFavorite() {
        val rvEvent = binding.rvFavorite
        rvEvent.layoutManager = LinearLayoutManager(requireContext())
        eventFavoriteAdapter = EventLargeAdapter(
            onFavoriteClick = { event ->
                viewLifecycleOwner.lifecycleScope.launch {
                    toggleFavorite(event)
                }
            },
            onItemClick = { eventId -> navigateToDetail(eventId) }
        )
        rvEvent.adapter = eventFavoriteAdapter
    }

    private suspend fun toggleFavorite(event: EventEntity) {
        if (event.isFavorite) {
            favoriteViewModel.deleteEvent(event)
        } else {
            favoriteViewModel.saveEvent(event)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun navigateToDetail(eventId: Int) {
        val intent = Intent(requireContext(), DetailActivity::class.java).apply {
            putExtra("EVENT_ID", eventId)
        }
        startActivity(intent)
    }

}