package com.example.criminalintent

import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

data class Crime(@PrimaryKey val id: UUID = UUID.randomUUID(),
                 var title: String = "",
                 var date: Date = Date(),
                 var isSolved: Boolean = false)