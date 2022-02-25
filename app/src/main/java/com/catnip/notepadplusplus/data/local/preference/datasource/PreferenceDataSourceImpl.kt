package com.catnip.notepadplusplus.data.local.preference.datasource

import com.catnip.notepadplusplus.data.local.preference.UserPreference

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
class PreferenceDataSourceImpl(
    private val userPreference: UserPreference
) : PreferenceDataSource {
    override fun setPassword(password: String) {
        userPreference.password = password
    }
    override fun getPassword(): String? = userPreference.password

    override fun isPasswordExist(): Boolean = !userPreference.password.isNullOrEmpty()
}