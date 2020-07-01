package com.example.flyingaround.details.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.flyingaround.R
import com.example.flyingaround.details.viewmodel.DetailsDialogFragmentViewModel
import com.example.flyingaround.utils.include
import io.reactivex.disposables.CompositeDisposable
import org.koin.android.viewmodel.ext.android.viewModel

class DetailsDialogFragment : DialogFragment() {
    private val destroyViewDisposable = CompositeDisposable()
    private lateinit var flightNumber: String
    private var dialogView: View? = null

    private val viewModel: DetailsDialogFragmentViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        flightNumber = requireNotNull(arguments?.getString(KEY_FLIGHT_NUMBER))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        val view = activity?.layoutInflater?.inflate(R.layout.details_dialog, null)

        dialogView = view

        return AlertDialog.Builder(activity)
            .setTitle(getString(R.string.flight_details))
            .setView(view)
            .setPositiveButton(android.R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = dialogView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        destroyViewDisposable include viewModel.getFlightInfo(flightNumber)
            .subscribe({
                view.findViewById<TextView>(R.id.origin).text = it.origin
                view.findViewById<TextView>(R.id.discountInPercent).text =
                    it.discountInPercent.toString()
                view.findViewById<TextView>(R.id.destination).text = it.destination
                view.findViewById<TextView>(R.id.fareClass).text = it.fareClass
                view.findViewById<TextView>(R.id.infantsLeft).text = it.infantsLeft.toString()
            }, {
                Log.e("DetailsDialogFragment", "error: ${it.localizedMessage}")
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dialogView = null
        destroyViewDisposable.dispose()
    }

    companion object {
        const val TAG = "TAG_DETAILS"
        const val KEY_FLIGHT_NUMBER = "KEY_FLIGHT_NUMBER"

        fun create(flightNumber: String): DetailsDialogFragment {
            val bundle = Bundle()
            bundle.putString(KEY_FLIGHT_NUMBER, flightNumber)
            val fragment =
                DetailsDialogFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}