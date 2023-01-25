package com.vholodynskyi.assignment.data.remote.dto

import com.squareup.moshi.JsonClass
import com.vholodynskyi.assignment.api.contacts.ApiContact

@JsonClass(generateAdapter = true)
data class ApiContactResponse(val results: List<ApiContact>?)