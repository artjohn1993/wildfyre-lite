package com.generator.wildfyrelite.activity

import android.annotation.SuppressLint
import android.app.ActionBar
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.generator.wildfyrelite.R
import com.generator.wildfyrelite.defaultSettings
import com.generator.wildfyrelite.dialog.CloseDialog
import com.generator.wildfyrelite.dialog.UrlCheckerDialog
import com.generator.wildfyrelite.enum.WebOpenerDB
import com.generator.wildfyrelite.events.IdleCheckerEvent
import com.generator.wildfyrelite.local_db.DatabaseHandler
import com.generator.wildfyrelite.model.URLData
import com.generator.wildfyrelite.services.IdleChecker
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_action_bar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.startActivity
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    var db = DatabaseHandler(this)
    var urlData: MutableList<URLData.Details> = ArrayList()
    var total = 0
    var closeDialog = CloseDialog()

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        actionBar?.hide()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        versionTxt.text = "Version 20220709.1"
        //wildfyre_lite_v.20220709.1

        bind()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this);
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        start.imageResource = R.drawable.play2
        startService(Intent(this,IdleChecker::class.java))
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this);
        stopService(Intent(this,IdleChecker::class.java))
    }



    private fun bind() {
        checkDataBase()
        setClickEvent()
    }

    private fun setClickEvent() {
        addURL.setOnClickListener {
            addURLEditText(null)
            if (total == 25) {
                addURL.isEnabled = false
            }
        }

        start.setOnClickListener {
            start.imageResource = R.drawable.pause2
            saveUrl()
            saveFactor()
            startActivity<WordpressLoaderActivity>()
        }

        shuffleBtn.setOnClickListener {
            shuffleUrl()
        }

        arrangeBtn.setOnClickListener {
            var tempData = urlData.filter { item ->
                item.url != ""
            }.sortedBy { item -> item.url }
            removeUrlView()
            tempData.forEach { item ->
                addURLEditText(item.url)
            }
        }

        resetBtn.setOnClickListener {
            removeUrlView()
            defaultSettings?.url?.forEach { item ->
                addURLEditText(item)
            }
        }
    }

    private fun saveFactor() {
        db.deleteDatabase(WebOpenerDB.TABLE_FACTOR.getValue())
        db.insertFactor(factor.text.toString())
    }

    private fun saveUrl() {
        db.deleteDatabase(WebOpenerDB.TABLE_URL.getValue())
        urlData.forEach { item ->
            db.insertURL(item.url!!)
        }
    }

    private fun shuffleUrl() {
        var shuffledUrl = urlData.shuffled()
            .filter { data ->
                data.url != ""
            }
        removeUrlView()

        shuffledUrl
        shuffledUrl.forEach { item ->
            addURLEditText(item.url)
        }
    }

    private fun removeUrlView() {
        urlData.clear()
        urlCon.removeAllViews()
    }

    private fun checkDataBase() {
        checkURL()
        checkFactor()
    }

    private fun checkFactor() {
        var data = db.getFactor()
        if (data == 0) {
            factor.setText(defaultSettings?.factor)
        } else {
            factor.setText(data.toString())
        }
    }

    private fun checkURL() {
        var data = db.getURL()
        if (data.isEmpty()) {
            defaultSettings!!.url.forEach { item ->
                addURLEditText(item)
            }
        } else {
            data.forEach { item ->
                addURLEditText(item)
            }
        }
    }

    private fun addURLEditText(url: String?) {
        addURL.text = "add URL (${++total})"
        var editText = EditText(this)
        var layout = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )

        editText.layoutParams = layout
        editText.id = View.generateViewId()
        editText.hint = "Enter URL"
        editText.setText(url)
        var data = URLData.Details(url, editText.id.toString())
        urlData.add(data)
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                checkArray(editText.id.toString(), s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })

        urlCon.addView(editText)
    }

    private fun checkArray(id: String, url: String) {
        var index = -1
        urlData.forEach { data ->
            index++
            if (id == data.id) {
                var newData = URLData.Details(url, id)
                urlData[index] = newData
            }
        }
    }

    override fun onBackPressed() {
        closeDialog.showDialog(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onIdleCheckerEvent(event: IdleCheckerEvent) {
        println("IdleCheckerEvent")
        shuffleUrl()

        start.imageResource = R.drawable.pause2
        saveUrl()
        saveFactor()
        startActivity<WordpressLoaderActivity>()
    }

}