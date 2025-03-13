package com.example.bdapplication.model

data class Item(
    var id: Int = 0,
    var name: String = "",
    var description: String = ""
) {
    constructor() : this(0, "", "")
}
