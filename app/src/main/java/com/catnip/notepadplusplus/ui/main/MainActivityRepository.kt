package com.catnip.notepadplusplus.ui.main

import com.catnip.notepadplusplus.data.local.preference.UserPreference
import com.catnip.notepadplusplus.data.local.preference.datasource.PreferenceDataSource

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
class MainActivityRepository(private val dataSource: PreferenceDataSource) :
    MainActivityContract.Repository {
    override fun isPasswordExist(): Boolean {
        return dataSource.isPasswordExist()
    }
}