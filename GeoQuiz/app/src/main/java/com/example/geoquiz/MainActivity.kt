package com.example.geoquiz

import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var falseButton: Button
    private lateinit var trueButton:Button
    private lateinit var prevButton: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var questionTextView: TextView

    //List of question for quiz (A better way is database)
    private val questionBank = listOf<Question>(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        //initialise variable (give them link to the View)
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question_text_view)

        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
        }

        falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
        }

        prevButton.setOnClickListener {
            updateAndMoveToPrevQuestion()
        }

        nextButton.setOnClickListener {
            updateAndMoveToNextQuestion()
        }

        questionTextView.setOnClickListener {
            updateAndMoveToNextQuestion()
        }

        //fill textView area with information (question) when application start
        updateQuestion()

    }

    private fun updateQuestion(){
        /*Create variable that has type Int.
        * This variable point on the question in our list of question.
        * As soon as the application is launched, it is always point on the first question
        * because currentIndex it is 0.
        * Next we get from the object of question his Int number of string resource id
        * (this is needed to be able to set the string in the TextView)*/
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)
    }

    private fun updateAndMoveToNextQuestion(){
        /*the point is to incrementally increase the current index,
            but when it equals the size of questionBank, then immediately equate it to 0*/
        currentIndex = (currentIndex + 1) % questionBank.size
        updateQuestion()
    }

    private fun updateAndMoveToPrevQuestion(){
        /*if currentIndex equals 0 then make currentIndex = 5
        * In other cases we will go through the list in backward direction*/
        currentIndex = if(currentIndex == 0){
            5
        } else {
            (currentIndex - 1) % questionBank.size
        }
        updateQuestion()
    }

    private fun checkAnswer(userAnswer: Boolean){
        /*save in variable correct answer on the current quesion (true or false)*/
        val correctAnswer = questionBank[currentIndex].answer

        /*save variable (Int) with correct or incorrect answer string resource
        * this variable is more for comfort than benefit*/
        val messageResId = if(userAnswer == correctAnswer){
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

}