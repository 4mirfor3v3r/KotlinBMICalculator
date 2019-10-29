package com.amier.bmicalculator

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.amier.bmicalculator.sqlite.Data
import com.amier.bmicalculator.sqlite.SQLiteHelper
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DecimalFormat



class WeightDiary : AppCompatActivity() {
    private val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = supportActionBar
        toolbar?.title = "Weight Diary"
        main_recycle.adapter = adapter
        streamData()
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
        val bmi = intent.getDoubleExtra("BMI",0.0)
        text_now.text = DecimalFormat("##.##").format(bmi)
        val normal = intent.getDoubleExtra("Normal",0.0)
        text_ideal.text = "${DecimalFormat("##.##").format(normal)} kg"
    }

    private fun streamData(){
            val dataList = SQLiteHelper(this).fetchAll()
            for (item in dataList){
                adapter.add(MainItem(
                    Data(
                        item.id,
                        item.day,
                        item.date,
                        item.weight,
                        item.bmi,
                        item.icon,
                        item.div
                    )
                ))
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_add){
            val intent = Intent(this, Calculator::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
