package com.example.asaankissan.others

import com.example.asaankissan.models.Category
import com.example.asaankissan.models.Ad

object Constants {

    const val USERS = "USERS"
    const val ADS = "ADS"
    const val CATEGORY = "Category"
    const val CITY = "CITIES"

    fun getImageListSlider() : ArrayList<String> {

        val imageList = ArrayList<String>()

        imageList.add("https://media.istockphoto.com/photos/fresh-fruits-and-vegetables-picture-id589415708?k=20&m=589415708&s=612x612&w=0&h=W8_PMpIadeMht-8JwchHqiXQZUpkZt_869WhOQvPjO0=")
        imageList.add("https://media.istockphoto.com/photos/unrecognizable-woman-shops-for-produce-in-supermarket-picture-id871227828?k=20&m=871227828&s=612x612&w=0&h=9pnLBf67HRTJBpCLwvwKQd--TCMxdHnIx12tFLuNgTw=")
        imageList.add("https://media.istockphoto.com/photos/group-of-farm-animals-standing-next-to-a-straw-bale-picture-id188031530?k=20&m=188031530&s=612x612&w=0&h=o7ZNyLrLdaDS5F6SaQ_1bjau5fmye1lWn6B-8mAL2n4=")
        imageList.add("https://www.provisioneronline.com/ext/resources/2021/02/12/0221np_poultrypersp_img1.jpg?1613158612")
        imageList.add("https://media.istockphoto.com/photos/male-farmer-pouring-milk-in-canister-at-dairy-farm-picture-id1331816791?k=20&m=1331816791&s=612x612&w=0&h=DJufDHxO1C5RWmh5P1Djsk0q1E6ndILi2fPJ0lQokGY=")

        return imageList
    }

    fun getCategoriesList() : ArrayList<Category> {

        val catList: ArrayList<Category> = ArrayList()

        catList.add(Category("1","https://media.istockphoto.com/photos/fresh-fruits-and-vegetables-picture-id589415708?k=20&m=589415708&s=612x612&w=0&h=W8_PMpIadeMht-8JwchHqiXQZUpkZt_869WhOQvPjO0=", "Vegetables"))
        catList.add(Category("2","https://media.istockphoto.com/photos/unrecognizable-woman-shops-for-produce-in-supermarket-picture-id871227828?k=20&m=871227828&s=612x612&w=0&h=9pnLBf67HRTJBpCLwvwKQd--TCMxdHnIx12tFLuNgTw=", "Fruits"))
        catList.add(Category("3","https://media.istockphoto.com/photos/group-of-farm-animals-standing-next-to-a-straw-bale-picture-id188031530?k=20&m=188031530&s=612x612&w=0&h=o7ZNyLrLdaDS5F6SaQ_1bjau5fmye1lWn6B-8mAL2n4=", "Animals"))
        catList.add(Category("4","https://media.istockphoto.com/photos/group-of-farm-animals-standing-next-to-a-straw-bale-picture-id188031530?k=20&m=188031530&s=612x612&w=0&h=o7ZNyLrLdaDS5F6SaQ_1bjau5fmye1lWn6B-8mAL2n4=", "Poultry"))
        catList.add(Category("5","https://media.istockphoto.com/photos/group-of-farm-animals-standing-next-to-a-straw-bale-picture-id188031530?k=20&m=188031530&s=612x612&w=0&h=o7ZNyLrLdaDS5F6SaQ_1bjau5fmye1lWn6B-8mAL2n4=", "Fresh Milk"))
        catList.add(Category("6","https://media.istockphoto.com/photos/group-of-farm-animals-standing-next-to-a-straw-bale-picture-id188031530?k=20&m=188031530&s=612x612&w=0&h=o7ZNyLrLdaDS5F6SaQ_1bjau5fmye1lWn6B-8mAL2n4=", "Commodities"))
        catList.add(Category("7","https://media.istockphoto.com/photos/male-farmer-pouring-milk-in-canister-at-dairy-farm-picture-id1331816791?k=20&m=1331816791&s=612x612&w=0&h=DJufDHxO1C5RWmh5P1Djsk0q1E6ndILi2fPJ0lQokGY=", "Open Market"))

        return catList

    }

    fun getLocationsList() : ArrayList<String> {

        val locationsList : ArrayList<String> = ArrayList()

        locationsList.add("Multan")
        locationsList.add("Qadir Pur Raan")
        locationsList.add("Khanewal")
        locationsList.add("Vehari")
        locationsList.add("Muzaffargarh")
        locationsList.add("Layyah")
        locationsList.add("Fateh Pur")

        return locationsList
    }

    fun getAdsList() : ArrayList<Ad> {

        val adsList = ArrayList<Ad>()

        val adsl : ArrayList<String> = ArrayList()
        adsl.add("https://media.istockphoto.com/photos/fresh-fruits-and-vegetables-picture-id589415708?k=20&m=589415708&s=612x612&w=0&h=W8_PMpIadeMht-8JwchHqiXQZUpkZt_869WhOQvPjO0=")
        adsl.add("https://media.istockphoto.com/photos/fresh-fruits-and-vegetables-picture-id589415708?k=20&m=589415708&s=612x612&w=0&h=W8_PMpIadeMht-8JwchHqiXQZUpkZt_869WhOQvPjO0=")
        adsl.add("https://media.istockphoto.com/photos/fresh-fruits-and-vegetables-picture-id589415708?k=20&m=589415708&s=612x612&w=0&h=W8_PMpIadeMht-8JwchHqiXQZUpkZt_869WhOQvPjO0=")


        adsList.add(Ad("2",adsl, "Vege", "my vegi tables", 30.0, "Multan", "gali muhalla", "d3d9", "Animal" ,"0305", "34563", "1"))
        adsList.add(Ad("3", adsl, "Vege", "my vegi tables", 30.0, "Multan", "gali muhalla", "d3d9", "Animal","0305", "34563", "1"))


        return adsList
    }
}