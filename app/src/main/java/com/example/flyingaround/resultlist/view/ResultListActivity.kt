package com.example.flyingaround.resultlist.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flyingaround.R
import com.example.flyingaround.details.view.DetailsDialogFragment
import com.example.flyingaround.resultlist.model.usecase.GetFlightsResult
import com.example.flyingaround.resultlist.viewmodel.ResultListActivityViewModel
import com.example.flyingaround.utils.hide
import com.example.flyingaround.utils.include
import com.example.flyingaround.utils.show
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_result_list.*
import org.koin.android.viewmodel.ext.android.viewModel

class ResultListActivity : AppCompatActivity() {
    private val destroyDisposables = CompositeDisposable()

    private val viewModel: ResultListActivityViewModel by viewModel()
    private var originStation: String? = null
    private var destinationStation: String? = null
    private var departureTime: String? = null
    private var adults: Int = 0
    private var teens: Int = 0
    private var children: Int = 0

    private lateinit var adapter: ResultListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_list)

        adapter = ResultListAdapter(emptyList())
        resultListRecyclerView.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        resultListRecyclerView.adapter = adapter
        adapter.onClickListener = {
            DetailsDialogFragment.create(it)
                .showNow(supportFragmentManager, DetailsDialogFragment.TAG)
        }

        originStation = intent.getStringExtra(KEY_ORIGIN_STATION)
        destinationStation = intent.getStringExtra(KEY_DESTINATION_STATION)
        departureTime = intent.getStringExtra(KEY_DEPARTURE)
        adults = intent.getIntExtra(KEY_ADULTS, 0)
        teens = intent.getIntExtra(KEY_TEENS, 0)
        children = intent.getIntExtra(KEY_CHILDREN, 0)

        if (originStation != null || destinationStation != null) {
            toolbarResult.title = "$originStation -> $destinationStation"
            setSupportActionBar(toolbarResult)
        }

        destroyDisposables include viewModel.downloadFlights(
            requireNotNull(originStation),
            requireNotNull(destinationStation),
            requireNotNull(departureTime),
            adults,
            teens,
            children
        )
            .subscribe(::handleDownloadSuccess, ::handleError)

        destroyDisposables include viewModel.listOfFlightItems
            .subscribe { list ->
                (resultListRecyclerView.adapter as? ResultListAdapter)?.flightInfoItemList =
                    list
                (resultListRecyclerView.adapter as? ResultListAdapter)?.notifyDataSetChanged()
            }
    }

    private fun handleDownloadSuccess(result: GetFlightsResult) {
        progressBar.hide()
        when (result) {
            is GetFlightsResult.Success -> {
                if (result.isEmptyList) {
                    resultListRecyclerView.hide()
                    emptyResultTextView.show()
                } else {
                    resultListRecyclerView.show()
                }
            }
            is GetFlightsResult.Error ->
                handleError(result.throwable)
        }
    }

    private fun handleError(throwable: Throwable) {
        Log.e("ResultListActivity", "error: ${throwable.localizedMessage}")
        val snackbar = Snackbar.make(
            rootView,
            "Wrong search response",
            Snackbar.LENGTH_INDEFINITE
        )
        snackbar.setAction(getString(R.string.retry)) {
            snackbar.dismiss()
            finish()
        }
        snackbar.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyDisposables.dispose()
    }


    companion object {
        private const val KEY_ORIGIN_STATION = "key_origin_station"
        private const val KEY_DESTINATION_STATION = "key_destination_station"
        private const val KEY_DEPARTURE = "key_departure"
        private const val KEY_ADULTS = "key_adults"
        private const val KEY_TEENS = "key_teens"
        private const val KEY_CHILDREN = "key_children"

        fun start(
            context: Context,
            originStation: String,
            destinationStation: String,
            departureTime: String,
            adults: Int,
            teens: Int,
            children: Int
        ) {
            val intent = Intent(context, ResultListActivity::class.java)
            intent.putExtra(KEY_ORIGIN_STATION, originStation)
            intent.putExtra(KEY_DESTINATION_STATION, destinationStation)
            intent.putExtra(KEY_DEPARTURE, departureTime)
            intent.putExtra(KEY_ADULTS, adults)
            intent.putExtra(KEY_TEENS, teens)
            intent.putExtra(KEY_CHILDREN, children)
            context.startActivity(intent)
        }
    }
}