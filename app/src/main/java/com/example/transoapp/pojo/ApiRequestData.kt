package com.example.transoapp.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ApiRequestData {


    @SerializedName("method")
    @Expose
    var method: String? = null

    @SerializedName("params")
    @Expose
    var params: List<String?>? = null

    @SerializedName("id")
    @Expose
    var id: Int? = 0


}
