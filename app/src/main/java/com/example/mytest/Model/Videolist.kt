package com.example.mytest.Model

import com.example.mytest.Model.Datalist
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import java.util.ArrayList

class Videolist {
    @SerializedName("page")
    @Expose
    val page: String? = null

    @SerializedName("data")
    @Expose
    val results: List<Datalist> = ArrayList()
}