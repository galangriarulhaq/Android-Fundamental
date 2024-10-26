package com.bangkit.eventdicodingapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.eventdicodingapp.data.local.entity.EventEntity
import com.bangkit.eventdicodingapp.databinding.FragmentHomeBinding
import com.bangkit.eventdicodingapp.ui.DetailActivity
import com.bangkit.eventdicodingapp.ui.adapter.EventLargeAdapter
import com.bangkit.eventdicodingapp.ui.adapter.EventSmallAdapter
import com.bangkit.eventdicodingapp.ui.factory.HomeModelFactory
import com.bangkit.eventdicodingapp.ui.model.HomeViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val homeViewModel by viewModels<HomeViewModel>(){
        HomeModelFactory.getInstance(requireActivity())
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val layoutManagerUpcoming = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvEventUpcoming.layoutManager = layoutManagerUpcoming
        val layoutManagerFinished = LinearLayoutManager(requireActivity())
        binding.rvEventFinished.layoutManager = layoutManagerFinished


        homeViewModel.listEventUpcoming.observe(viewLifecycleOwner) {eventListUpcoming ->
            setEventDataUpcoming(eventListUpcoming)
        }

        homeViewModel.listEventFinished.observe(viewLifecycleOwner) { eventListFinished ->
            setEventDataFinished(eventListFinished)
        }

//        homeViewModel.errorMessage.observe(viewLifecycleOwner) {
//            it.getContentIfNotHandled()?.let {errorMessage ->
//                Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show()
//            }
//        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        return root
    }

    private fun setEventDataUpcoming(listEvent: List<EventEntity>) {
        val adapter = EventSmallAdapter(
            onItemClick = { eventId -> navigateToDetail(eventId)},
            onFavoriteClick = { event ->
                viewLifecycleOwner.lifecycleScope.launch {
                    toggleFavorite(event)
                }
            })
        adapter.submitList(listEvent.take(5))
        binding.rvEventUpcoming.adapter = adapter
    }

    private fun setEventDataFinished(listEvent: List<EventEntity>) {
        val adapter = EventLargeAdapter(
            onItemClick = { eventId -> navigateToDetail(eventId) },
            onFavoriteClick = { event ->
                viewLifecycleOwner.lifecycleScope.launch {
                    toggleFavorite(event)
                }
            })
        adapter.submitList(listEvent.take(5))
        binding.rvEventFinished.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private suspend fun toggleFavorite(event: EventEntity) {
        if (event.isFavorite) {
            homeViewModel.deleteEvent(event)
        } else {
            homeViewModel.saveEvent(event)
        }
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