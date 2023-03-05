package com.example.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton:Button
    private lateinit var falseButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)

        trueButton.setOnClickListener { view: View ->
            //action
            /*устновка выведения toast сверху посреди экрана
            * здесь важен порядок, если я вызову setGravity перед makeText
            * или создам Toast(this).setGravity - то это не сработает, программа будет
            * считать что я присвоил функцию переменной
            * Что имею в виду: если сейчас навести на перемнную toast, то отобразит,
            * что это тип данных Toast!, а если Toast(this).setGravity то отобразит,
            * что это Unit (это функция)
            *
            * Вывод: правильная формула, это сначала создать Toast, установить его параметры
            * (context, text, duration), а затем изменять его другие характеристики*/
            val toast = Toast
                .makeText(this, R.string.correct_toast, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.TOP, 0, 0)
            toast.show()
        }

        falseButton.setOnClickListener { view: View ->
            //стандартное выведение toast
            Toast.makeText(this, R.string.incorrect_toast, Toast.LENGTH_SHORT).show()
        }
    }
}