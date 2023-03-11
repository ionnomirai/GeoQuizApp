package com.example.geoquiz

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"
const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"

class QuizViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    init {
        //we will see this message, when object will be created
        Log.d(TAG, "ViewModel instance created")
    }

    //List of question for quiz (A better way is database)
    private val questionBank = listOf<Question>(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    //current index question in questionBank (list of question)
    private var currentIndex: Int
        get() = savedStateHandle.get(CURRENT_INDEX_KEY) ?: 0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)

    fun teststate(){
        Log.d(TAG, "${savedStateHandle.get(CURRENT_INDEX_KEY)?:-1}")
    }

    var countDoneAnswers = 0 //total number of completed answers
    var countRightAnswers = 0//total number of right answers
    private val setIndexDoneQuesiotn = mutableSetOf<Int>()

    /*save in variable correct answer on the current quesion (true or false)*/
    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    val sizeOfQuestionList: Int
        get() = questionBank.size

    //calculate next index
    fun moveToNext() {
        /*the point is to incrementally increase the current index,
        but when it equals the size of questionBank, then immediately equate it to 0*/
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    //calculate prev index
    fun moveToPrev(){
        /*if currentIndex equals 0 then make currentIndex = 5
        * In other cases we will go through the list in backward direction*/
        currentIndex = if(currentIndex == 0){
            5
        } else {
            (currentIndex - 1) % questionBank.size
        }
    }

    //add to set a question that user answered
    fun addDoneQuestion(){
        setIndexDoneQuesiotn.add(currentIndex)
    }

    //check the user has already answered this question or not
    fun checkCurrentQuestionnDone(): Boolean{
        return setIndexDoneQuesiotn.contains(currentIndex)
    }

    fun clearUserAnswerData(){
        setIndexDoneQuesiotn.clear()
        countDoneAnswers = 0
        countRightAnswers = 0
    }

    fun getCountWrongAns(): Int{
        return sizeOfQuestionList - countRightAnswers
    }

    /* This method will be called when this ViewModel is no longer
        used and will be destroyed*/
    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel instance destroyed")
    }
}

