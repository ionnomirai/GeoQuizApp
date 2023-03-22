package com.example.geoquiz

import android.nfc.Tag
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"
const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"
const val SET_OF_DONE_QUESTIONS = "SET_OF_DONE_QUESTIONS"
const val COUNT_DONE_ANSWERS = "COUNT_DONE_ANSWERS"
const val COUNT_RIGHT_ANSWERS = "COUNT_RIGHT_ANSWERS"
const val IS_CHEATER_KEY = "IS_CHEATER_KEY"
const val SET_OF_CHEAT_QUESTIONS = "SET_OF_CHEAT_QUESTIONS"

class QuizViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
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

    //private var currentIndex: Int = 0

    fun teststate() {
        Log.d(TAG, "${savedStateHandle.get(CURRENT_INDEX_KEY) ?: -1}")
    }

    //total number of completed answers
    var countDoneAnswers: Int
        get() = savedStateHandle.get(COUNT_DONE_ANSWERS) ?: 0
        set(value) = savedStateHandle.set(COUNT_DONE_ANSWERS, value)

    //total number of right answers
    var countRightAnswers: Int
        get() = savedStateHandle.get(COUNT_RIGHT_ANSWERS) ?: 0
        set(value) = savedStateHandle.set(COUNT_RIGHT_ANSWERS, value)

    private var setIndexDoneQuesiotn = mutableSetOf<Int>()
    private var setIndexCheatQuestion = mutableSetOf<Int>()

    /*save in variable correct answer on the current quesion (true or false)*/
    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    val sizeOfQuestionList: Int
        get() = questionBank.size

    var isCheater: Boolean
        get() = savedStateHandle.get(IS_CHEATER_KEY) ?: false
        set(value) = savedStateHandle.set(IS_CHEATER_KEY, value)

    init {
        //we will see this message, when object will be created
//        Log.d(TAG, "ViewModel instance created")

        /*if set from ViewModel is not equal set from savedStateHandle
            and set from savedStateHandle it is not null  -- than do it,
            if it not exist create empty set
         otherwise get info from ViewModel

         The purpose of this if it is to save time accessing data.
         If possible, then we will take from the ViewModel because it is faster,
         but if the ViewModel is removed from memory,
         then we restore the data from the SaveStateNandle */
        if (!setIndexDoneQuesiotn.equals(savedStateHandle.get(SET_OF_DONE_QUESTIONS)) &&
            savedStateHandle.get<MutableSet<Int>>(SET_OF_DONE_QUESTIONS) != null
        ) {
            setIndexDoneQuesiotn = savedStateHandle.get(SET_OF_DONE_QUESTIONS) ?: mutableSetOf()
            Log.d(TAG, "in init done set")
        }

        if (!setIndexCheatQuestion.equals(savedStateHandle.get(SET_OF_CHEAT_QUESTIONS)) &&
            savedStateHandle.get<MutableSet<Int>>(SET_OF_CHEAT_QUESTIONS) != null
        ) {
            setIndexCheatQuestion = savedStateHandle.get(SET_OF_CHEAT_QUESTIONS) ?: mutableSetOf()
            Log.d(TAG, "in init cheat set")
        }
    }

    //calculate next index
    fun moveToNext() {
        /*the point is to incrementally increase the current index,
        but when it equals the size of questionBank, then immediately equate it to 0*/
        currentIndex = (currentIndex + 1) % questionBank.size
        //teststate()
    }

    //calculate prev index
    fun moveToPrev() {
        /*if currentIndex equals 0 then make currentIndex = 5
        * In other cases we will go through the list in backward direction*/
        currentIndex = if (currentIndex == 0) {
            5
        } else {
            (currentIndex - 1) % questionBank.size
        }
    }

    //add to set a question that user answered
    fun addDoneQuestion() {
        setIndexDoneQuesiotn.add(currentIndex)
        savedStateHandle.set(SET_OF_DONE_QUESTIONS, setIndexDoneQuesiotn)
    }

    //check the user has already answered this question or not
    fun checkCurrentQuestionnDone(): Boolean {
        return setIndexDoneQuesiotn.contains(currentIndex)
    }

    //add index question with cheat answer
    fun addCheatQuestion(){
        setIndexCheatQuestion.add(currentIndex)
        savedStateHandle.set(SET_OF_CHEAT_QUESTIONS, setIndexCheatQuestion)
    }

    fun checkCurrentQuestionCheat(): Boolean{
        return setIndexCheatQuestion.contains(currentIndex)
    }

    fun clearUserAnswerData() {
        setIndexDoneQuesiotn.clear()
        savedStateHandle.set(SET_OF_DONE_QUESTIONS, setIndexDoneQuesiotn)
        setIndexCheatQuestion.clear()
        savedStateHandle.set(SET_OF_CHEAT_QUESTIONS, setIndexCheatQuestion)
        countDoneAnswers = 0
        countRightAnswers = 0
    }

    fun getCountWrongAns(): Int {
        return sizeOfQuestionList - countRightAnswers
    }

    /* This method will be called when this ViewModel is no longer
        used and will be destroyed*/
    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel instance destroyed")
    }
}

