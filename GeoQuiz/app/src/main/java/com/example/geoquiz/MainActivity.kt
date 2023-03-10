package com.example.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var falseButton: Button
    private lateinit var trueButton:Button
    private lateinit var restartButton: Button
    private lateinit var prevButton: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var questionTextView: TextView
    private lateinit var tvCountDoneAnswers: TextView
    private lateinit var tvGeneralCountOfAnswers: TextView

    //List of question for quiz (A better way is database)
    private val questionBank = listOf<Question>(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )
    private var currentIndex = 0//current index question in questionBank (list of question)
    private var countDoneAnswers = 0 //total number of completed answers
    private var countRightAnswers = 0//total number of right answers
    private val setIndexDoneQuesiotn = mutableSetOf<Int>()


    private val quizViewModel: QuizViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        initialiseVariable()


        //set information about count of auestions and count of done answers
        tvCountDoneAnswers.setText("$countDoneAnswers")
        tvGeneralCountOfAnswers.setText("${questionBank.size}")

        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
        }

        falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
        }

        prevButton.setOnClickListener {
            updateAndMoveToPrevQuestion()
            checkDoneAnswer()
        }

        nextButton.setOnClickListener {
            updateAndMoveToNextQuestion()
            checkDoneAnswer()
        }

        questionTextView.setOnClickListener {
            updateAndMoveToNextQuestion()
        }

        restartButton.setOnClickListener {
            clearDataAboutPassingQuestion()
        }

        //fill textView area with information (question) when application start
        updateQuestion()

    }

    fun initialiseVariable(){
        //initialise variable (give them link to the View)
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        restartButton = findViewById(R.id.button_restart)
        questionTextView = findViewById(R.id.question_text_view)
        tvCountDoneAnswers = findViewById(R.id.tv_count_done_answers)
        tvGeneralCountOfAnswers = findViewById(R.id.tv_general_count_of_question)
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
        var messageResId = 0
        if(userAnswer == correctAnswer){
            messageResId = R.string.correct_toast
            countRightAnswers++//increase score right answer of it right
        } else {
            messageResId = R.string.incorrect_toast
        }

        setIndexDoneQuesiotn.add(currentIndex)//add current index to set indexes done auestions
        countDoneAnswers++//this show that one of the question
        tvCountDoneAnswers.setText("${countDoneAnswers}")

        checkDoneAnswer()

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()

        showResultMessage()
    }

    //enable or disable buttons depending on whether the user answered the question or not
    private fun checkDoneAnswer(){
        if(setIndexDoneQuesiotn.contains(currentIndex)) {
            trueButton.isEnabled = false
            falseButton.isEnabled = false
        } else {
            trueButton.isEnabled = true
            falseButton.isEnabled = true
        }
    }

    private fun clearDataAboutPassingQuestion(){
        setIndexDoneQuesiotn.clear()
        trueButton.isEnabled = true
        falseButton.isEnabled = true
        countDoneAnswers = 0
        countRightAnswers = 0
        tvCountDoneAnswers.setText("${countDoneAnswers}")
    }

    private fun showResultMessage(){
        if(countDoneAnswers == questionBank.size){
            val countWrongAnsw = questionBank.size - countRightAnswers
            Toast.makeText(this,
                "Correct answers: ${countRightAnswers} (${findPersent(countRightAnswers)}%)" +
                        "\nWrong answers: ${countWrongAnsw} (${findPersent(countWrongAnsw)}%)", Toast.LENGTH_LONG).show()
        }
    }
    //Find percentage of a number
    private fun findPersent(a: Int, b: Int = questionBank.size):String{
        return String.format("%.2f", (a.toDouble()/b.toDouble()*100.0))
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