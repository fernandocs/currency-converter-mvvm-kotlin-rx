package fernandocs.currencyconverter.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fernandocs.currencyconverter.App
import fernandocs.currencyconverter.R
import fernandocs.currencyconverter.model.Rate
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.item_view_rate.view.*
import kotlinx.android.synthetic.main.rates_fragment.*

class RatesFragment : MvvmFragment() {

    private var rates = mutableListOf<Rate>()
    private val currencyViewModel = App.injectCurrencyViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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

        buttonBaseRate.text = App.getBaseCurrency(activity)
        buttonBaseRate.setOnClickListener({
            val b = AlertDialog.Builder(activity)
            b.setTitle("Choose base currency")
            val types = arrayOf(App.baseCurrency, "EUR", "BRL")
            b.setItems(types, {
                dialog, which ->
                dialog.dismiss()

                when(which) {
                    0 -> App.setBaseCurrency(activity, App.baseCurrency)
                    1 -> App.setBaseCurrency(activity, "EUR")
                    2 -> App.setBaseCurrency(activity, "BRL")
                }
                buttonBaseRate.text = App.getBaseCurrency(activity)
                loadRates()
            })

            b.show()
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
                    itemView.textViewRateValue.text = value.toString()
                    itemView.setOnClickListener { itemClick(this, position) }
                }
            }
        }
    }
}