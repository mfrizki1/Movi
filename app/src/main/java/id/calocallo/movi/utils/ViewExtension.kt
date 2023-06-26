package id.calocallo.movi.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.calocallo.movi.BuildConfig
import id.calocallo.movi.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T,
) =
    lazy(LazyThreadSafetyMode.NONE) {
        bindingInflater.invoke(layoutInflater)
    }

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun Context.toast(message: CharSequence) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.toast(stringResource: Int) =
    Toast.makeText(this, stringResource, Toast.LENGTH_SHORT).show()

fun Context.longToast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()

fun Context.longToast(stringResource: Int) =
    Toast.makeText(this, stringResource, Toast.LENGTH_LONG).show()

@SuppressLint("CheckResult")
fun ImageView.loadImage(url: String, type: String = PlaceHolderImage.Default.toString()) {
    val glide = Glide.with(context)
        .load(url)

    val options = RequestOptions()
    when (type) {
        PlaceHolderImage.Default.toString() -> {
            options.placeholder(R.drawable.ic_movi_image_placeholder)
                .error(R.drawable.ic_movi_image_placeholder)
        }
        PlaceHolderImage.Profile.toString() -> {
            options.placeholder(R.drawable.ic_movi_user_placeholder)
                .error(R.drawable.ic_movi_user_placeholder)
        }
        else -> {
            options.placeholder(R.drawable.ic_movi_image_placeholder)
        }
    }
    glide.apply(options).into(this)
}

fun RecyclerView.onReachBottomScroll(
    action: () -> Unit,
) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            val isSettling = newState == RecyclerView.SCROLL_STATE_SETTLING
            val isBottomReach = !canScrollVertically(1)

            if (isSettling && isBottomReach) {
                action.invoke()
            }
        }
    })
}

fun convertStringToDate(
    dateTime: String?,
    inputDateFormat: String,
    outputDateFormat: String,
): String? {
    val input = SimpleDateFormat(inputDateFormat, Locale.getDefault())
    val output = SimpleDateFormat(outputDateFormat, Locale("id", "ID"))
    try {
        val getAbbreviate: Date? = input.parse(dateTime.orEmpty())
        return output.format(getAbbreviate!!)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return null
}

fun ViewGroup.inflateRoot(@LayoutRes layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun <T : Equatable> genericAdapterLazy(
    layoutRes: Int,
    loadingRes: Int = R.layout.layout_progress_bar,
    errorRes: Int = R.layout.layout_error_page,
    onBind: View.(position: Int, item: T) -> Unit,
    onBindError: View.(String) -> Unit = {},
): Lazy<GenericAdapter<T>> {
    return lazy {
        val adapter = GenericAdapter<T>(
            layoutRes = layoutRes,
            loadingRes = loadingRes,
            errorRes = errorRes,
        )
        adapter.onBind = onBind
        adapter.onBindError = onBindError

        return@lazy adapter
    }
}

fun RecyclerView.setDivider(@DrawableRes drawableRes: Int) {
    val divider = DividerItemDecoration(
        this.context,
        DividerItemDecoration.VERTICAL,
    )
    val drawable = ContextCompat.getDrawable(
        this.context,
        drawableRes,
    )
    drawable?.let {
        divider.setDrawable(it)
        addItemDecoration(divider)
    }
}

fun String.trimImageLink(): String {
    val trimLinkText: String = if (this.startsWith("/") && this.contains(
            "secure.gravatar.com/avatar/",
            ignoreCase = true,
        )
    ) {
        this.substring(1)
    } else {
        BuildConfig.SMALL_IMAGE_URL + this
    }

    return trimLinkText
}