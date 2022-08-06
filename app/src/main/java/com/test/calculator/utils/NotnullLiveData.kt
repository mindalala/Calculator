package com.test.calculator.utils

import androidx.lifecycle.MutableLiveData

class NotnullLiveData<T>(initData: T) : MutableLiveData<T>(initData) {

    override fun getValue(): T {
        return super.getValue()!!
    }

    override fun setValue(value: T) {
        if(value == null){
            throw NullPointerException()
        }
        super.setValue(value)
    }
}