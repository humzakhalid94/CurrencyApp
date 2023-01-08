package mhk.app.currencyconverter.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BaseCurrencyEntity(
    val base: String = "",
    val rates: LinkedHashMap<String, String> = linkedMapOf<String, String>()
) : Parcelable
