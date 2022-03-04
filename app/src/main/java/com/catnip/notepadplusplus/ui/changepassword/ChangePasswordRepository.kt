package com.catnip.notepadplusplus.ui.changepassword

import com.catnip.notepadplusplus.data.local.preference.UserPreference
import com.catnip.notepadplusplus.data.local.preference.datasource.PreferenceDataSource

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
class ChangePasswordRepository(private val dataSource: PreferenceDataSource) :
    ChangePasswordContract.Repository {
    override fun getUserPassword(): String {
        return dataSource.getPassword().orEmpty()
    }

    override fun setUserPassword(newPassword: String) {
        dataSource.setPassword(newPassword)
    }

}