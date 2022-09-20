package com.example.asaankissan.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.asaankissan.adapters.ImageSliderAdapter
import com.example.asaankissan.databinding.ActivityAdDetailImagesBinding
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations

class AdDetailImagesActivity : AppCompatActivity() {
    var binding: ActivityAdDetailImagesBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdDetailImagesBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // getting images list

        val imagesList = intent.getStringArrayListExtra("imageslist")
        setUpImageSlider(imagesList!!)
    }

    private fun setUpImageSlider(imagesList: ArrayList<String>) {
        val sliderAdapter = ImageSliderAdapter(this, imagesList)
        binding?.imageSlider?.setSliderAdapter(sliderAdapter)
        binding?.imageSlider?.setIndicatorAnimation(IndicatorAnimationType.SWAP)
        binding?.imageSlider?.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION)
        //binding?.imageSlider?.startAutoCycle()
    }
}