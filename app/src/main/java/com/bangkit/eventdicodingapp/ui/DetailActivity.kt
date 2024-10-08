package com.bangkit.eventdicodingapp.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bangkit.eventdicodingapp.R
import com.bangkit.eventdicodingapp.data.response.Event
import com.bangkit.eventdicodingapp.data.response.EventDetailResponse
import com.bangkit.eventdicodingapp.data.retrofit.ApiConfig
import com.bangkit.eventdicodingapp.databinding.ActivityDetailBinding
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

class DetailActivity : AppCompatActivity() {


    private lateinit var binding: ActivityDetailBinding

    companion object {
        const val TAG = "DetailActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eventId = intent.getIntExtra("EVENT_ID", 1)

        findEventDetail(eventId)
    }

    private fun findEventDetail(id: Int) {
        val client = ApiConfig.getApiService().getEventDetail(id)
        client.enqueue(object : Callback<EventDetailResponse> {
            override fun onResponse(
                call: Call<EventDetailResponse>,
                response: Response<EventDetailResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setEventData(responseBody.event)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<EventDetailResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
    private fun setEventData(event: Event) {
        binding.tvEventDetail.text = event.name
    }

}