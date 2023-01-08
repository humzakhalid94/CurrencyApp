package mhk.app.currencyconverter.data.remote.data_source.dto

data class CurrencyGenericDTO<T>(
    val base: String,
    val date: String,
//    val rates: T,
    val symbols: HashMap<String, String>,
    val success: Boolean,
    val timestamp: Int,
    val message : String

)