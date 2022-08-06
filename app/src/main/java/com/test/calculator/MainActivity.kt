package com.test.calculator

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import com.test.calculator.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val TAG = "App ${javaClass.simpleName}"

    private var binding : ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
        binding = ActivityMainBinding.inflate(layoutInflater)

        if(resources.configuration.orientation ==Configuration.ORIENTATION_LANDSCAPE){
            val displayLayoutParams = binding!!.displayFragmentContainerView.layoutParams as ConstraintLayout.LayoutParams
            displayLayoutParams.apply {
                startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                endToStart = R.id.calculatorFragmentContainerView
                topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                endToEnd = ConstraintLayout.LayoutParams.UNSET
                bottomToTop = ConstraintLayout.LayoutParams.UNSET
            }

            val calculatorLayoutParams = binding!!.calculatorFragmentContainerView.layoutParams as ConstraintLayout.LayoutParams
            calculatorLayoutParams.apply {
                startToEnd = R.id.displayFragmentContainerView
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                startToStart = ConstraintLayout.LayoutParams.UNSET
                topToBottom = ConstraintLayout.LayoutParams.UNSET
            }

            binding!!.displayFragmentContainerView.layoutParams = displayLayoutParams
            binding!!.calculatorFragmentContainerView.layoutParams = calculatorLayoutParams
        }

        setContentView(binding!!.root)



        val fragmentTransaction = supportFragmentManager.beginTransaction()

        fragmentTransaction.add(R.id.displayFragmentContainerView, DisplayFragment(),DisplayFragment::class.java.name)
        fragmentTransaction.add(R.id.calculatorFragmentContainerView, CalculatorFragment(), CalculatorFragment::class.java.name)

        fragmentTransaction.commit()

    }
}