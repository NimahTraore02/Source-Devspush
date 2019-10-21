package com.decouikit.news.network.dto

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Rendered(
    @SerializedName("rendered") val rendered: String,
    @SerializedName("protected") val protected: Boolean
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString().toString(),
        1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(rendered)
        writeInt((if (protected) 1 else 0))
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Rendered> = object : Parcelable.Creator<Rendered> {
            override fun createFromParcel(source: Parcel): Rendered = Rendered(source)
            override fun newArray(size: Int): Array<Rendered?> = arrayOfNulls(size)
        }
    }
}