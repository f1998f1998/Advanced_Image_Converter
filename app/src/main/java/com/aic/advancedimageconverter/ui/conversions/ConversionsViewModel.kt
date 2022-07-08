package com.aic.advancedimageconverter.ui.conversions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

class ConversionsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text

    val conversions = ConcurrentLinkedQueue<String>()

    fun a(){
        conversions




    }






















}