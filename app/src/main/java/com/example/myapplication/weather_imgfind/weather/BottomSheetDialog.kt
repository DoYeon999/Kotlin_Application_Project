package com.example.myapplication.weather_imgfind.weather

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.weather_imgfind.adapter.APIAdapter
import com.example.myapplication.weather_imgfind.model.TideInfo
import com.example.myapplication.weather_imgfind.model.TideModel
import com.example.myapplication.weather_imgfind.model.temper
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.w3c.dom.Text

class BottomSheetDialog(val datas : MutableList<TideModel>, val levels : MutableList<Entry>, val tempers : MutableList<temper>, val where : String, val lunars : List<String>) : BottomSheetDialogFragment() {

    lateinit var txtview: TextView
    lateinit var txtview2: TextView
    lateinit var rcview: RecyclerView
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(
            R.layout.activity_bottom_sheet_dialog,
            container,
            false
        )
        txtview = view.findViewById(R.id.bottom_text)
        txtview.text = where
        txtview2 = view.findViewById(R.id.bottom_text2)
        txtview2.text = "오늘의 물때 : ${lunars?.get(0)}"
        txtview.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        txtview2.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        rcview = view.findViewById(R.id.myrecyclerView2)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        rcview.layoutManager = linearLayoutManager
        rcview.adapter = APIAdapter(requireContext(), datas, tempers, lunars)

        var hightide : MutableList<TideInfo> = ArrayList()
        var lowtide  : MutableList<TideInfo> = ArrayList()
        datas.get(0).result.data.forEach {
            if(it.tidetype == "고조") hightide.add(it)
                //val nowtime = it.tidetime.split(" ")[1].split(":")
                //hightide.add((nowtime[0].toInt() * 60 + nowtime[1].toInt()).toFloat())
            else lowtide.add(it)
                //val nowtime = it.tidetime.split(" ")[1].split(":")
                //lowtide.add((nowtime[0].toInt() * 60 + nowtime[1].toInt()).toFloat())
        }
        val dataset = LineDataSet(levels, "")
        dataset.mode = LineDataSet.Mode.LINEAR
        dataset.color = ColorTemplate.VORDIPLOM_COLORS[0]
        val lineData = LineData(dataset)
        val lineChart: LineChart = view.findViewById(R.id.lineChart)
        lineChart.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val hour = value.toInt() / 60
                val minute = value.toInt() % 60
                return String.format("%02d:%02d", hour, minute)
            }
        }

        for(tide in hightide) {
            Log.d("maptest", "$tide **")
            val nowtime = tide.tidetime.split(" ")[1].split(":")
            val nowx = (nowtime[0].toInt() * 60 + nowtime[1].toInt()).toFloat()
            val target = levels.find { it.x == nowx }
            val myMarker = CustomMarkerView(requireContext(), R.layout.linechart_data_textview)
            Log.d("maptest", "${target!!.x}, ${target!!.y}")
            myMarker.refreshContent(target!!, Highlight(target.x, target.y, target.x.toInt()))
            lineChart.marker = myMarker
        }

        for(tide in lowtide) {
            Log.d("maptest", "** $tide **")
            val nowtime = tide.tidetime.split(" ")[1].split(":")
            val nowx = (nowtime[0].toInt() * 60 + nowtime[1].toInt()).toFloat()
            val target = levels.find { it.x == nowx }
            val myMarker = CustomMarkerView(requireContext(), R.layout.linechart_data_textview)
            Log.d("maptest", "${target!!.x}, ${target!!.y}")
            myMarker.refreshContent(target!!, Highlight(target.x, target.y, target.x.toInt()))
            lineChart.marker = myMarker
        }

        val description = Description()
        description.text = "물높이 (cm)"
        description.textColor = ContextCompat.getColor(requireContext(), R.color.white)
        dataset.color = ContextCompat.getColor(requireContext(), R.color.white)
        lineChart.description = description
        lineChart.xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.white)
        lineChart.axisRight.textColor = ContextCompat.getColor(requireContext(), R.color.white)
        lineChart.axisLeft.textColor = ContextCompat.getColor(requireContext(), R.color.white)
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.data = lineData
        lineChart.setPinchZoom(false)
        lineChart.setTouchEnabled(false)
        lineChart.setScaleEnabled(false)
        lineChart.isDragEnabled = false
        lineChart.legend.isEnabled = false
        //lineChart.description.isEnabled = false
        lineChart.invalidate()

        return view
    }
}

class CustomMarkerView(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {
    override fun refreshContent(e: Entry, highlight: Highlight) {
        val tidetime: TextView = findViewById(R.id.tide_box)
        tidetime.text = e.x.toString() + "\n" + e.y.toString() + "cm"
        Log.d("maptest", "${e.x.toString()}, ${e.y.toString()}")
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-width / 2).toFloat(), -height.toFloat())
    }

}
