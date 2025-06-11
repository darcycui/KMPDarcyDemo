package com.darcy.kmpdemo.bean

import com.darcy.kmpdemo.bean.base.IEntity
import com.darcy.kmpdemo.navigation.Pages

data class HomeItemEntity(
    val id: Int = -1,
    var title: String = "",
    val pages: Pages,
) : IEntity
