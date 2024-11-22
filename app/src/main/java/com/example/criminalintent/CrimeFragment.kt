package com.example.criminalintent

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.room.Update
import java.util.Date
import java.util.UUID

private const val ARG_CRIME_ID = "crime_id"
private const val TAG = "CrimeFragment"

class CrimeFragment: Fragment() {
    private lateinit var crime: Crime
    private lateinit var  titleField: EditText
    private lateinit var dateButton: Button
    private lateinit var solvedCheckBox: CheckBox
    private val crimeDetailViewModel: CrimeDetailViewModel by lazy {
        ViewModelProvider(this)[CrimeDetailViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crime = Crime()
        val crimeId: UUID = arguments?.getSerializable(ARG_CRIME_ID) as UUID
//        val crimeIdString = arguments?.getString(ARG_CRIME_ID)
//        val crimeId: UUID? = crimeIdString?.let { UUID.fromString(it) }

        // загрузка преступления из бд
        crimeDetailViewModel.loadCrime(crimeId)
        Log.d(TAG, "ВОТ ЭТО ОН ПОКАЖЕТ ИЛИ НЕТ: $crimeId")
        //updateUI()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime,container,false)
        titleField = view.findViewById(R.id.crime_title)!!
        dateButton = view.findViewById(R.id.crime_date)!!
        solvedCheckBox = view.findViewById(R.id.crime_solved)!!

        dateButton.apply {
            val formattedDate = formatDate(crime.date) //меняем формат даты в кнопке
            //text = crime.date.toString()
            text = formattedDate
            isEnabled = false
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        crimeDetailViewModel.crimeLiveData.observe(viewLifecycleOwner)
        { crime ->
            if (crime != null){
                Log.d(TAG, "ПРЕСТУПЛЕНИЕ ЗАГРУЖЕНО: $crime")
                Log.d(TAG, "Поля объекта Crime: ID=${crime.id}, Title=${crime.title}, Date=${crime.date}, Solved=${crime.isSolved}")
                this.crime = crime
                updateUI()
            } else {
                Log.d(TAG, "ПРЕСТУПЛЕНИЕ НЕ ЗАГРУЖЕНО")
            }
        }

//        crimeDetailViewModel.crimeLiveData.observe(
//            viewLifecycleOwner,
//            Observer { crime ->
//
//                if (crime != null){
//                    Log.d(TAG, "ПРЕСТУПЛЕНИЕ ЗАГРУЖЕНО: $crime")
//                    this.crime = crime
//                    updateUI()
//                } else {
//                    Log.d(TAG, "ПРЕСТУПЛЕНИЕ НЕ ЗАГРУЖЕНО")
//                }
////                crime?.let {
////                    this.crime = crime
////                    Log.d(TAG, "ПРЕСТУПЛЕНИЕ ЗАГРУЖЕНО: $crime")
////                    updateUI()
////                }
//            }
//        )


        //вот это норм
//        crimeDetailViewModel.crimeLiveData.observe(viewLifecycleOwner) { crime ->
//            crime?.let { nonNullCrime ->
//                this.crime = nonNullCrime  // Kotlin smart cast
//                Log.d(TAG, "ПРЕСТУПЛЕНИЕ ЗАГРУЖЕНО: $nonNullCrime")
//                updateUI()
//            }
//        }

    }

    private fun updateUI(){
        titleField.setText(crime.title)
        dateButton.text = crime.date.toString()
        //solvedCheckBox.isChecked = crime.isSolved
        solvedCheckBox.apply {
            isChecked = crime.isSolved
            jumpDrawablesToCurrentState()
        }
    }

    override fun onStart() {
        super.onStart()

        val titleWatcher = object : TextWatcher
        {
            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // Это пространство оставлено пустым специально
            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                crime.title = sequence.toString()
            }

            override fun
                    afterTextChanged(sequence: Editable?) {
                // И это
            }
        }

        titleField.addTextChangedListener(titleWatcher)

        solvedCheckBox.apply {
            setOnCheckedChangeListener{_, isChecked ->
                crime.isSolved = isChecked}
        }
    }


    companion object{
        fun newInstance(crimeId: UUID): CrimeFragment{
            val args = Bundle().apply {
                putSerializable(ARG_CRIME_ID, crimeId)
                //putString(ARG_CRIME_ID, crimeId.toString()) // преобразуем в строку
            }
            return CrimeFragment().apply {
                arguments = args
                Log.d(TAG, "ARRRRRG $args")
            }
        }
    }

    // Функция форматирования даты
    private fun formatDate(date: Date): String {
        val formatString = "EEEE, MMM dd, yyyy HH:mm"
        val formattedDate = DateFormat.format(formatString, date)
        return formattedDate.toString()
    }
}