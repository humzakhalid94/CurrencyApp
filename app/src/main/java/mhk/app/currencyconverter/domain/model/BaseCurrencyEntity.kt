package mhk.app.currencyconverter.domain.model

data class BaseCurrencyEntity(
    val rates: HashMap<String, String> = hashMapOf<String, String>()
)
