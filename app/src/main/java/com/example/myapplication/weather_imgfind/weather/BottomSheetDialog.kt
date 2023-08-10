package com.example.myapplication.weather_imgfind.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.weather_imgfind.adapter.APIAdapter
import com.example.myapplication.weather_imgfind.model.TideModel
import com.example.myapplication.weather_imgfind.model.temper
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDialog(val datas : MutableList<TideModel>, val levels : MutableList<Entry>, val tempers : MutableList<temper>, val where : String, val lunars : List<String>) : BottomSheetDialogFragment() {

    lateinit var btn: Button
    lateinit var txtview: TextView
    lateinit var txtview2: TextView
    lateinit var rcview: RecyclerView
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
