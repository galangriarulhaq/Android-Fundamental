package com.bangkit.eventdicodingapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.eventdicodingapp.data.response.ListEventsItem
import com.bangkit.eventdicodingapp.databinding.FragmentHomeBinding
import com.bangkit.eventdicodingapp.ui.DetailActivity
import com.bangkit.eventdicodingapp.ui.adapter.EventAdapter
import com.bangkit.eventdicodingapp.ui.adapter.EventFinishedAdapter
import com.bangkit.eventdicodingapp.ui.model.HomeViewModel
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val layoutManagerUpcoming = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvEventUpcoming.layoutManager = layoutManagerUpcoming
        val layoutManagerFinished = LinearLayoutManager(requireActivity())
        binding.rvEventFinished.layoutManager = layoutManagerFinished

        homeViewModel.listEventUpcoming.observe(viewLifecycleOwner, Observer { eventListUpcoming ->
            setEventDataUpcoming(eventListUpcoming)
        })

        homeViewModel.listEventFinished.observe(viewLifecycleOwner, Observer { eventListFinished ->
            setEventDataFinished(eventListFinished)
        })

        homeViewModel.snackbarText.observe(viewLifecycleOwner, Observer { snakbarText->
            snakbarText.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(
                    requireActivity().window.decorView.rootView,
                    snackBarText,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })

        homeViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            showLoading(isLoading)
        })

        return root
    }

    private fun setEventDataUpcoming(listEvent: List<ListEventsItem>) {
        val adapter = EventFinishedAdapter(onItemClick = { eventId -> navigateToDetail(eventId)})
        adapter.submitList(listEvent.take(5))
        binding.rvEventUpcoming.adapter = adapter
    }

    private fun setEventDataFinished(listEvent: List<ListEventsItem>) {
        val adapter = EventAdapter(onItemClick = { eventId -> navigateToDetail(eventId)})
        adapter.submitList(listEvent.take(5))
        binding.rvEventFinished.adapter = adapter
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