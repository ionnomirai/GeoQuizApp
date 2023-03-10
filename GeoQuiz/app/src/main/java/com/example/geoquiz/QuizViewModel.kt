package com.example.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel(){
    init{
        //we will see this message, when object will be created
        Log.d(TAG, "ViewModel instance created")
    }

    /* This method will be called when this ViewModel is no longer
        used and will be destroyed*/
    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel instance destroyed")
    }
}

