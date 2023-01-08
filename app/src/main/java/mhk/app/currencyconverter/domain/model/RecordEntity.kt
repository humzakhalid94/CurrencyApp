package mhk.app.currencyconverter.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName = "records", indices = [Index(value = ["id"], unique = true)])
data class RecordEntity(
    @PrimaryKey(autoGenerate = false)
    var id : Int? = null,

    @ColumnInfo(name = "from")
    var from: String,

    @ColumnInfo(name = "to")
    var to: String,

    @ColumnInfo(name = "date")
    val date: Date = Date()
)