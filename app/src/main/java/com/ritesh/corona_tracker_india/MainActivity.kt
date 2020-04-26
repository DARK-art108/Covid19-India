package com.ritesh.corona_tracker_india

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var stateAdapter: StateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        list.addHeaderView(LayoutInflater.from(this).inflate(R.layout.item_header,list,false))



        fetchResults()

    }

    private fun fetchResults() {

        GlobalScope.launch {
            val response = withContext(Dispatchers.IO) { Client.api.execute() }

            if(response.isSuccessful){

                val data = Gson().fromJson(response.body?.string(),Response::class.java)

                launch(Dispatchers.Main){
                    bindCombinedData(data.statewise[0])
                    bindstatewisedata(data.statewise.subList(0,data.statewise.size))
                }




            }
        }
    }

    private fun bindstatewisedata(subList: List<StatewiseItem>) {

        stateAdapter = StateAdapter(subList)
        list.adapter = stateAdapter


    }

    private fun bindCombinedData(data: StatewiseItem) {

        val lastUpdatedTime = data.lastupdatedtime
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        lastupdatedTv.text = "Last Updated\n ${getTimeAgo(simpleDateFormat.parse(lastUpdatedTime))}"

        confirmedtv.text = data.confirmed
        deceasedtv.text = data.deaths
        recoveredtv.text = data.recovered
        activetv.text = data.active

    }
    fun getTimeAgo(past: Date): String {
        val now = Date()
        val seconds = TimeUnit.MILLISECONDS.toSeconds(now.time - past.time)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
        val hours = TimeUnit.MILLISECONDS.toHours(now.time - past.time)

        return when {
            seconds < 60 -> {
                "Few seconds ago"
            }
            minutes < 60 -> {
                "$minutes minutes ago"
            }
            hours < 24 -> {
                "$hours hour ${minutes % 60} min ago"
            }
            else -> {
                SimpleDateFormat("dd/MM/yy, hh:mm a").format(past).toString()
            }
        }
    }
}
