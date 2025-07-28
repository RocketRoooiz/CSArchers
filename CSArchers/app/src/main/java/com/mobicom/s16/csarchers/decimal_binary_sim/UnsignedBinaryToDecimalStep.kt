package com.mobicom.s16.csarchers.decimal_binary_sim

import android.os.Parcel
import android.os.Parcelable

data class UnsignedBinaryToDecimalStep(
    val bit: Long,
    val exponent: Int,
    val powerOfTwo: Long,
    val product: Long
) : Parcelable {
    constructor(parcel: Parcel) : this(
        bit = parcel.readLong(),
        exponent = parcel.readInt(),
        powerOfTwo = parcel.readLong(),
        product = parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(bit)
        parcel.writeInt(exponent)
        parcel.writeLong(powerOfTwo)
        parcel.writeLong(product)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UnsignedBinaryToDecimalStep> {
        override fun createFromParcel(parcel: Parcel): UnsignedBinaryToDecimalStep {
            return UnsignedBinaryToDecimalStep(parcel)
        }

        override fun newArray(size: Int): Array<UnsignedBinaryToDecimalStep?> {
            return arrayOfNulls(size)
        }
    }
}