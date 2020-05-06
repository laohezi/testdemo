package com.yunzhi.bluetooth

import android.bluetooth.BluetoothAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var bluetoothAdapter: BluetoothAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initBlueTooth()
        initListner();
    }

    private fun initListner() {
        bt_switch.setOnClickListener {

            bluetoothAdapter?.let {
                if (it!!.isEnabled) {
                    it.disable()
                } else {
                    it.enable()
                }

            }


        }

        bt_bound.setOnClickListener {
            bluetoothAdapter?.let {
                val pairedDevices = it.bondedDevices
                val adapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line)
                sp_bound.adapter = adapter
                pairedDevices.forEach {
                    adapter.add(it.name)
                }
            }


        }

    }


    fun initBlueTooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()


    }


}
