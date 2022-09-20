package com.example.asaankissan.firebase

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.asaankissan.activities.*
import com.example.asaankissan.adapters.MyAdsAdapter
import com.example.asaankissan.fragments.AccountFragment
import com.example.asaankissan.fragments.ExploreFragment
import com.example.asaankissan.fragments.MyAdsFragment
import com.example.asaankissan.fragments.SellFragment
import com.example.asaankissan.models.Ad
import com.example.asaankissan.models.Category
import com.example.asaankissan.models.City
import com.example.asaankissan.models.User
import com.example.asaankissan.others.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirebaseClass {
    private val myFirestore = FirebaseFirestore.getInstance()
    private lateinit var documentRef: DocumentReference

    fun uploadAd(activity: AdPostingActivity, ad: Ad) {
        myFirestore.collection(Constants.ADS)
            .document()
            .set(ad, SetOptions.merge())
            .addOnSuccessListener {
                activity.addUploadedSuccessfully()
            }
            .addOnFailureListener {
                activity.addUploadingFailed()
            }
    }

    fun findUserByEmail(activity: GoogleSignupActivity, email: String) {
        myFirestore.collection(Constants.USERS).whereEqualTo("email", email).get()
            .addOnSuccessListener { document ->
                if (document.documents.size > 0) {
                    val user = document.documents[0].toObject(User::class.java)!! as User
                    activity.userAlreadyExistedInDatabase(user)
                } else {
                    //user not existed
                    activity.userNotExistedInDatabase()
                }

            }.addOnFailureListener {

            }
    }

    fun saveUserDataToFireStore(activity: GoogleSignupActivity, user: User) {
        myFirestore.collection(Constants.USERS).document(user.id)
            .set(user, SetOptions.merge())
            .addOnSuccessListener {
                activity.newUserSavedSuccessfully()
            }
            .addOnFailureListener {

            }
    }

    fun updateUserData(fragment: Fragment, user: User) {
        val userHashMap = HashMap<String, Any>()
        userHashMap["name"] = user.name
        userHashMap["phone"] = user.phone
        userHashMap["city"] = user.city
        myFirestore.collection(Constants.USERS).document(user.id)
            .update(userHashMap)
            .addOnSuccessListener {
                if (fragment is AccountFragment) {
                    fragment.userProfileUpdatedSuccessfully(user)

                }
            }
            .addOnFailureListener {
                if (fragment is AccountFragment) {
                    fragment.userProfileNotUpdated()

                }
            }
    }

    fun getCurrentCategoryAds(activity: Activity, category: String) {

        val currentAdsList : ArrayList<Ad> = ArrayList()
        myFirestore.collection(Constants.ADS).whereEqualTo("adCategory", category)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val ad: Ad = document.toObject(Ad::class.java)
                    currentAdsList.add(ad)
                }
                if (activity is CategoryAdsActivity) {

                    activity.loadCurrentCategoryAdsListIntoRecyclerView(currentAdsList)
                    //activity.showToast("success")

                }
            }
            .addOnFailureListener {
                if (activity is CategoryAdsActivity) {
                    //activity.getCategoryAds(ad)
                    activity.showToast("Error")
                }
            }
    }

    fun getAllFreshRecommAds(fragment: Fragment) {
        val myAdsList : ArrayList<Ad> = ArrayList()
        myFirestore.collection(Constants.ADS).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val ad : Ad = document.toObject(Ad::class.java)
                    myAdsList.add(ad)
                }
                if (fragment is ExploreFragment) {
                    fragment.loadFreshRecommAdsInToRecyclerView(myAdsList)
                }
            }
    }

    fun getAllAdsForSearch(activity: Activity) {
        val myAdsList : ArrayList<Ad> = ArrayList()
        myFirestore.collection(Constants.ADS).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val ad : Ad = document.toObject(Ad::class.java)
                    myAdsList.add(ad)
                }
                if (activity is AdSearchActivity) {
                    activity.loadAllAdsIntoRecylerView(myAdsList)
                }
            }
    }

    val myadsList : ArrayList<Ad> = ArrayList()
    fun getMyAds(fragment: Fragment) {

        myFirestore.collection(Constants.ADS).whereEqualTo("adUserId", getCurrentUserId())
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val ad: Ad = document.toObject(Ad::class.java)
                    ad.adId=document.id
                    myadsList.add(ad)
                }
                if (fragment is MyAdsFragment){
                    //fragment.showToast("Success")
                    fragment.loadMyAdsListIntoRecyclerView(myadsList)
                }
            }
            .addOnFailureListener { document ->
                if (fragment is MyAdsFragment){
                    fragment.showToast("Failed when retrieving ads from server")
                }
            }
    }

    fun deleteMyAd(fragment: Fragment, ad: Ad, position: Int){
        myFirestore.collection(Constants.ADS).document(ad.adId)
            .update("adStatus","2")
            .addOnSuccessListener {
                if (fragment is MyAdsFragment){
                    fragment.showToast("Ad deleted successfully")
                }
            }
            .addOnFailureListener {
                if (fragment is MyAdsFragment){
                    fragment.showToast("Unable to delete this Ad")
                }
            }
    }

    fun getAllCategories(fragment: Fragment){
        val categoriesList : ArrayList<Category> = ArrayList()
        myFirestore.collection(Constants.CATEGORY).orderBy("catId", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    val cat : Category = document.toObject(Category::class.java)
                    categoriesList.add(cat)
                }
                if (fragment is ExploreFragment){
                    fragment.loadCategoriesIntoRecyclerView(categoriesList)
                }
                if (fragment is SellFragment){
                    fragment.loadCategoriesIntoRecyclerView(categoriesList)
                }
            }
            .addOnFailureListener { documents ->
                if (fragment is ExploreFragment){
                    fragment.showToast("Error while fetching categories!")
                }
                if (fragment is SellFragment){
                    fragment.showToast("Error while fetching categories!")
                }
            }
    }

    fun getAllCities(activity: Activity){
        val citiesList : ArrayList<City> = ArrayList()
        myFirestore.collection(Constants.CITY).orderBy("cityId", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val city: City = document.toObject(City::class.java)
                    citiesList.add(city)
                }
                    if (activity is AdPostingActivity){
                        activity.getAllcities(citiesList)
                        //activity.showToast(citiesList.toString())
                    }

            }
            .addOnFailureListener{ documents ->
                if (activity is AdPostingActivity){
                    activity.showToast("Error while fetching cities")
                }
            }
    }


    fun getCurrentUserDataForActvity(activity: Activity) {
        myFirestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val user: User = document.toObject(User::class.java)!!

                if (activity is AdPostingActivity) {
                   activity.displayUserPhoneNumber(user)
                    //activity.displayAlert()
                }
            }
            .addOnFailureListener {

            }
    }

    fun getCurrentUserDataForFragment(fragment: Fragment) {
        myFirestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val user: User = document.toObject(User::class.java)!!
                if (fragment is AccountFragment) {
                    fragment.displayUserInfoIntoEditText(user)
                }
            }
            .addOnFailureListener {
                /*if (activity is UserProfileActivity) {
                    //activity.hideProgressDialog()
                } else if (activity is MainActivity) {
                    //activity.hideProgressDialog()
                }*/
            }
    }


    fun getCurrentUserId(): String {

        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if (currentUser != null) {
            currentUserId = currentUser.uid
        }
        return currentUserId
    }
}