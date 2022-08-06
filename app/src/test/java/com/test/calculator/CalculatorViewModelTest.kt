package com.test.calculator

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.test.calculator.data.Repository
import com.test.calculator.data.RepositoryKey
import com.test.calculator.service.CalculatorService
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


@RunWith(AndroidJUnit4::class)
class CalculatorViewModelTest{
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository : Repository
    lateinit var viewModel : CalculatorViewModel

    @Before
    fun setup(){
        MockitoAnnotations.openMocks(this)

        `when`(repository.getByKey(RepositoryKey.FirstNumber)).thenReturn("10")
        `when`(repository.getByKey(RepositoryKey.SecondNumber)).thenReturn("12")
        `when`(repository.getByKey(RepositoryKey.Symbol)).thenReturn("*")
        `when`(repository.getByKey(RepositoryKey.Result)).thenReturn("= 120")

        viewModel = CalculatorViewModel(repository, CalculatorService())

    }

    @Test
    fun init_saved_setAllValues(){

        assertEquals(viewModel.firstNumberLiveData.value,  "10")
        assertEquals(viewModel.secondNumberLiveData.value,  "12")
        assertEquals(viewModel.symbolLiveData.value,  "*")
        assertEquals(viewModel.resultLiveData.value,  "= 120")
        assertEquals(viewModel.totalStateLiveData.getOrAwaitValue(),  "10 * 12")
    }

    @Test
    fun onClickClearButton_afterClear_returnAllEmpty(){
        viewModel.onClickClearButton()
        assertEquals(viewModel.firstNumberLiveData.value,  "")
        assertEquals(viewModel.secondNumberLiveData.value,  "")
        assertEquals(viewModel.symbolLiveData.value,  "")
        assertEquals(viewModel.resultLiveData.value,  "")
    }

    @Test
    fun onClickNumberButton_putNumber_getFirstNumber(){
        viewModel.onClickClearButton()
        viewModel.onClickNumberButton("9")
        assertEquals( "9", viewModel.firstNumberLiveData.value)
    }

    @Test
    fun onClickNumberButton_putSymbolNumber_checkAllNumber(){
        viewModel.onClickClearButton()

        viewModel.onClickSymbolButton("*")
        viewModel.onClickNumberButton("9")
        assertEquals( "9", viewModel.firstNumberLiveData.value)
        assertEquals( "", viewModel.secondNumberLiveData.value)
    }

    @Test
    fun onClickNumberButton_putNumberSymbolNumber_checkAllNumber(){
        viewModel.onClickClearButton()

        viewModel.onClickNumberButton("10")
        viewModel.onClickSymbolButton("*")
        viewModel.onClickNumberButton("9")
        assertEquals( "10", viewModel.firstNumberLiveData.value)
        assertEquals( "9", viewModel.secondNumberLiveData.value)
    }

}

fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }

    this.observeForever(observer)

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}