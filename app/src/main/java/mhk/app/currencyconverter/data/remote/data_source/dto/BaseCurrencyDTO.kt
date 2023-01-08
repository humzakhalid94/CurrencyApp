package mhk.app.currencyconverter.data.remote.data_source.dto

data class BaseCurrencyDTO(
    val base: String,
    val date: String,
    val rates: HashMap<String, String>,
    val success: Boolean,
    val message : String
)