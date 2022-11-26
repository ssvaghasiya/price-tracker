package com.example.transoapp.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class SymbolPriceTickerData {
    @SerializedName("e")
    @Expose
    var e: String? = null

    @SerializedName("E")
    @Expose
    var E: Long? = null

    @SerializedName("s")
    @Expose
    var s: String? = null

    @SerializedName("p")
    @Expose
    var p: String? = null

    @SerializedName("P")
    @Expose
    var pricePercentage: String? = null

    @SerializedName("w")
    @Expose
    var w: String? = null

    @SerializedName("x")
    @Expose
    var x: String? = null

    @SerializedName("b")
    @Expose
    var b: String? = null

    @SerializedName("a")
    @Expose
    var a: String? = null

    @SerializedName("h")
    @Expose
    var h: String? = null

    @SerializedName("v")
    @Expose
    var v: String? = null

    @SerializedName("q")
    @Expose
    var q: String? = null

    @SerializedName("O")
    @Expose
    var o: Long? = null

    @SerializedName("C")
    @Expose
    var c: Long? = null

    @SerializedName("F")
    @Expose
    var f: Int? = null

    @SerializedName("L")
    @Expose
    var l: Int? = null

    @SerializedName("n")
    @Expose
    var n: Int? = null
}
