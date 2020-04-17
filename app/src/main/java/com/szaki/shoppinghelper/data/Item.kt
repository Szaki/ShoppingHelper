package com.szaki.shoppinghelper.data

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class Item(var name: String?, var cat: String?, var num: Double) : Parcelable, Serializable {

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(name)
        dest?.writeString(cat)
        dest?.writeDouble(num)
    }

    override fun describeContents(): Int = 0

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readDouble())

    companion object CREATOR : Parcelable.Creator<Item> {
        override fun createFromParcel(parcel: Parcel): Item {
            return Item(parcel)
        }

        override fun newArray(size: Int): Array<Item?> {
            return arrayOfNulls(size)
        }
    }

}