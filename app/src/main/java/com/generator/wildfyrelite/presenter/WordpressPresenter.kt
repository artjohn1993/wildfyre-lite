package com.generator.wildfyrelite.presenter

import com.generator.wildfyrelite.api.ApiServices
import com.generator.wildfyrelite.model.Wordpress
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class WordpressPresenterClass(var view: WordpressView, var api: ApiServices) : WordpressPresenter {
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun getLatestPost(url: String) {
        println(url)
        compositeDisposable.add(
            api.getlatestPost(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe({ result ->
                    view.responseGetLatestPost(result)
                }, { error ->
                    println(error.cause?.message.toString())
                    view.responseGetLatestPostFailed(error.cause?.message.toString())
                })
        )
    }
}

interface WordpressPresenter {
    fun getLatestPost(url: String)
}

interface WordpressView {
    fun responseGetLatestPost(data: List<Wordpress.Result>)
    fun responseGetLatestPostFailed(data: String)
}