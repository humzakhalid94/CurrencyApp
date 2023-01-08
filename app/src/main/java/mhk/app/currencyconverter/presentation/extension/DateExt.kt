package mhk.app.currencyconverter.presentation.extension

import java.text.SimpleDateFormat
import java.util.*


fun Date.formatted() : String{
    val format = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
    return format.format(this)
}