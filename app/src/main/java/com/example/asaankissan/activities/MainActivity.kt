package com.example.asaankissan.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.asaankissan.R
import com.example.asaankissan.databinding.ActivityMainBinding
import com.example.asaankissan.firebase.FirebaseClass
import com.example.asaankissan.fragments.AccountFragment
import com.example.asaankissan.fragments.ExploreFragment
import com.example.asaankissan.fragments.MyAdsFragment
import com.example.asaankissan.fragments.SellFragment
import com.example.asaankissan.models.User
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var userData: User
    private var profileKey: String? = null

    override fun onRestart() {
        auth = FirebaseAuth.getInstance()
        super.onRestart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        auth = FirebaseAuth.getInstance()
        FirebaseClass()

        selectFragment()
        //by default load Explore fragment
        loadFragment(ExploreFragment())

        //load Account fragment if comes from the AdPostActvity for setting up mobile number and city
        profileKey = intent.getStringExtra("profileKey")
        //Toast.makeText(this, "found: $profileKey", Toast.LENGTH_SHORT).show()
        if (profileKey == "1"){
            loadFragment(AccountFragment())
            profileKey = "0"
        }else{
            loadFragment(ExploreFragment())
        }
    }

    private fun selectFragment() {

        binding?.bottomNav?.setOnItemSelectedListener {
            when(it.itemId){
                R.id.actionExplore -> loadFragment(ExploreFragment())
                R.id.actionSell -> loadFragment(SellFragment())
                R.id.actionMyAds -> loadFragment(MyAdsFragment())
                R.id.actionAccount -> loadFragment(AccountFragment())
            }
            true
        }
    }
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commitAllowingStateLoss()
    }

    override fun onDestroy() {
        if (binding != null) {
            binding = null
        }
        super.onDestroy()
    }
}