package com.example.asaankissan.activities

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asaankissan.adapters.LocationsAdapter
import com.example.asaankissan.adapters.UploadImageAdapter
import com.example.asaankissan.databinding.ActivityAdPostingBinding
import com.example.asaankissan.databinding.ItemLayoutLocationsDialogBinding
import com.example.asaankissan.firebase.FirebaseClass
import com.example.asaankissan.models.Ad
import com.example.asaankissan.models.Category
import com.example.asaankissan.models.City
import com.example.asaankissan.models.User
import com.example.asaankissan.others.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import java.io.ByteArrayOutputStream
import java.util.regex.Pattern

class AdPostingActivity : AppCompatActivity() {

    fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    var binding: ActivityAdPostingBinding? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var user: User
    private var uploadImagesList: ArrayList<String> = ArrayList()
    private var locationsList: ArrayList<City> = ArrayList()

    companion object {
        const val OPEN_CAMERA_REQUEST_CODE = 1
        const val OPEN_GALLERY_REQUEST_CODE = 2
    }

    override fun onRestart() {
        super.onRestart()
        auth = FirebaseAuth.getInstance()
        FirebaseClass().getCurrentUserDataForActvity(this)
        FirebaseClass().getAllCities(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdPostingBinding.inflate(layoutInflater)

        setContentView(binding?.root)


        auth = FirebaseAuth.getInstance()
        FirebaseClass().getCurrentUserDataForActvity(this)
        FirebaseClass().getAllCities(this)
       //FirebaseClass().getCurrentCategoryAds(this)

        //select images for Ad
        binding?.btnUploadImages?.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("Choose Action")

            alertDialog.setPositiveButton("Camera") { _, _ ->
                captureImageFromCamera()
            }
            alertDialog.setNegativeButton("Gallery") { _, _ ->
                chooseImageFromGallery()
            }
            alertDialog.show()
        }

        // Post Ad button click here to post Ad
        binding?.btnPostAd?.setOnClickListener {

            //FirebaseClass().uploadAd(this, ad, "Birds")
            validateAdPostForm()

        }

        binding?.ivSelectCity?.setOnClickListener {

            selecCityDialog()
        }

    }

    fun getAllcities(cities: ArrayList<City>){
        locationsList.addAll(cities)
        Log.i("mytag", locationsList.toString())
    }

    private fun validateAdPostForm() {

        val adTitle = binding?.tieAdTitle?.text.toString()
        val adDescription = binding?.tieAdDescription?.text.toString()
        val adPrice = binding?.tieAdPrice?.text.toString()
        val adMeasure = binding?.tieAdMeasure?.text.toString()
        val adCity = binding?.etSelectCity?.text.toString()
        val adAddress = binding?.tieAddress?.text.toString()
        val adContactNumber = binding?.tiePhoneNumber?.text.toString()
        val adUserId = FirebaseClass().getCurrentUserId()

        val category: Category = intent.getSerializableExtra("category") as Category
        val adCategoryName = category.catName
        val adImages = uploadImagesList
        val adUploadTime = System.currentTimeMillis().toString()
        val adId = System.currentTimeMillis().toString()

        binding?.tilPhoneNumber?.isErrorEnabled = false
        binding?.tilAdTitle?.isErrorEnabled = false
        binding?.tilAdDescription?.isErrorEnabled = false
        binding?.tilAddress?.isErrorEnabled = false
        binding?.tilAdPrice?.isErrorEnabled = false
        binding?.tilAdMeasure?.isErrorEnabled = false
        /*binding?.etSelectCity?.error = ""
        binding?.btnUploadImages?.error = ""*/

        if (adTitle.isEmpty()) {
          //  binding?.tilAdTitle?.isErrorEnabled = true
            binding?.tieAdTitle?.error = "Title is required!"
            binding?.tieAdTitle?.requestFocus()
        } else if (adDescription.isEmpty()) {
            //binding?.tilAdDescription?.isErrorEnabled = true
            binding?.tieAdDescription?.error = "Description is required!"
            binding?.tieAdDescription?.requestFocus()
        } else if (adPrice.isEmpty()) {
            //binding?.tilAdPrice?.isErrorEnabled = true
            binding?.tieAdPrice?.error = "Price is required!"
            binding?.tieAdPrice?.requestFocus()

        }
        else if (adPrice.length < 2) {
            //binding?.tilAdPrice?.isErrorEnabled = true
            binding?.tieAdPrice?.error = "Price must be more than 9 rupees"
            binding?.tieAdPrice?.requestFocus()
        } else if (adMeasure.isEmpty()) {
            //binding?.tilAdPrice?.isErrorEnabled = true
            binding?.tieAdMeasure?.error = "Please set the appropriate measure"
            binding?.tieAdMeasure?.requestFocus()

        } else if (adCity.isEmpty()) {
            /*binding?.etSelectCity?.error = "Please select any city"
            binding?.tieAdPrice?.requestFocus()*/
            showToast("City is missing!")
        } else if (adAddress.isEmpty()) {
            //binding?.tilAddress?.isErrorEnabled = true
            binding?.tieAddress?.error = "Please enter accurate address"
            binding?.tieAddress?.requestFocus()
        } else if (adContactNumber.isEmpty()) {
            //binding?.tilPhoneNumber?.isErrorEnabled = true
            binding?.tiePhoneNumber?.error = "Phone number is required"
            binding?.tiePhoneNumber?.requestFocus()
        } else if (adImages.isEmpty()) {
            /*binding?.btnUploadImages?.error = "Please Upload at least one AD image"
            binding?.btnUploadImages?.requestFocus()*/
            showToast("AD image is missing!")
        } else {

            val ad = Ad(
                adId,
                adImages,
                adTitle,
                adDescription,
                adPrice.toDouble(),
                adMeasure,
                adCity,
                adAddress,
                adUserId,
                adCategoryName,
                adContactNumber,
                adUploadTime,
                adStatus = "1"
            )
            FirebaseClass().uploadAd(this, ad)

        }
    }

    private fun selecCityDialog() {

        val locationsDialog = Dialog(this)
        val dialogBinding = ItemLayoutLocationsDialogBinding.inflate(layoutInflater)
        locationsDialog.setContentView(dialogBinding.root)

        val locationsAdapter = LocationsAdapter(this, locationsList)
        dialogBinding.rvLocations.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        locationsAdapter.setOnItemClickListner(object : LocationsAdapter.onItemClickListner {
            override fun onItemClick(position: Int) {
                val selectedLocation = locationsList[position]
                binding?.etSelectCity?.setText(selectedLocation.cityName)
                locationsDialog.hide()
            }
        })
        dialogBinding.rvLocations.adapter = locationsAdapter
        locationsDialog.show()
    }

    private fun captureImageFromCamera() {

        Dexter.withContext(this).withPermission(android.Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(permissionResponse: PermissionGrantedResponse?) {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, OPEN_CAMERA_REQUEST_CODE)
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    if (response!!.isPermanentlyDenied) {
                        AlertDialog.Builder(this@AdPostingActivity)
                            .setTitle("Permission Denied")
                            .setMessage("It looks like you have permanently denied camera permission. Go to application settings to enalbe it.")
                            .setPositiveButton("Settings") { _, _ ->
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                val uri = Uri.fromParts("package", packageName, null)
                                intent.data = uri
                                startActivity(intent)
                            }
                            .setNegativeButton("Not Now", null)
                            .show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    request: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    AlertDialog.Builder(this@AdPostingActivity).setTitle("Permission Required!")
                        .setMessage("Please give Camera permission to capture images.")
                        .setPositiveButton("Ok") { _, _ ->
                            token?.continuePermissionRequest()
                        }
                        .setNegativeButton("Cancel", null)
                        .show()
                }
            }).onSameThread().check()

    }

    private fun chooseImageFromGallery() {

        Dexter.withContext(this).withPermissions(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.isAnyPermissionPermanentlyDenied) {
                        AlertDialog.Builder(this@AdPostingActivity)
                            .setTitle("Permission Denied")
                            .setMessage("It looks like you have permanently denied storage permission. Go to application settings to enable it.")
                            .setPositiveButton("Settings") { _, _ ->
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                val uri = Uri.fromParts("package", packageName, null)
                                intent.data = uri
                                startActivity(intent)
                            }
                            .setNegativeButton("Not Now", null)
                            .show()
                    } else if (report.areAllPermissionsGranted()) {
                        val galleryIntent =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        startActivityForResult(galleryIntent, OPEN_GALLERY_REQUEST_CODE)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    AlertDialog.Builder(this@AdPostingActivity).setTitle("Permission Required!")
                        .setMessage("Please give storage permission to select images from storage.")
                        .setPositiveButton("Ok") { _, _ ->
                            token?.continuePermissionRequest()
                        }
                        .setNegativeButton("Cancel", null)
                        .show()
                }
            }).onSameThread().check()

    }

    fun addUploadedSuccessfully() {
        Toast.makeText(this, "Uploaded Successfully", Toast.LENGTH_SHORT).show()
        finish()
    }

    fun addUploadingFailed() {
        Toast.makeText(this, "Uploading Failed", Toast.LENGTH_SHORT).show()
    }

    fun displayUserPhoneNumber(user: User) {

        Log.d("mytag", "awi")
        if (user.phone.isEmpty()) {
            AlertDialog.Builder(this).setTitle("Profile Alert")
                .setMessage("Your Profile is not Completed, Go to Profile Settings and Add Your Contact Number and City.")
                .setPositiveButton("Go To Profile") { _, _ ->
                    val profileIntent = Intent(this, MainActivity::class.java)
                    profileIntent.putExtra("profileKey", "1")
                    startActivity(profileIntent)
                }
                .setNegativeButton("Cancel") { _, _ ->
                    finish()
                }
                .setCancelable(false)
                .show()
        } else {
            binding?.tiePhoneNumber?.setText(user.phone)
        }

    }

    private fun getFileExtension(uri: Uri?): String? {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri!!))
    }

    private fun uploadImageToStorage(imageUri: Uri) {
        if (imageUri != null) {
            val ref: StorageReference = FirebaseStorage.getInstance().reference.child(
                "IMAGE" + System.currentTimeMillis() + "." + getFileExtension(imageUri)
            )
            ref.putFile(imageUri).addOnSuccessListener { task ->
                task.metadata!!.reference!!.downloadUrl.addOnSuccessListener { url ->

                    imageUploadedSuccessfully(url.toString())
                }
            }.addOnFailureListener { e ->
                showToast("Unable to upload image")
                hideProgressBar()
                showRecyclerView()
                Log.d("e", e.toString())
            }
        }
    }

    private fun imageUploadedSuccessfully(url: String) {
        uploadImagesList.add(url)
        loadImagesIntoRecyclerView(uploadImagesList)
    }

    private fun loadImagesIntoRecyclerView(imagesList: ArrayList<String>) {
        val uploadImageAdapter = UploadImageAdapter(this, imagesList)
        binding?.rvImages?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        hideProgressBar()
        showRecyclerView()
        if (imagesList.size == 1) {
            binding?.rvImages?.adapter = uploadImageAdapter
        } else if (imagesList.size > 1) {
            uploadImageAdapter.notifyDataSetChanged()
        }
    }

    private fun hideProgressBar() {
        binding?.progressBar?.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding?.progressBar?.visibility = View.VISIBLE
    }

    private fun showRecyclerView() {
        binding?.rvImages?.visibility = View.VISIBLE
    }

    private fun hideRecyclerView() {
        binding?.rvImages?.visibility = View.INVISIBLE
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == OPEN_CAMERA_REQUEST_CODE) {
                try {
                    val bitmap: Bitmap = data!!.extras!!.get("data") as Bitmap
                    val uri = getImageUri(this, bitmap)
                    if (uri != null) {
                        //showProgressDialog("Uploading....")
                        showProgressBar()
                        hideRecyclerView()
                        uploadImageToStorage(uri)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    showToast("Unable to Upload Image....")
                    //hideProgressDialog()
                    hideProgressBar()
                    showRecyclerView()
                }
            }

            if (requestCode == OPEN_GALLERY_REQUEST_CODE && data != null) {
                try {
                    val contentUri = data.data
                    //showToast(data.data.toString())
                    uploadImageToStorage(contentUri!!)
                    showProgressBar()
                    hideRecyclerView()

                } catch (e: Exception) {
                    e.printStackTrace()
                    showToast("Unable to Upload Image....")

                }
            }
        }
    }
}