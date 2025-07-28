package com.mobicom.s16.csarchers.decimal_binary_sim

import android.os.Parcel
import android.os.Parcelable

data class UnsignedDecimalToBinaryStep(
    val dividend: Long,
    val quotient: Long,
    val remainder: Long,
    val bit: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        dividend = parcel.readLong(),
        quotient = parcel.readLong(),
        remainder = parcel.readLong(),
        bit = parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(dividend)
        parcel.writeLong(quotient)
        parcel.writeLong(remainder)
        parcel.writeInt(bit)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UnsignedDecimalToBinaryStep> {
        override fun createFromParcel(parcel: Parcel): UnsignedDecimalToBinaryStep {
            return UnsignedDecimalToBinaryStep(parcel)
        }

        override fun newArray(size: Int): Array<UnsignedDecimalToBinaryStep?> {
            return arrayOfNulls(size)
        }
    }
}