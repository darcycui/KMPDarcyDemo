package com.darcy.kmpdemo.bean.ui

import com.darcy.kmpdemo.bean.IEntity
import com.darcy.kmpdemo.navigation.Pages

data class HomeItemBean(
    val id: Int = -1,
    var title: String = "",
    val pages: Pages,
) : IEntity