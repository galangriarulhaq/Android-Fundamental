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
import com.bangkit.eventdicodingapp.data.remote.response.Event
import com.bangkit.eventdicodingapp.databinding.ActivityDetailBinding
import com.bangkit.eventdicodingapp.ui.model.DetailViewModel
import com.bumptech.glide.Glide
class DetailActivity : AppCompatActivity() {


    private lateinit var binding: ActivityDetailBinding

    private val detailViewModel by viewModels<DetailViewModel>()



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
    private fun setEventData(event: Event) {

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
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}