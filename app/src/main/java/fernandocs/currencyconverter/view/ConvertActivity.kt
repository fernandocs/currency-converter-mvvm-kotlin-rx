package fernandocs.currencyconverter.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import fernandocs.currencyconverter.App
import fernandocs.currencyconverter.R
import fernandocs.currencyconverter.model.Rate
import kotlinx.android.synthetic.main.activity_convert.*

class ConvertActivity : AppCompatActivity() {

    private var rate: Rate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_convert)

        if (intent.hasExtra("rate")) {
            rate = intent.getParcelableExtra("rate")

            if (rate != null) {
                textViewBaseCurrency.text = App.getBaseCurrency(this)
                editTextValue.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(editable: Editable?) {
                        try {
                            val number = editable.toString().toDouble()
                            editTextConvertedValue.setText((number * rate!!.value).toString())
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }
                })
                editTextValue.setText("1.0")

                textViewConvertTo.text = rate!!.name
                editTextConvertedValue.setText(rate!!.value.toString())
            }
        }
    }
}
