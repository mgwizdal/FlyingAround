package com.example.flyingaround.utils

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


infix fun CompositeDisposable.include(observable: Disposable) {
    this.add(observable)
}