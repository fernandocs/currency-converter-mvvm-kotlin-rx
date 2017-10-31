package fernandocs.currencyconverter.model

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import fernandocs.currencyconverter.App

data class Result(@SerializedName("id") val base: String,
                  @SerializedName("date") val date: String,
                  @SerializedName("rates") val rates: Map<String, Double>,
                  @SerializedName("error") val error: String)

data class Rate(val name: String,
                val value: Double) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readDouble()) {
    }

    fun getDrawableByName(context: Context): Drawable? {
        return App.getDrawableByName(context, name)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeDouble(value)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Rate> {
        override fun createFromParcel(parcel: Parcel): Rate {
            return Rate(parcel)
        }

        override fun newArray(size: Int): Array<Rate?> {
            return arrayOfNulls(size)
        }
    }
}