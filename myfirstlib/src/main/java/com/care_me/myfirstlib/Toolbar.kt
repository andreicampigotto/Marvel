package com.care_me.myfirstlib

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.care_me.myfirstlib.databinding.ToolbarBinding

class Toolbar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet?

): ConstraintLayout(context, attrs) {

    private val binding: ToolbarBinding = ToolbarBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    fun enableBackButton(){
        binding.btBack.visibility = View.VISIBLE
    }

    fun clickBackButton(callback:()-> Unit){
        binding.btBack.setOnClickListener {
            callback()
        }
    }
}