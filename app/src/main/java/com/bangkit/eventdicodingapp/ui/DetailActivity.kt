package com.bangkit.eventdicodingapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bangkit.eventdicodingapp.R
import com.bangkit.eventdicodingapp.data.response.Event
import com.bangkit.eventdicodingapp.data.response.EventDetailResponse
import com.bangkit.eventdicodingapp.data.retrofit.ApiConfig
import com.bangkit.eventdicodingapp.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide
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
        setContentView(R.layout.activity_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eventId = intent.getIntExtra("EVENT_ID", 1)

        fetchEventDetail(eventId)
    }

    private fun fetchEventDetail(id: Int) {
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
    @SuppressLint("SetTextI18n")
    private fun setEventData(event: Event) {

        supportActionBar?.title = event.name

        Glide.with(binding.root.context)
            .load(event.imageLogo)
            .into(binding.imgEvent)
        binding.tvTitleEvent.text = event.name
        binding.tvSummaryEvent.text = event.summary
        binding.tvBeginTime.text = event.beginTime
        val remainingQuota = event.quota - event.registrants
        binding.tvRemainingQuota.text = "Sisa Kuota: ${remainingQuota.toString()} "
        binding.tvCategory.text = event.category
        binding.tvOwner.text = "Oleh : ${event.ownerName}"
        binding.tvDescription.text = HtmlCompat.fromHtml(
            event.description.toString(),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        binding.btnRegister.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(event.link)

            startActivity(intent)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}