package com.example.mytest.Model

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class Datalist {
    @SerializedName("id")
    @Expose
    val id: String? = null

    @SerializedName("email")
    @Expose
    val email: String? = null

    @SerializedName("first_name")
    @Expose
    val firstname: String? = null

    @SerializedName("avatar")
    @Expose
    val avatar: String? = null

    @SerializedName("last_name")
    @Expose
    val lastname: String? = null
}