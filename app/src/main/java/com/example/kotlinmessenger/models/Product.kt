package com.example.kotlinmessenger.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Product(val uid:String,val pid:String,val productname:String,val productprice:String,val profileImageUrl:String):
    Parcelable {
    constructor():this("","","","","")
}