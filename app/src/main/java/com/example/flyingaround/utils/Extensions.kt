package com.example.flyingaround.utils

import android.content.Context
import android.view.View
import com.example.flyingaround.R
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_search.*


infix fun CompositeDisposable.include(observable: Disposable) {
    this.add(observable)
}