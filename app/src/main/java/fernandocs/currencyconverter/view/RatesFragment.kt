package fernandocs.currencyconverter.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mynameismidori.currencypicker.CurrencyPicker
import fernandocs.currencyconverter.App
import fernandocs.currencyconverter.R
import fernandocs.currencyconverter.model.Rate
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.item_view_rate.view.*
import kotlinx.android.synthetic.main.rates_fragment.*
import retrofit2.HttpException


class RatesFragment : MvvmFragment() {

    private var rates = mutableListOf<Rate>()
    private val currencyViewModel = App.injectCurrencyViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.rates_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewRates.adapter = RatesAdapter(rates,
                { rate, _ ->
                    startActivity(
                            Intent(activity, ConvertActivity::class.java).apply {
                                putExtra("rate", rate)
                            }
                    )
                })

        textViewRateName.text = App.getBaseCurrency(activity)
        textViewRateName.setCompoundDrawablesWithIntrinsicBounds(App.getDrawableByName(activity, App.getBaseCurrency(activity)), null, null, null)

        val picker = CurrencyPicker.newInstance(getString(R.string.choose_base_currency))
        picker.setListener { _, code, _, _ ->
            App.setBaseCurrency(activity, code)
            textViewRateName.text = App.getBaseCurrency(activity)
            textViewRateName.setCompoundDrawablesWithIntrinsicBounds(App.getDrawableByName(activity, App.getBaseCurrency(activity)), null, null, null)
            loadRates()
            picker.dismiss()
        }

        buttonBaseRate.setOnClickListener({
            picker.show(activity.supportFragmentManager, "CURRENCY_PICKER")
        })

        textViewErrorGetRates.setOnClickListener({
            loadRates()
        })

        loadRates()
    }

    private fun loadRates() {
        rates.clear()

        progressBar.visibility = View.VISIBLE
        textViewErrorGetRates.visibility = View.GONE

        subscribe(currencyViewModel.getRates(App.getBaseCurrency(activity))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { rates.add(it) },
                        {
                            recyclerViewRates.adapter.notifyDataSetChanged()
                            progressBar.visibility = View.GONE
                            if (it is HttpException) {
                                textViewErrorGetRates.setText(R.string.error_get_rates)
                                textViewErrorGetRates.isClickable = false
                            } else {
                                textViewErrorGetRates.setText(R.string.internet_error_get_rates)
                                textViewErrorGetRates.isClickable = true
                            }
                            textViewErrorGetRates.visibility = View.VISIBLE
                            it.printStackTrace()
                        },
                        {
                            progressBar.visibility = View.GONE
                            recyclerViewRates.adapter.notifyDataSetChanged()
                        }))
    }

    class RatesAdapter(private val list: List<Rate>, private val itemClick: (Rate, Int) -> Unit) :
            RecyclerView.Adapter<RatesAdapter.ViewHolder>() {

        override fun getItemCount() = list.size

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_view_rate,
                    parent, false)
            return ViewHolder(view, itemClick)
        }

        override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
            holder?.bind(list[position], position)
        }

        class ViewHolder(view: View, private val itemClick: (Rate, Int) -> Unit) :
                RecyclerView.ViewHolder(view) {
            fun bind(item: Rate, position: Int) {
                with(item) {
                    itemView.textViewRateName.text = name
                    itemView.textViewRateName
                            .setCompoundDrawablesWithIntrinsicBounds(
                                    getDrawableByName(itemView.context),
                                    null, null, null)
                    itemView.textViewRateValue.text = String.format("%.${4}f", value)
                    itemView.setOnClickListener { itemClick(this, position) }
                }
            }
        }
    }
}