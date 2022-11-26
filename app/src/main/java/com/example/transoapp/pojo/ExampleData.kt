package com.example.transoapp.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ExampleData {

    @SerializedName("status")
    @Expose
    var status = 0

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    class Data {
        @SerializedName("docs")
        @Expose
        var docs: List<Doc>? = null
    }

    class Doc {
        @SerializedName("quote_coin_icon")
        @Expose
        var quoteCoinIcon: String? = null

        @SerializedName("quote_coin_name")
        @Expose
        var quoteCoinName: String? = null

        @SerializedName("cs")
        @Expose
        var cs = 0f

        @SerializedName("base_coin_icon")
        @Expose
        var baseCoinIcon: String? = null

        @SerializedName("market_cap")
        @Expose
        var marketCap = 0f

        @SerializedName("base_coin_name")
        @Expose
        var baseCoinName: String? = null

        @SerializedName("all_coin_price")
        @Expose
        var allCoinPrice: AllCoinPrice? = null

        @SerializedName("_id")
        @Expose
        var id: String? = null

        @SerializedName("last_price")
        @Expose
        var lastPrice = 0f

        @SerializedName("price_change")
        @Expose
        var priceChange: Float? = 0f

        @SerializedName("price_change_percent")
        @Expose
        var priceChangePercent: Float? = 0f

        @SerializedName("high_price")
        @Expose
        var highPrice = 0f

        @SerializedName("low_price")
        @Expose
        var lowPrice = 0f

        @SerializedName("volume")
        @Expose
        var volume = 0f

        @SerializedName("quote_volume")
        @Expose
        var quoteVolume = 0f

        @SerializedName("pair_name")
        @Expose
        var pairName: String? = null

        @SerializedName("base_coin_code")
        @Expose
        var baseCoinCode: String? = null

        @SerializedName("quote_coin_code")
        @Expose
        var quoteCoinCode: String? = null

        @SerializedName("base_coin_precision")
        @Expose
        var baseCoinPrecision = 0

        @SerializedName("quote_coin_precision")
        @Expose
        var quoteCoinPrecision = 0

        @SerializedName("tick_size")
        @Expose
        var tickSize = 0f

        @SerializedName("__order")
        @Expose
        var order: Any? = null
    }

    class AllCoinPrice {
        @SerializedName("usd")
        @Expose
        var usd: Usd? = null
    }

    class Usd {
        @SerializedName("coin_price")
        @Expose
        var coinPrice = 0f
    }

}