package com.szaki.shoppinghelper.data

import java.io.Serializable

data class Shop(var name: String) : Serializable {

    var selected: Boolean = false

    constructor(name: String, inc: Boolean) : this(name) {
        selected = inc
    }
}