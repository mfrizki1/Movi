package id.calocallo.movi.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.ActionBar
import androidx.core.os.bundleOf
import id.calocallo.movi.R

fun Context.intentTo(
    className: String,
    bundle: Bundle = bundleOf(),
    time: Long = 0,
) {
    Handler(Looper.getMainLooper()).postDelayed({
        Intent(this, Class.forName(className)).apply {
            putExtras(bundle)
            startActivity(this)
        }
    }, time)
}

fun setupSupportActionBar(supportActionBar: ActionBar?, title: String) {
    supportActionBar?.apply {
        this.title = title
        setDisplayHomeAsUpEnabled(true)
        setHomeAsUpIndicator(R.drawable.ic_movi_arrow_back)
    }
}
