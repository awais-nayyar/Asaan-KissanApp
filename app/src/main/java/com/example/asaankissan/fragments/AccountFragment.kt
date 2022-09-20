package com.example.asaankissan.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.example.asaankissan.R
import com.example.asaankissan.activities.GoogleSignupActivity
import com.example.asaankissan.activities.UserGuideActivity
import com.example.asaankissan.databinding.FragmentAccountBinding
import com.example.asaankissan.databinding.ItemLayoutEditprofileDialogBinding
import com.example.asaankissan.firebase.FirebaseClass
import com.example.asaankissan.models.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import java.net.URLEncoder

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    //my variables start form here
    private var binding: FragmentAccountBinding? = null

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_account, container, false)
        binding = FragmentAccountBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        FirebaseClass().getCurrentUserDataForFragment(this)

        createRequest()

        binding?.btnLogout?.setOnClickListener {

            logoutUser()
        }

        // user guide button

        binding?.btnHowToUse?.setOnClickListener {
            startActivity(Intent(requireContext(), UserGuideActivity::class.java))
        }

        // shimmer effect

        showShimmer()

        // contact customer care
        contactCustomerCare()

    }

    private fun contactCustomerCare() {
        val message = "Hi! I need help, Are you here?"
        binding?.btnContactUs?.setOnClickListener {
            val url = "https://api.whatsapp.com/send?phone=03055599143"+ "&text=" + URLEncoder.encode(message, "UTF-8")
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
    }

    private fun showShimmer() {
        binding?.shimmerLayout?.visibility = View.VISIBLE
        binding?.shimmerLayout?.startShimmer()
        binding?.mainLayout?.visibility = View.INVISIBLE
    }

    private fun hideShimmer() {
        binding?.shimmerLayout?.visibility = View.INVISIBLE
        binding?.shimmerLayout?.stopShimmer()
        binding?.mainLayout?.visibility = View.VISIBLE
    }

    private fun createRequest() {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        auth = FirebaseAuth.getInstance()
    }

    private fun logoutUser() {
        googleSignInClient.signOut()
        auth.signOut()
        //to finish the fragment
        activity?.onBackPressed();
        startActivity(Intent(context, GoogleSignupActivity::class.java))

    }

    fun displayUserInfoIntoEditText(userInfo: User) {
        user = userInfo
        if (userInfo.imageUrl.isNotEmpty()) {
            Glide.with(requireContext()).load(userInfo.imageUrl)
                .into(binding?.ivProfile as ImageView)
        }
        binding?.tieName?.setText(userInfo.name)
        binding?.tiePhoneNumber?.setText(userInfo.phone)
        binding?.tieEmail?.setText(userInfo.email)
        binding?.tieCity?.setText(userInfo.city)

        hideShimmer()
        binding?.btnEditProfile?.setOnClickListener {
            editProfile(userInfo.imageUrl, userInfo.name, userInfo.phone, userInfo.city)
        }
    }

    fun userProfileUpdatedSuccessfully(userInfo: User) {
        hideShimmer()
        showToast("User Profile Updated Successfully!")
        displayUserInfoIntoEditText(userInfo)
    }

    fun userProfileNotUpdated() {
        showToast("Error while updating user profile")
    }

    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    private fun editProfile(
        ivProfile: String,
        etName: String,
        etPhoneNumber: String,
        etCity: String
    ) {

        val editProfileDialog = Dialog(requireContext())
        val dialogBinding = ItemLayoutEditprofileDialogBinding.inflate(layoutInflater)
        editProfileDialog.setContentView(dialogBinding.root)

        Glide.with(requireContext()).load(ivProfile).placeholder(R.drawable.placeholder)
            .into(dialogBinding.ivProfile)
        dialogBinding.tieName.setText(etName)
        dialogBinding.tiePhoneNumber.setText(etPhoneNumber)
        dialogBinding.tieCity.setText(etCity)

        // cancel button
        dialogBinding.btnEditProfileCancel.setOnClickListener {
            Toast.makeText(context, "No changes occured!", Toast.LENGTH_SHORT).show()
            editProfileDialog.hide()
        }

        // Save button
        dialogBinding.btnEditProfileSave.setOnClickListener {

            showShimmer()
            val Name = dialogBinding.tieName.text.toString()
            val PhoneNumber = dialogBinding.tiePhoneNumber.text.toString()
            val City = dialogBinding.tieCity.text.toString()

            dialogBinding.tilName.isErrorEnabled = false
            dialogBinding.tilCity.isErrorEnabled = false
            dialogBinding.tilPhoneNumber.isErrorEnabled = false

            if (Name.isEmpty()) {
                dialogBinding.tilName.isErrorEnabled = true
                dialogBinding.tilName.error = "Name is required!"
                dialogBinding.tilName.requestFocus()

            } else if (PhoneNumber.isEmpty()) {
                dialogBinding.tilPhoneNumber.isErrorEnabled = true
                dialogBinding.tilPhoneNumber.error = "Phone number is required!"
                dialogBinding.tilPhoneNumber.requestFocus()
            } else if (PhoneNumber.length != 10) {
                dialogBinding.tilPhoneNumber.isErrorEnabled = true
                dialogBinding.tilPhoneNumber.error = "Enter number without 0"
                dialogBinding.tilPhoneNumber.requestFocus()
            } else if (City.isEmpty()) {
                dialogBinding.tilCity.isErrorEnabled = true
                dialogBinding.tilCity.error = "City is required!"
                dialogBinding.tilCity.requestFocus()
            } else {
                user = User(
                    id = user.id,
                    name = Name,
                    phone = PhoneNumber,
                    email = user.email,
                    city = City,
                    imageUrl = user.imageUrl
                )
                FirebaseClass().updateUserData(this, user)
                editProfileDialog.hide()
            }
        }

        editProfileDialog.setCanceledOnTouchOutside(false)
        editProfileDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AccountFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AccountFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}