package com.example.suitmedia.thirdscreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.suitmedia.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ThirdActivity : AppCompatActivity() {
    private lateinit var userRV: RecyclerView
    private lateinit var userAdapter: UserAdaptor
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyStateView: TextView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var loadMoreProgressBar: ProgressBar

    private var pageNum = 1
    private var totalUsers = -1
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_third)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        userRV = findViewById(R.id.user_rv)
        swipeRefreshLayout = findViewById(R.id.main)
        progressBar = findViewById(R.id.progressBar_ActivityMain)
        emptyStateView = findViewById(R.id.emptyStateView)
        loadMoreProgressBar = findViewById(R.id.loadMoreProgressBar)

        layoutManager = LinearLayoutManager(this)
        userRV.layoutManager = layoutManager

        hideAll()
        getUsers()

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            pageNum = 1
            getUsers()
        }

        userRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading && totalUsers > layoutManager.itemCount && !recyclerView.canScrollVertically(1)) {
                    loadMoreUsers()
                }
            }
        })

        val btnBack: ImageButton = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun showAll() {
        progressBar.visibility = View.GONE
        userRV.visibility = View.VISIBLE
        emptyStateView.visibility = View.GONE
    }

    private fun hideAll() {
        userRV.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        emptyStateView.visibility = View.GONE
    }

    private fun showEmptyState() {
        userRV.visibility = View.GONE
        progressBar.visibility = View.GONE
        emptyStateView.visibility = View.VISIBLE
    }

    private fun showLoadMoreProgress() {
        loadMoreProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideLoadMoreProgress() {
        loadMoreProgressBar.visibility = View.GONE
        isLoading = false
    }

    private fun loadMoreUsers() {
        showLoadMoreProgress()
        pageNum++
        getUsers(isLoadingMore = true)
    }

    private fun getUsers(isLoadingMore: Boolean = false) {
        if (!isLoadingMore) {
            hideAll()
        }
        val usersCall = UsersService.userInstance.getUsers(pageNum)
        usersCall.enqueue(object : Callback<UsersResponse> {
            override fun onResponse(call: Call<UsersResponse>, response: Response<UsersResponse>) {
                val usersResponse = response.body()
                if (usersResponse?.data != null) {
                    totalUsers = usersResponse.total ?: 0
                    if (!::userAdapter.isInitialized) {
                        userAdapter = UserAdaptor(this@ThirdActivity) { selectedUser ->
                            val intent = Intent()
                            intent.putExtra("selected_user", "${selectedUser.firstName} ${selectedUser.lastName}")
                            setResult(RESULT_OK, intent)
                            finish()
                        }
                        userRV.adapter = userAdapter
                    }
                    if (!isLoadingMore) {
                        userAdapter.clear()
                    }
                    userAdapter.addAll(usersResponse.data)
                    if (!isLoadingMore) {
                        showAll()
                    } else {
                        hideLoadMoreProgress()
                    }
                } else {
                    if (!isLoadingMore) {
                        showEmptyState()
                    } else {
                        hideLoadMoreProgress()
                    }
                }
            }

            override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
                Log.e(TAG, "Failed Fetching Users", t)
                if (!isLoadingMore) {
                    showEmptyState()
                } else {
                    hideLoadMoreProgress()
                }
            }
        })
    }

    companion object {
        const val TAG = "ThirdActivity"
    }
}