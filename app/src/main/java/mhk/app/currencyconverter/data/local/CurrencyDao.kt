package mhk.app.currencyconverter.data.local

import androidx.room.*
import mhk.app.currencyconverter.domain.model.RecordEntity
import java.util.*

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(record: RecordEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(records: List<RecordEntity>)

    @Query("SELECT * FROM records")
    fun findAll() : List<RecordEntity>

//    @Query("SELECT * from records where date = date('now','-3 day')")
    @Query("SELECT * from records WHERE CAST((date / 1000) AS INTEGER) BETWEEN strftime('%s','now','-3 days') AND strftime('%s','now')  ORDER BY id DESC")
    fun findLast3Days() : List<RecordEntity>

    @Query("DELETE FROM records")
    fun deleteAll()
}

