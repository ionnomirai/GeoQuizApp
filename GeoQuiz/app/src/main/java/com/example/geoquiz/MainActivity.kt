package com.example.geoquiz

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var falseButton: Button
    private lateinit var trueButton: Button
    private lateinit var restartButton: Button
    private lateinit var cheatButton: Button
    private lateinit var prevButton: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var questionTextView: TextView
    private lateinit var tvCountDoneAnswers: TextView
    private lateinit var tvGeneralCountOfAnswers: TextView

    private val quizViewModel: QuizViewModel by viewModels()

    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result -> //Handle the result
        if (result.resultCode == Activity.RESULT_OK) {
            quizViewModel.isCheater =
                result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
            //add cheat question to the set with indexes question with answered with help cheat
            quizViewModel.addCheatQuestion()
            //increase the number of cheats used by the user
            quizViewModel.usedCheatIncrementator()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        // Log.d(TAG, "Got a QuizViewModel: $quizViewModel")
        quizViewModel.teststate()

        setContentView(R.layout.activity_main)

        initialiseVariable()

        setPrimaryScreenInfo()

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

        cheatButton.setOnClickListener {
            if (quizViewModel.checkAvailableCheatAttempt()) {
                val answerIsTrue = quizViewModel.currentQuestionAnswer
                val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
                cheatLauncher.launch(intent)
            } else {
                Toast.makeText(this, R.string.number_of_cheats_exceeded_toast, Toast.LENGTH_SHORT)
                    .show()
            }
        }

        //fill textView area with information (question) when application start
        updateQuestion()
    }


    fun initialiseVariable() {
        //initialise variable (give them link to the View)
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        restartButton = findViewById(R.id.button_restart)
        cheatButton = findViewById(R.id.cheat_button)
        questionTextView = findViewById(R.id.question_text_view)
        tvCountDoneAnswers = findViewById(R.id.tv_count_done_answers)
        tvGeneralCountOfAnswers = findViewById(R.id.tv_general_count_of_question)
    }

    private fun updateQuestion() {
        /*Set the current question in the TextView
        * As soon as the application is launched, it is always point on the first question
        * because currentIndex it is 0.
        * Next we get from the object of question his Int number of string resource id
        * (this is needed to be able to set the string in the TextView)*/
        questionTextView.setText(quizViewModel.currentQuestionText)
    }

    private fun updateAndMoveToNextQuestion() {
        quizViewModel.moveToNext()
        updateQuestion()
    }

    private fun updateAndMoveToPrevQuestion() {
        quizViewModel.moveToPrev()
        updateQuestion()
    }

    private fun checkAnswer(userAnswer: Boolean) {
        /*Save variable with string resource
        if user is cheater than toast will be judgmental, it the other case it will be right or
        wrong answer toast*/
        val messageResId = when {
            quizViewModel.checkCurrentQuestionCheat() -> R.string.judgment_toast
            userAnswer == quizViewModel.currentQuestionAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }

        if (userAnswer == quizViewModel.currentQuestionAnswer) {
            quizViewModel.countRightAnswers++
        }

        quizViewModel.addDoneQuestion()//add current index to set indexes done auestions
        quizViewModel.countDoneAnswers++//this show that one of the question
        tvCountDoneAnswers.setText("${quizViewModel.countDoneAnswers}")

        checkDoneAnswer()

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()

        showResultMessage()
    }

    //enable or disable buttons depending on whether the user answered the question or not
    private fun checkDoneAnswer() {
        if (quizViewModel.checkCurrentQuestionnDone()) {
            trueButton.isEnabled = false
            falseButton.isEnabled = false
        } else {
            trueButton.isEnabled = true
            falseButton.isEnabled = true
        }
    }

    private fun clearDataAboutPassingQuestion() {
        quizViewModel.clearUserAnswerData()
        trueButton.isEnabled = true
        falseButton.isEnabled = true
        tvCountDoneAnswers.setText("${quizViewModel.countDoneAnswers}")
    }

    private fun showResultMessage() {
        if (quizViewModel.countDoneAnswers == quizViewModel.sizeOfQuestionList) {
            Toast.makeText(
                this,
                "Correct answers: ${quizViewModel.countRightAnswers} (${findPersent(quizViewModel.countRightAnswers)}%)" +
                        "\nWrong answers: ${quizViewModel.getCountWrongAns()} " +
                        "(${findPersent(quizViewModel.getCountWrongAns())}%)", Toast.LENGTH_LONG
            ).show()
        }
    }

    //Find percentage of a number
    private fun findPersent(a: Int, b: Int = quizViewModel.sizeOfQuestionList): String {
        return String.format("%.2f", (a.toDouble() / b.toDouble() * 100.0))
    }

    fun setPrimaryScreenInfo() {
        //set information about count of auestions and count of done answers
        tvCountDoneAnswers.setText("${quizViewModel.countDoneAnswers}")
        tvGeneralCountOfAnswers.setText("${quizViewModel.sizeOfQuestionList}")
        checkDoneAnswer()
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