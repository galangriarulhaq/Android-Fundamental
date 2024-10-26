package com.bangkit.eventdicodingapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bangkit.eventdicodingapp.data.remote.response.EventDetailResponse
import com.bangkit.eventdicodingapp.databinding.ActivityDetailBinding
import com.bangkit.eventdicodingapp.ui.factory.DetailModelFactory
import com.bangkit.eventdicodingapp.ui.model.DetailViewModel
import com.bumptech.glide.Glide
class DetailActivity : AppCompatActivity() {


    private lateinit var binding: ActivityDetailBinding

    private val detailViewModel by viewModels<DetailViewModel>(){
        DetailModelFactory.getInstance(this)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val eventId = intent.getIntExtra("EVENT_ID", 1)

        detailViewModel.fetchDetailEvent(eventId)

        detailViewModel.eventDetail.observe(this) { event ->
            setEventData(event)
        }

        detailViewModel.errorMessage.observe(this) {
            it.getContentIfNotHandled()?.let {errorMessage ->
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        detailViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

    }


    @SuppressLint("SetTextI18n")
    private fun setEventData(eventDetail: EventDetailResponse) {

        val event = eventDetail.event

        supportActionBar?.title = event.name

        Glide.with(binding.root.context)
            .load(event.imageLogo)
            .into(binding.imgEvent)
        binding.tvTitleEvent.text = event.name
        binding.tvSummaryEvent.text = event.summary
        binding.tvBeginTime.text = event.beginTime
        val remainingQuota = event.quota - event.registrants
        binding.tvRemainingQuota.text = "Sisa Kuota: $remainingQuota "
        binding.tvCategory.text = event.category
        binding.tvOwner.text = "Oleh : ${event.ownerName}"
        binding.tvDescription.text = HtmlCompat.fromHtml(
            event.description,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        binding.btnRegister.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(event.link)

            startActivity(intent)
        }

    }

    private fun showLoading(isLoading: Boolean) {

        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.btnRegister.visibility = View.GONE
            binding.tvInformation.visibility = View.GONE
            binding.cardCategory.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.btnRegister.visibility = View.VISIBLE
            binding.tvInformation.visibility = View.VISIBLE
            binding.cardCategory.visibility = View.VISIBLE
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}