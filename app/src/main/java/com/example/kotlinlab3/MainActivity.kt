package com.example.kotlinlab3

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale
import kotlin.math.sqrt
import kotlin.math.PI
import kotlin.math.exp

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //введення значень
        val power_input: EditText = findViewById(R.id.input_power)
        val error1_input: EditText = findViewById(R.id.input_error1)
        val error2_input: EditText = findViewById(R.id.input_error2)
        val price_input: EditText = findViewById(R.id.input_price)

        //результати обчислень
        val result: TextView = findViewById(R.id.result)

        val sumButton: Button = findViewById(R.id.button)

        sumButton.setOnClickListener {
            val value_power = power_input.text.toString()
            val value_error1 = error1_input.text.toString()
            val value_error2 = error2_input.text.toString()
            val value_price = price_input.text.toString()


            // перевірка на наявність значень
            if (value_power.isNotEmpty() && value_error1.isNotEmpty() && value_error2.isNotEmpty()
                && value_price.isNotEmpty()
            ) {
                try {
                    val value_power_f = value_power.toFloat()
                    val value_error1_f = value_error1.toFloat()
                    val value_error2_f = value_error2.toFloat()
                    val value_price_f = value_price.toFloat()

                    if (value_error2_f > 0 || value_error2_f < 100) {
                        val temp = value_power_f * (value_error2_f/100).toDouble()
                        val integral_a = value_power_f - temp
                        val integral_b = value_power_f + temp
                        val function: (Double) -> Double = { p ->
                            1 / (value_error1_f * sqrt(2 * PI)) * exp(-(((p - value_power_f) *
                                    (p - value_power_f)) / (2 * value_error1_f * value_error1_f)))
                        }

                        val qw1 = trapezoidalIntegral(function, integral_a, integral_b, 1000)

                        val w1 = value_power_f * 24 * qw1 * value_price_f
                        val w2 = value_power_f * 24 * (1 - qw1) * value_price_f
                        val profit = w1 - w2

                        //вивід отриманих значень
                        result.text = String.format(Locale.US, "%.2f", profit)
                    } else {
                        Toast.makeText(this, "Допустима похибка повинна бути від 0 до 100%", Toast.LENGTH_SHORT).show()
                    }

                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "Введіть коректні числа.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Всі поля повинні бути заповненні.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
    fun trapezoidalIntegral(
        function: (Double) -> Double,
        start: Double,
        end: Double,
        intervals: Int
    ): Double {
        val h = (end - start) / intervals
        var integral = (function(start) + function(end)) / 2.0

        for (i in 1 until intervals) {
            val x = start + i * h
            integral += function(x)
        }

        return integral * h
    }
