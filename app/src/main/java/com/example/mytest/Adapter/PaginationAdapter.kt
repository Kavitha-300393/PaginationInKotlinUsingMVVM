package com.example.mytest.Adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.example.mytest.Model.Datalist
import com.example.mytest.Interface.PaginationAdapterCallback
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.example.mytest.Adapter.PaginationAdapter
import com.example.mytest.R
import com.example.mytest.Adapter.PaginationAdapter.MovieVH
import com.example.mytest.Adapter.PaginationAdapter.LoadingVH
import com.example.mytest.Adapter.PaginationAdapter.HeroVH
import com.bumptech.glide.Glide
import com.example.mytest.Model.Videolist
import java.util.ArrayList

/**
 * Created by Kavitha on 25/03/2022.
 */
class PaginationAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var videolists: MutableList<Datalist>?
    private var isLoadingAdded = false
    private var retryPageLoad = false
    private val mCallback: PaginationAdapterCallback? = null
    private var errorMsg: String? = null
    val movies: List<Datalist>?
        get() = videolists

    fun setMovies(videolists: MutableList<Datalist>?) {
        this.videolists = videolists
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            ITEM -> {
                val viewItem = inflater.inflate(R.layout.row_videos, parent, false)
                viewHolder = MovieVH(viewItem)
            }
            LOADING -> {
                val viewLoading = inflater.inflate(R.layout.item_progress, parent, false)
                viewHolder = LoadingVH(viewLoading)
            }
            HERO -> {
                val viewHero = inflater.inflate(R.layout.row_videos, parent, false)
                viewHolder = HeroVH(viewHero)
            }
        }
        return viewHolder!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val result = videolists!![position]
        when (getItemViewType(position)) {
            HERO -> {
                val heroVh = holder as HeroVH
                heroVh.name.text = result.firstname + " " + result.lastname
                heroVh.email.text = result.email
                Glide.with(context)
                    .load(result.avatar)
                    .into(heroVh.imageView)
            }
            ITEM -> {
                val movieVH = holder as MovieVH
                movieVH.name.text = result.firstname + " " + result.lastname
                movieVH.email.text = result.email
                Glide.with(context)
                    .load(result.avatar)
                    .into(movieVH.imageView)
            }
            LOADING -> {
                val loadingVH = holder as LoadingVH
                if (retryPageLoad) {
                    loadingVH.mErrorLayout.visibility = View.VISIBLE
                    loadingVH.mProgressBar.visibility = View.GONE
                } else {
                    loadingVH.mErrorLayout.visibility = View.GONE
                    loadingVH.mProgressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    protected inner class LoadingVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mProgressBar: ProgressBar
        private val mRetryBtn: ImageButton
        private val mErrorTxt: TextView
        val mErrorLayout: LinearLayout

        init {
            mProgressBar = itemView.findViewById(R.id.loadmore_progress)
            mRetryBtn = itemView.findViewById(R.id.loadmore_retry)
            mErrorTxt = itemView.findViewById(R.id.loadmore_errortxt)
            mErrorLayout = itemView.findViewById(R.id.loadmore_errorlayout)
        }
    }

    override fun getItemCount(): Int {
        return if (videolists == null) 0 else videolists!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            HERO
        } else {
            if (position == videolists!!.size - 1 && isLoadingAdded) LOADING else ITEM
        }
    }

    fun add(r: Datalist) {
        videolists!!.add(r)
        notifyItemInserted(videolists!!.size - 1)
    }

    fun addAll(moveResults: List<Datalist>) {
        for (result in moveResults) {
            add(result)
        }
    }

//    fun remove(r: Videolist?) {
//        var position = videolists!!.indexOf(r)
//        if (position > -1) {
//            videolists!!.removeAt(position)
//            notifyItemRemoved(position)
//        }
//    }

    val isEmpty: Boolean
        get() = itemCount == 0

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(Datalist())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        val position = videolists!!.size - 1
        val result = getItem(position)
        if (result != null) {
            videolists!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun getItem(position: Int): Datalist {
        return videolists!![position]
    }

    fun showRetry(show: Boolean, errorMsg: String?) {
        retryPageLoad = show
        notifyItemChanged(videolists!!.size - 1)
        if (errorMsg != null) this.errorMsg = errorMsg
    }

    protected inner class HeroVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView
        val email: TextView
        var imageView: ImageView

        init {
            name = itemView.findViewById(R.id.name)
            email = itemView.findViewById(R.id.email)
            imageView = itemView.findViewById(R.id.avatar)
        }
    }

    protected inner class MovieVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView
        val email: TextView
        var imageView: ImageView

        init {
            name = itemView.findViewById(R.id.name)
            email = itemView.findViewById(R.id.email)
            imageView = itemView.findViewById(R.id.avatar)
        }
    }

    companion object {
        // View Types
        private const val ITEM = 0
        private const val LOADING = 1
        private const val HERO = 2
        private const val BASE_URL_IMG = "https://image.tmdb.org/t/p/w200"
        const val totalItemsCount = 14
    }

    init {
        // this.mCallback = (PaginationAdapterCallback) context;
        videolists = ArrayList()
    }
}