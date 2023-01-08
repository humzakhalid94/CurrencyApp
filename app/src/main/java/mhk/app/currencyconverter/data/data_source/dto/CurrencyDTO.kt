package mhk.app.currencyconverter.data.data_source.dto

data class CurrencyDTO(
    val symbols: HashMap<String, String>,
    val success: Boolean,
    val message : String
)