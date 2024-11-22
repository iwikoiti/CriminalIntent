package com.example.criminalintent

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.criminalintent.database.CrimeDatabase
import com.example.criminalintent.database.MIGRATION_1_2
import java.util.UUID
import java.util.concurrent.Executors

private const val  DATABASE_NAME = "crime-database"
private const val TAG = "CrimeRepository"
class CrimeRepository private constructor(context: Context){

    private val database : CrimeDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            CrimeDatabase::class.java,
            DATABASE_NAME
        ).addMigrations(MIGRATION_1_2).build()

    private val crimeDao = database.crimeDao()

    private val executor = Executors.newSingleThreadExecutor()

    fun getCrimes(): LiveData<List<Crime>> = crimeDao.getCrimes()
    fun getCrime(id: UUID): LiveData<Crime?> = crimeDao.getCrime(id)

    fun updateCrime(crime: Crime) {
        executor.execute {
            crimeDao.updateCrime(crime)
        }
    }

    fun addCrime(crime: Crime) {
        executor.execute {
            crimeDao.addCrime(crime)
        }
    }

    //Проверка при выводе в редактирование преступления
//    fun getCrime(id: UUID): LiveData<Crime?> {
//        Log.d(TAG, "ПРЕСТУПЛЕНИЕ ВНУТРИ GET CRIME $id")
//        return crimeDao.getCrime(id).also {
//            it.observeForever { crime ->
//                Log.d(TAG, "Получен объект Crime: $crime")
//            }
//        }
//    }

    companion object{
        private var INSTANCE: CrimeRepository? = null
        fun initialize(context: Context){
            if (INSTANCE == null){
                INSTANCE = CrimeRepository(context)
            }
        }

        fun get(): CrimeRepository{
            return INSTANCE?:
            throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}