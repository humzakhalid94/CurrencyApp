package mhk.app.currencyconverter.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import mhk.app.currencyconverter.data.common.Converters
import mhk.app.currencyconverter.data.local.CurrencyDao
import mhk.app.currencyconverter.domain.model.RecordEntity

@Database(
    entities = [
        RecordEntity::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase(){
    abstract fun currencyDao() : CurrencyDao
}