package com.example.asaankissan.activities


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.asaankissan.R
import com.example.asaankissan.databinding.ActivityUserGuideBinding
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView

class UserGuideActivity : AppCompatActivity() {
    private var binding : ActivityUserGuideBinding? = null

    //val VIDEO_LINK="https://www.youtube.com/embed/8BOrBW6Amd4"
    val VIDEO_LINK="https://www.youtube.com/embed/5RTOly4fr0U"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserGuideBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.webView?.settings?.javaScriptEnabled = true
        binding?.webView?.loadUrl(VIDEO_LINK)

    }
}