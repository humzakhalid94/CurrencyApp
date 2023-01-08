package mhk.app.currencyconverter.domain.model

data class CurrencyEntity(
    val symbol: String,
    val name: String,

    val descriptions: String = "",
    val rate: Double = 0.0
)