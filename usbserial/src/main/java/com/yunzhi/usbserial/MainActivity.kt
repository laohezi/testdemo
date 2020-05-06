package com.yunzhi.usbserial

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.drm.DrmStore
import android.hardware.usb.*
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.hoho.android.usbserial.driver.UsbSerialPort
import com.hoho.android.usbserial.driver.UsbSerialProber
import kotlinx.android.synthetic.main.activity_main.*
import java.nio.ByteBuffer
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    lateinit var usbDevice: UsbDevice
    lateinit var usbEndpointIn: UsbEndpoint
    lateinit var usbEndpointOut: UsbEndpoint
    var usbConnection: UsbDeviceConnection? = null
    lateinit var port: UsbSerialPort


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val usbManager = getSystemService(Context.USB_SERVICE) as UsbManager
        /* val list = usbManager.deviceList
         usbDevice = list.values.first { it.vendorId == 6790 && it.productId == 29987 }
         if (usbManager.hasPermission(usbDevice)) {
             usbConnection = usbManager.openDevice(usbDevice)
             val interf = usbDevice.getInterface(0)
             for (index in 0 until interf.endpointCount) {
                 val point = interf.getEndpoint(index)
                 if (point.type == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                     if (point.direction == UsbConstants.USB_DIR_IN) {
                         usbEndpointIn = point
                     } else if (point.direction == UsbConstants.USB_DIR_OUT) {
                         usbEndpointOut = point

                     }
                 }
             }*/

        val drivers = UsbSerialProber.getDefaultProber().findAllDrivers(usbManager)

        val tempDriver =
            drivers.first { it.device.vendorId == 6790 && it.device.productId == 29987 }
        if (usbManager.hasPermission(tempDriver.device)) {
            usbConnection = usbManager.openDevice(tempDriver.device)
            port = tempDriver.ports[0]

            port.open(usbConnection)

            port.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE)



           startReadThread()
            startWriteThread()
        }





        bt_send.setOnClickListener {
            val data = HexConvert.hexStringToBytes(et_input.text.trim().toString())
            val data1 = byteArrayOf(
                0xA5.toByte(), 0x55.toByte(), 0x01.toByte(), 0xFB.toByte()
            )
            val retcode = usbConnection?.bulkTransfer(usbEndpointOut, data1, data1.size, 2000)
            Log.d("serial", "the send ret code is ${retcode}")

        }


    }

    private fun startWriteThread() {

        thread {
            while (true) {
                val data1 = HexConvert.hexStringToBytes("A55501FB")
                val retcode = port.write(data1, 2000)
               /* if (retcode == data1.size) {
                    val raw = ByteArray(7)
                    val ret = port.read(raw, 2500)
                    if (ret >0) {
                        Log.d("serial", raw.contentToString())
                    }

                }*/
                Log.d("uuu", "the send ret code is ${retcode}")
                Thread.sleep(2000)
            }

        }

    }


    fun startReadThread() {
        thread {
            while (usbConnection == null) {
                continue
            }

            while (true) {
                /*  Log.d("serial", "开始")
                  val inMax: Int = 1
                  val byteBuffer: ByteBuffer = ByteBuffer.allocate(inMax)
                  val usbRequest = UsbRequest()
                  usbRequest.initialize(usbConnection, usbEndpointIn)
                  usbRequest.queue(byteBuffer, inMax)
                  usbConnection.requestWait()
                  val retData: ByteArray = byteBuffer.array()
                  for (byte1 in retData) {
                      System.err.println(byte1)
                  }
               *//*   if (usbConnection.requestWait() == usbRequest) {
                    val retData: ByteArray = byteBuffer.array()
                    for (byte1 in retData) {
                        System.err.println(byte1)
                    }
                }*/

                val byte = ByteArray(7)

                val ret = port.read(byte, 0)
                if (ret ?: 0 > 0) {
                    Log.d("serial read", HexStringUtil.bytes2HexString(byte))
                }


            }

        }

    }

}
