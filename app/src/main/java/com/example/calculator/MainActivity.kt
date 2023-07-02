package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.calculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var lastNumeric = false
    var stateError = false
    var lastDot = false

    private lateinit var expression: Expression
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onEqualClick(view: View) {
        if (lastNumeric){
            onEqual()
            binding.calculate.text=binding.result.text.toString().drop(1)
        }

    }
    fun onDigitClick(view: View) {
        if (stateError){
            binding.calculate.text = (view as Button).text
            stateError = false
        }
        else{
            binding.calculate.append((view as Button).text)
        }
        lastNumeric = true
        onEqual()
    }
    fun onAllclearClick(view: View) {
        binding.calculate.text=""
        binding.result.text=""
        stateError=false
        lastDot=false
        lastNumeric=false
        binding.result.visibility=View.GONE
    }
    fun onOperatorClick(view: View) {
        if(!stateError && lastNumeric){
            binding.calculate.append((view as Button).text)
            lastNumeric =false
            lastDot = false
        }
    }
    fun onClearClick(view: View) {
        binding.calculate.text =""
        lastNumeric = false
    }
    fun onBackClick(view: View) {
        binding.calculate.text =binding.calculate.text.dropLast(1)

        try {
            val lastChar = binding.calculate.text.last()
            if(lastChar.isDigit()){
                onEqual()
            }
        }catch (e : Exception){
            binding.result.text =""
            binding.result.visibility = View.GONE
            Log.e("lastchar error", e.toString())
        }
    }

    fun onEqual(){
        if (lastNumeric && !stateError){
            val text = binding.calculate.text.toString()
            expression =ExpressionBuilder(text).build()
            try {
                val result = expression.evaluate()
                binding.result.visibility=View.VISIBLE
                binding.result.text= "=" + result.toString()
            }catch (e:java.lang.ArithmeticException){
                Log.e("evaluate error",e.toString())
                binding.result.text ="Error"
                stateError=true
                lastNumeric=false
            }
        }
    }
}