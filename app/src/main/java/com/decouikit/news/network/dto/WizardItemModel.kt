package com.decouikit.news.network.dto

import android.os.Parcel
import android.os.Parcelable

data class WizardItemModel(
    val title: String?, val subtitle: String?, var image: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(subtitle)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WizardItemModel> {
        override fun createFromParcel(parcel: Parcel): WizardItemModel {
            return WizardItemModel(parcel)
        }

        override fun newArray(size: Int): Array<WizardItemModel?> {
            return arrayOfNulls(size)
        }
    }
}