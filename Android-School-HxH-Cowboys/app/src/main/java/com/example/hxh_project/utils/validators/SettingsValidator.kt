package com.example.hxh_project.utils.validators

object SettingsValidator {
    fun isNameOrSurnameValid(name: String): Boolean {
        val regex = Regex("^[a-zA-Zа-яёА-ЯЁ]+\$")
        return regex.matches(name)
    }

    fun isJobValid(job: String): Boolean {
        return job.trim().isNotEmpty();
    }
}