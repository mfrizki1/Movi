package id.calocallo.movi.utils

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.properties.Delegates

class GenericAdapter<T : Equatable>(
    @LayoutRes private val layoutRes: Int,
    @LayoutRes private val loadingRes: Int,
    @LayoutRes private val errorRes: Int,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), AutoUpdatable {
    private var items: List<Equatable> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n, _ ->
            o == n
        }
    }

    var onBind: View.(position: Int, item: T) -> Unit = { _, _ -> }
    var onBindError: View.(String) -> Unit = { }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        val attachedLayoutMnaager = recyclerView.layoutManager
        if (attachedLayoutMnaager is GridLayoutManager) {
            val spanCount = attachedLayoutMnaager.spanCount
            attachedLayoutMnaager.spanSizeLookup = setGridSpan(spanCount)
        }
    }

    private fun setGridSpan(column: Int): GridLayoutManager.SpanSizeLookup {
        return object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (getItemViewType(position)) {
                    TYPE_DATA -> 1
                    else -> column
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewLoading = parent.inflateRoot(loadingRes)
        val viewError = parent.inflateRoot(errorRes)
        val viewData = parent.inflateRoot(layoutRes)

        return when (viewType) {
            TYPE_LOADING -> LoadingViewHolder(viewLoading)
            TYPE_ERROR -> ErrorViewHolder(viewError)
            else -> DataViewHolder<T>(viewData)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (getItemViewType(position)) {
            TYPE_DATA -> {
                (holder as DataViewHolder<T>).bind(position, item as T, onBind)
            }
            TYPE_ERROR -> {
                val itemError = item as ErrorData
                (holder as ErrorViewHolder).bind(itemError.errorMessage, onBindError)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is LoadingData -> TYPE_LOADING
            is ErrorData -> TYPE_ERROR
            else -> TYPE_DATA
        }
    }

    fun pushItems(item: List<T>) {
        val newItem = calculateMutableItems()
        newItem.addAll(item)
        items = newItem
    }

    fun pushItemsWithClear(item: List<T>) {
        val newItem = calculateMutableItemsWithClear()
        newItem.addAll(item)
        items = newItem
    }

    fun pushItem(item: T) {
        items = items + item
    }

    fun updateItemByPosition(position: Int, item: (T) -> T) {
        val currentItem = items[position]
        val newItem = item.invoke(currentItem as T)
        val newItems = items.toMutableList()
        newItems.remove(currentItem)
        newItems.add(newItem)
        items = newItems
    }

    fun updateItemForPosition(position: Int, item: (T) -> T) {
        val currentItem = items[position]
        val newItem = item.invoke(currentItem as T)
        val newItems = items.toMutableList()
        newItems.remove(currentItem)
        newItems.add(newItem)
        notifyItemChanged(position)
        items = newItems
    }

    fun pushLoading() {
        val newItem = calculateMutableItems()
        newItem.add(LoadingData)
        items = newItem
    }
    fun pushLoadingWithClear() {
        val newItem = calculateMutableItemsWithClear()
        newItem.add(LoadingData)
        items = newItem
    }

    fun pushError(localizedMessage: String?) {
        val newItem = calculateMutableItems()
        val errorItem = ErrorData(localizedMessage ?: DEFAULT_ERROR_MESSAGE)
        newItem.add(errorItem)
        items = newItem
    }

    private fun calculateMutableItems(preventClear: Boolean = false): MutableList<Equatable> {
        val newItems = items.toMutableList()
        if (!preventClear) {
            newItems.remove(LoadingData)
            newItems.remove(newItems.find { it is ErrorData })
        }
        return newItems
    }

    private fun calculateMutableItemsWithClear(): MutableList<Equatable> {
        val newItems = items.toMutableList()
        newItems.remove(LoadingData)
        newItems.remove(newItems.find { it is ErrorData })
        newItems.removeAll(items)
        return newItems
    }

    class DataViewHolder<T : Equatable>(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(position: Int, item: T, onBind: View.(position: Int, item: T) -> Unit) {
            onBind.invoke(itemView, position, item)
        }
    }

    class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class ErrorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(message: String, onBind: View.(String) -> Unit) {
            onBind.invoke(itemView, message)
        }
    }

    object LoadingData : BaseEquatable("loading")
    data class ErrorData(val errorMessage: String) : BaseEquatable("error")

    companion object {
        private const val TYPE_DATA = 1
        private const val TYPE_LOADING = 2
        private const val TYPE_ERROR = 3

        private const val DEFAULT_ERROR_MESSAGE = "Something wrong!"
    }
}