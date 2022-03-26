package com.example.mytest

import com.example.mytest.Interface.PaginationAdapterCallback
import com.example.mytest.Adapter.PaginationAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mytest.Interface.Api_Interface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.mytest.Utils.RetroClient
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.mytest.Utils.PaginationScrollListener
import com.example.mytest.Model.Videolist
import com.example.mytest.Model.Datalist
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VideoFragmet : Fragment(), PaginationAdapterCallback {
  lateinit  var binding: Videobinding
    var adapter: PaginationAdapter? = null
    var linearLayoutManager: LinearLayoutManager? = null
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = PAGE_START
    var apiService: Api_Interface? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_video, container, false
        )
        val view = binding.getRoot()
        binding.setVideocontent(this)
        apiService = RetroClient.client?.create(Api_Interface::class.java)
        adapter = context?.let { PaginationAdapter(it) }
        linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.mainrecycler.layoutManager = linearLayoutManager
        binding.mainrecycler.itemAnimator = DefaultItemAnimator()
        binding.mainrecycler.adapter = adapter
        binding.mainrecycler.addOnScrollListener(object :
            PaginationScrollListener(linearLayoutManager!!) {
            override fun loadMoreItems() {
                isLoading = true
                currentPage += 1
                loadNextPage()
            }

            override fun getTotalPageCount(): Int {
                return TOTAL_PAGES
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }
        })
        loadFirstPage()
        return view
    }

    private fun loadFirstPage() {
        Log.d("TAG", "loadFirstPage: ")
        currentPage = PAGE_START
        callVideosApi()!!.enqueue(object : Callback<Videolist?> {
            override fun onResponse(call: Call<Videolist?>, response: Response<Videolist?>) {
                val results = fetchResults(response)
                binding!!.mainProgress.visibility = View.GONE
                adapter!!.addAll(results)
                Log.v("result>>", results.size.toString())
                if (currentPage <= TOTAL_PAGES) adapter!!.addLoadingFooter() else isLastPage = true
            }

            override fun onFailure(call: Call<Videolist?>, t: Throwable) {
                t.printStackTrace()
                Log.v("dfd", t.message!!)
            }
        })
    }

    private fun loadNextPage() {
        Log.v("ds?", "" + currentPage)
        callVideosApi()!!.enqueue(object : Callback<Videolist?> {
            override fun onResponse(call: Call<Videolist?>, response: Response<Videolist?>) {
                adapter!!.removeLoadingFooter()
                isLoading = false
                val results = fetchResults(response)
                binding!!.mainProgress.visibility = View.GONE
                assert(results != null)
                adapter!!.addAll(results)
                Log.v("result>>", results.size.toString())
                if (currentPage <= TOTAL_PAGES) adapter!!.addLoadingFooter() else isLastPage = true
            }

            override fun onFailure(call: Call<Videolist?>, t: Throwable) {
                t.printStackTrace()
                Log.v("dfd", t.message!!)
            }
        })
    }

    private fun fetchResults(response: Response<Videolist?>): List<Datalist> {
        val videolist = response.body()
        return videolist!!.results
    }

    private fun callVideosApi(): Call<Videolist?>? {
        return apiService!!.getList(
            currentPage
        )
    }

    override fun retryPageLoad() {
        loadNextPage()
    }

    companion object {
        private const val PAGE_START = 1
        private const val TOTAL_PAGES = 6
    }
}