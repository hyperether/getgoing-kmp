package com.hyperether.getgoing_kmp.android.listeners

import com.hyperether.getgoing_kmp.repository.room.Route

interface AdapterOnItemClickListener {
    fun onClick(route: Route, i: Int)
    fun onClickText(route: Route)
}