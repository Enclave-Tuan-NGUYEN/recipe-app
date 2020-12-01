package com.project.recipedemo.database.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.project.recipedemo.database.typeconverter.StringTypeConverter
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "recipes")
data class Recipe(

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "type")
    var type: String = "",

    @ColumnInfo(name = "image")
    var image: String = "",

    @TypeConverters(StringTypeConverter::class)
    @ColumnInfo(name = "ingredients")
    var ingredients: List<String>,

    @TypeConverters(StringTypeConverter::class)
    @ColumnInfo(name = "steps")
    var steps: List<String>
) : Parcelable {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
