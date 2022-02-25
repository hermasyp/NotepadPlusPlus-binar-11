package com.catnip.notepadplusplus.data.local.preference.datasource

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
interface PreferenceDataSource {
    fun setPassword(password : String)
    fun getPassword() : String?
    fun isPasswordExist() : Boolean
}