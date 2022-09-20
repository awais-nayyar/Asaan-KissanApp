package com.example.asaankissan.activities

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import androidx.appcompat.app.AppCompatActivity
import com.example.asaankissan.R
import com.example.asaankissan.adapters.ImageSliderAdapter
import com.example.asaankissan.databinding.ActivityAdsDetailBinding
import com.example.asaankissan.models.Ad
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import org.ocpsoft.prettytime.PrettyTime
import java.net.URLEncoder
import java.util.*

class AdsDetailActivity : AppCompatActivity() {

    var binding : ActivityAdsDetailBinding? = null
    private lateinit var ad : Ad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdsDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setUpToolBar()

        // getting ads intent sent from previous activities/fragment
        ad = intent.getSerializableExtra("ad") as Ad
        val imagesList : ArrayList<String> = ad.adImage
        setUpImageSlider(imagesList)

        // setting all details on views
        setUpAdDetails(ad)

        // full screen images of ads
        binding?.ivAdDetail?.setOnClickListener{
            val detailImageIntent = Intent(this, AdDetailImagesActivity::class.java)
            detailImageIntent.putExtra("imageslist", imagesList)
            startActivity(detailImageIntent)
        }

        // setting call button
        setUpCallButton()

        // setting Message button
        setUpMessageButton()

        // setting WhatsApp button
        setUpWhatsAppButton()
    }

    private fun setUpWhatsAppButton() {
        val message = "Hi! I am looking for ${ad.adTitle} near ${ad.adAddress}, ${ad.adLocation}"
        binding?.ibWhatsApp?.setOnClickListener {
            val url = "https://api.whatsapp.com/send?phone=${ad.adContactNumber}"+ "&text=" + URLEncoder.encode(message, "UTF-8")
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
    }

    private fun setUpMessageButton() {
        binding?.ibMessage?.setOnClickListener {

            val message =
                "Hi! I am looking for ${ad.adTitle} near ${ad.adAddress}, ${ad.adLocation}"
            val phone = "0${ad.adContactNumber}"

            val uri = Uri.parse("smsto:+$phone")
            val intent = Intent(Intent.ACTION_SENDTO, uri)

            with(intent) {
                putExtra("address", "+$phone")
                putExtra("sms_body", message)
            }

            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> {
                    val defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(this)
                    if (defaultSmsPackageName != null) intent.setPackage(defaultSmsPackageName)

                    startActivity(intent)
                }
                else -> startActivity(intent)
            }
        }
    }

    private fun setUpCallButton() {
        binding?.ibCall?.setOnClickListener{
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:0${ad.adContactNumber}")
            startActivity(intent)
        }
    }

    private fun setUpAdDetails(ad: Ad) {
        binding?.tvAdPrice?.text = "RS: " + ad.adPrice.toString()
        binding?.tvAdTitle?.text = ad.adTitle
        binding?.tvAdDescription?.text = ad.adDescription
        binding?.tvAdLocation?.text = ad.adLocation
        binding?.tvAdLocationAddress?.text = ad.adAddress
        val pt = PrettyTime()
        binding?.tvAdUploadTime?.text = pt.format(Date(ad.adUploadTime.toLong())).toString()
        binding?.tvAdType?.text = ad.adCategory
    }

    private fun setUpImageSlider(imagesList : ArrayList<String>) {
        val sliderAdapter = ImageSliderAdapter(this, imagesList)
        binding?.imageSlider?.setSliderAdapter(sliderAdapter)
        binding?.imageSlider?.setIndicatorAnimation(IndicatorAnimationType.WORM)
        binding?.imageSlider?.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION)
        //binding?.imageSlider?.startAutoCycle()
    }

    private fun setUpToolBar() {
        setSupportActionBar(binding?.toolBar)
        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            title = ""
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        }

        binding?.toolBar?.setNavigationOnClickListener{
            onBackPressed()
        }
    }
}