package com.amier.bmicalculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.amier.bmicalculator.sqlite.Data
import com.amier.bmicalculator.sqlite.SQLiteHelper
import kotlinx.android.synthetic.main.activity_calculator.*
import java.text.DecimalFormat
import java.util.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class Calculator : AppCompatActivity() {
    private var male:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)
        calculator_male.setOnClickListener {
            it.setBackgroundResource(R.drawable.ic_btn_active)
            calculator_female.setBackgroundResource(R.drawable.ic_btn_deactive)
            male = true
        }
        calculator_female.setOnClickListener {
            it.setBackgroundResource(R.drawable.ic_btn_active)
            calculator_male.setBackgroundResource(R.drawable.ic_btn_deactive)
            male = false
        }
        calculator_plus_btn.setOnClickListener { weightControl(true) }
        calculator_minus_btn.setOnClickListener { weightControl(false) }
        calculator_btn.setOnClickListener { performCalculate() }
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
    private fun weightControl(plus:Boolean){
        val w = calculator_weight.text.toString().toDouble()
        if(plus) {
            calculator_weight.setText((w+1).toString())
        }
        else if(w > 0) {
            calculator_weight.setText((w-1).toString())
        }
    }

    private fun performCalculate(){
        val w = calculator_weight.text.toString().toDouble()
        val h = calculator_height.text.toString().toDouble() / 100
        val broscaHeight = calculator_height.text.toString().toDouble()
        val a = calculator_age.text.toString().toDouble()
        val value: Double
        var normal = 0.0

        if(a>=10){
            value = (w/(h*h))

            if(male){
                normal = (broscaHeight-100) - ((broscaHeight-100) * 0.10)
            }
            else if(!male) normal = (broscaHeight-100) - ((broscaHeight-100) * 0.15)
        }
        else{
            value = (w/(h*h))

            normal = (2 * a) + 8
        }
        val n = normal
        val intent = Intent(this, WeightDiary::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)

        val calendar = Calendar.getInstance()
        val month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        val day = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
        val date = calendar.get(Calendar.DAY_OF_MONTH).toString()

        val weight = w.toString()

        intent.putExtra("BMI", value)
        intent.putExtra("Normal",n)

        val v = DecimalFormat("##.##").format(value)
        var icon = ""
        var div = ""
        val raw = SQLiteHelper(this).fetchLast()
        if(raw.weight.isNotEmpty()) {
            if (raw.weight.toDouble() > weight.toDouble()) {
                icon = "0"
                div = "${raw.weight.toDouble() - weight.toDouble()} kg"
            } else if (raw.weight.toDouble() < weight.toDouble()) {
                icon = "1"
                div = "${weight.toDouble() - raw.weight.toDouble()} kg"
            }
            else{
                icon = " "
                div = " 0.0 kg"
            }
        }
        val data = Data(0, day, "$month $date", weight, v, icon, div)
        SQLiteHelper(this).addData(data)
        startActivity(intent)
    }
}
