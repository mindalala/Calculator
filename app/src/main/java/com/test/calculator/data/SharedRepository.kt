package com.test.calculator.data

import android.content.SharedPreferences


class SharedRepository constructor(
    private val sharedPreferences: SharedPreferences,
) : Repository {
    override fun getByKey(key: RepositoryKey): String {
        return sharedPreferences.getString(key.name, "")!!
    }

    override fun update(key: RepositoryKey, number: String ) {
        sharedPreferences.edit().putString(key.name, number).apply()
    }

    override fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}