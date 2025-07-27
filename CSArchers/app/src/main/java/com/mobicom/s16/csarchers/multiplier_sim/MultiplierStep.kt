package com.mobicom.s16.csarchers.multiplier_sim

import android.os.Parcel
import android.os.Parcelable

data class MultiplierStep(
    val bit: Int,
    val add_m: Boolean,
    val carry: Boolean,
    val a: ULong,
    val a_plus_m: ULong,
    val a_shr: ULong,
    val q: ULong,
    val q_shr: ULong
) : Parcelable {
    constructor(parcel: Parcel) : this(
        bit = parcel.readInt(),
        add_m = parcel.readByte() != 0.toByte(),
        carry = parcel.readByte() != 0.toByte(),
        a = parcel.readLong().toULong(),
        a_plus_m = parcel.readLong().toULong(),
        a_shr = parcel.readLong().toULong(),
        q = parcel.readLong().toULong(),
        q_shr = parcel.readLong().toULong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(bit)
        parcel.writeByte(if (add_m) 1 else 0)
        parcel.writeByte(if (carry) 1 else 0)
        parcel.writeLong(a.toLong())
        parcel.writeLong(a_plus_m.toLong())
        parcel.writeLong(a_shr.toLong())
        parcel.writeLong(q.toLong())
        parcel.writeLong(q_shr.toLong())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MultiplierStep> {
        override fun createFromParcel(parcel: Parcel): MultiplierStep {
            return MultiplierStep(parcel)
        }

        override fun newArray(size: Int): Array<MultiplierStep?> {
            return arrayOfNulls(size)
        }
    }
}