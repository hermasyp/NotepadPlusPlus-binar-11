package com.catnip.notepadplusplus.ui.enterpassword

import com.catnip.notepadplusplus.data.local.preference.datasource.PreferenceDataSource

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
class EnterPasswordRepository(private val dataSource: PreferenceDataSource) :
    EnterPasswordContract.Repository {
    override fun getUserPassword(): String {
        return dataSource.getPassword().orEmpty()
    }

}