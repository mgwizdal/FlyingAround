package com.example.flyingaround.search.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.flyingaround.R
import com.example.flyingaround.search.viewmodel.SearchActivityViewModel
import com.example.flyingaround.utils.hide
import com.example.flyingaround.utils.include
import com.example.flyingaround.utils.show
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_search.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.time.LocalDateTime

class SearchActivity : AppCompatActivity() {
    private val destroyDisposables = CompositeDisposable()

    private val viewModel: SearchActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)

        destroyDisposables include viewModel.initializationObservable
            .subscribe { state ->
                when (state) {
                    SearchActivityViewModel.InitializationUiState.Loading -> showLoading()
                    is SearchActivityViewModel.InitializationUiState.Success -> handleSuccess(state.list)
                    is SearchActivityViewModel.InitializationUiState.Error -> handleError()
                }
            }

        viewModel.initialize()
    }

    private fun showLoading() {
        progressBar.show()
        formContainer.hide()
        searchButton.hide()
    }

    private fun handleSuccess(list: List<StationAutoComplete>) {
        setupView(list)
        showContent()
    }

    private fun showContent() {
        progressBar.hide()
        formContainer.show()
        searchButton.show()
    }

    private fun setupView(list: List<StationAutoComplete>) {
        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_dropdown_item_1line,
            list.map { it.toString() })
        originAutoCompleteTextView.setAdapter(adapter)
        destinationAutoCompleteTextView.setAdapter(adapter)
        departureEditText.setOnClickListener {
            val now = LocalDateTime.now()
            DatePickerDialog(this, { a, year, month, day ->
                departureEditText.setText("$day/$month/$year")
            }, now.year, now.monthValue, now.dayOfMonth).show()
        }
    }

    private fun handleError() {
        showError()
        val snackbar = Snackbar.make(
            rootView,
            getString(R.string.init_failed),
            Snackbar.LENGTH_INDEFINITE
        )
        snackbar.setAction(getString(R.string.retry)) {
            snackbar.dismiss()
            showLoading()
            viewModel.initialize()
        }
        snackbar.show()
    }

    private fun showError() {
        progressBar.hide()
        formContainer.hide()
        searchButton.hide()
    }
}