package com.test.calculator

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.test.calculator.data.Repository
import com.test.calculator.data.RepositoryKey
import com.test.calculator.service.CalculatorService
import com.test.calculator.utils.NotnullLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val repository: Repository,
    private val calculatorService: CalculatorService,
) : ViewModel() {

    private val _firstNumberLiveData = NotnullLiveData(repository.getByKey(RepositoryKey.FirstNumber))
    val firstNumberLiveData : LiveData<String>
        get() = _firstNumberLiveData

    private val _secondNumberLiveData = NotnullLiveData(repository.getByKey(RepositoryKey.SecondNumber))
    val secondNumberLiveData : LiveData<String>
        get() = _secondNumberLiveData

    private val _symbolLiveData = NotnullLiveData(repository.getByKey(RepositoryKey.Symbol))
    val symbolLiveData : LiveData<String>
        get() = _symbolLiveData

    private val _resultLiveData = NotnullLiveData(repository.getByKey(RepositoryKey.Result))
    val resultLiveData : LiveData<String>
        get() = _resultLiveData

    private val _totalStateLiveData = MediatorLiveData<String>()
    val totalStateLiveData : LiveData<String>
        get() = _totalStateLiveData

    init {

        val setTotalState = {
            _totalStateLiveData.value =
                "${_firstNumberLiveData.value} ${_symbolLiveData.value} ${_secondNumberLiveData.value}"
        }
        _totalStateLiveData.addSource(_firstNumberLiveData) {
            repository.update(RepositoryKey.FirstNumber,it)
            setTotalState()
        }
        _totalStateLiveData.addSource(_secondNumberLiveData) {
            repository.update(RepositoryKey.SecondNumber,it)
            setTotalState()
        }
        _totalStateLiveData.addSource(_symbolLiveData) {
            repository.update(RepositoryKey.Symbol,it)
            setTotalState()
        }
    }

    fun onClickNumberButton(numberString: String) {
        if (TextUtils.isEmpty(_symbolLiveData.value)) {
            _firstNumberLiveData.value = calculatorService.getNumberText(_firstNumberLiveData.value, numberString)
            return
        }

        if(TextUtils.isEmpty(_firstNumberLiveData.value)){
            _firstNumberLiveData.value = calculatorService.getNumberText(_firstNumberLiveData.value, numberString)
            return
        }

        _secondNumberLiveData.value = calculatorService.getNumberText(_secondNumberLiveData.value, numberString)
    }

    private fun calculate() {
        _resultLiveData.value = "= " + calculatorService.calculate(
            _firstNumberLiveData.value,
            _symbolLiveData.value,
            _secondNumberLiveData.value
        )
        repository.update(RepositoryKey.Result ,_resultLiveData.value)
    }

    fun onClickSymbolButton(symbol: String) {
        val beforeSymbol = _symbolLiveData.value

        _symbolLiveData.value = symbol

        if (TextUtils.isEmpty(beforeSymbol)) return
        if (TextUtils.isEmpty(_secondNumberLiveData.value)) return

        calculate()
    }

    fun onClickClearButton() {
        _firstNumberLiveData.value = ""
        _secondNumberLiveData.value = ""
        _symbolLiveData.value = ""
        _resultLiveData.value = ""
        repository.clear()
    }

    fun onClickEqualButton() {

        if (TextUtils.isEmpty(_symbolLiveData.value)) return
        if (TextUtils.isEmpty(_firstNumberLiveData.value)) return
        if (TextUtils.isEmpty(_secondNumberLiveData.value)) return

        calculate()
    }


}