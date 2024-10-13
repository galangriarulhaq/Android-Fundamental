package com.bangkit.eventdicodingapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.eventdicodingapp.R
import com.bangkit.eventdicodingapp.data.response.EventResponse
import com.bangkit.eventdicodingapp.data.response.ListEventsItem
import com.bangkit.eventdicodingapp.data.retrofit.ApiConfig
import com.bangkit.eventdicodingapp.databinding.ActivitySearchBinding
import com.bangkit.eventdicodingapp.ui.adapter.EventLargeAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    companion object {
        const val TAG = "SearchActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        supportActionBar?.hide()

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvEventSearch.layoutManager = layoutManager

        binding.tfSearch.editText?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || event?.keyCode == KeyEvent.KEYCODE_ENTER) {
                val query = binding.tfSearch.editText?.text.toString()
                fetchEventSearch(query)
                true
            } else {
                false
            }
        }

    }

    private fun fetchEventSearch(query: String) {
        val client = ApiConfig.getApiService().getEventSearch(query)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setEventDataSearch(responseBody.listEvents)
                    }
                } else {
                    Log.e(SearchActivity.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                Log.e(SearchActivity.TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setEventDataSearch(listEvent: List<ListEventsItem>) {
        val adapter = EventLargeAdapter(onItemClick = { eventId -> navigateToDetail(eventId) })
        adapter.submitList(listEvent)
        binding.rvEventSearch.adapter = adapter
    }

    private fun navigateToDetail(eventId: Int) {
        val intent = Intent(this@SearchActivity, DetailActivity::class.java).apply {
            putExtra("EVENT_ID", eventId)
        }
        startActivity(intent)
    }

}