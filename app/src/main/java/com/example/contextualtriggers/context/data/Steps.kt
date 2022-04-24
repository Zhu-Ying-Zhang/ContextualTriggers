package com.example.contextualtriggers.context.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "steps_tbl")
class Steps(
    @PrimaryKey
    val date: String,
    var steps: Int
) {

}