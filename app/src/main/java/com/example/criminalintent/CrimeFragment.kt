package com.example.criminalintent

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import java.util.Date

class CrimeFragment: Fragment() {
    private lateinit var crime: Crime
    private lateinit var  titleField: EditText
    private lateinit var dateButton: Button
    private lateinit var solvedCheckBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crime = Crime()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime,container,false)
        titleField = view.findViewById<EditText>(R.id.crime_title)!!
        dateButton = view.findViewById<Button>(R.id.crime_date)!!
        solvedCheckBox = view.findViewById<CheckBox>(R.id.crime_solved)!!

        dateButton.apply {
            val formattedDate = formatDate(crime.date) //меняем формат даты в кнопке
            //text = crime.date.toString()
            text = formattedDate
            isEnabled = false
        }
        return view
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
                crime.title =
                    sequence.toString()
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
    // Функция форматирования даты
    private fun formatDate(date: Date): String {
        val formatString = "EEEE, MMM dd, yyyy HH:mm"
        val formattedDate = DateFormat.format(formatString, date)
        return formattedDate.toString()
    }
}