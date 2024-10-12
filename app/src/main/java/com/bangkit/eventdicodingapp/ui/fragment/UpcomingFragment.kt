package com.bangkit.eventdicodingapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.eventdicodingapp.data.response.ListEventsItem
import com.bangkit.eventdicodingapp.databinding.FragmentUpcomingBinding
import com.bangkit.eventdicodingapp.ui.DetailActivity
import com.bangkit.eventdicodingapp.ui.adapter.EventLargeAdapter
import com.bangkit.eventdicodingapp.ui.model.UpcomingViewModel

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val upcomingViewModel by viewModels<UpcomingViewModel>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvEvent.layoutManager = layoutManager

        upcomingViewModel.listEvent.observe(viewLifecycleOwner, Observer { eventList ->
            setEventData(eventList)
        })

        upcomingViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {errorMessage ->
                Toast.makeText(requireActivity(), errorMessage.toString(), Toast.LENGTH_SHORT).show()
            }
        })

        upcomingViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            showLoading(isLoading)
        })

        return root
    }

    private fun setEventData(listEvent: List<ListEventsItem>) {
        val adapter = EventLargeAdapter(onItemClick = { eventId -> navigateToDetail(eventId)})
        adapter.submitList(listEvent)
        binding.rvEvent.adapter = adapter
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