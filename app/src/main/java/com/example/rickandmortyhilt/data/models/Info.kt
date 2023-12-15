package com.example.rickandmortyhilt.data.models

import android.net.Uri
import androidx.paging.Pager

data class Info(
    val count: Int?,
    val next: String?,
    val pages: Int?,
    val prev: String?,
) {
    companion object {
        const val Page = "page"
    }

    fun getNextPageInt(): Int? {
        return next?.let {
            Uri.parse(it).getQueryParameter(Page)?.toInt()
        }
    }

    fun getPreviousPage(): Int? {
        return prev?.let {
            Uri.parse(prev).getQueryParameter(Page)?.toInt()
        }
    }
}