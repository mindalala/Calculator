package com.test.calculator.data


enum class RepositoryKey {
    FirstNumber,
    SecondNumber,
    Symbol,
    Result;
}

interface Repository {

    fun getByKey(key : RepositoryKey) : String

    fun update(key :RepositoryKey, number: String)

    fun clear()

}