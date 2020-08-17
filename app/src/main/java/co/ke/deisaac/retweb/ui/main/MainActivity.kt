package co.ke.deisaac.retweb.ui.main

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import co.ke.deisaac.retweb.R
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset
import java.util.*


internal class MainActivity : AppCompatActivity() {
    var bluetoothAdapter: BluetoothAdapter? = null
    var bluetoothDevice: BluetoothDevice? = null
    var bluetoothSocket: BluetoothSocket? = null
    var inputStream: InputStream? = null
    var outputStream: OutputStream? = null
    var stopWorker: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findBluetoothDevice()
    }

    private fun findBluetoothDevice() {
        try {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            if (bluetoothAdapter == null) {
                lblPrinterName.setText("No Bluetooth Adapter found")
            }
            if (bluetoothAdapter!!.isEnabled()) {
                val enableBT = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBT, 0)
            }
            val pairedDevice: Set<BluetoothDevice> =
                bluetoothAdapter!!.getBondedDevices()
            if (pairedDevice.size > 0) {
                for (pairedDev in pairedDevice) {

                    // My Bluetoth printer name is BTP_F09F1A
                    if (pairedDev.name == "BTP_F09F1A") {
                        val bluetoothDevice = pairedDev
                        lblPrinterName.setText("Bluetooth Printer Attached: " + pairedDev.name)
                        break
                    }
                }
            }
            lblPrinterName.setText("Bluetooth Printer Attached")
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    @Throws(IOException::class)
    fun openBluetoothPrinter() {
        try {

            //Standard uuid from string //
            val uuidSting: UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
            bluetoothSocket = bluetoothDevice?.createRfcommSocketToServiceRecord(uuidSting)
            bluetoothSocket?.connect()
            outputStream = bluetoothSocket?.outputStream
            inputStream = bluetoothSocket?.inputStream
            beginListenData()
        } catch (ex: java.lang.Exception) {
        }
    }

    private fun beginListenData() {
        try {
            val handler = Handler()
            val delimiter: Byte = 10
            stopWorker = false
            var readBufferPosition = 0
            val readBuffer = ByteArray(1024)
            val thread = Thread(Runnable {
                while (!Thread.currentThread().isInterrupted && !stopWorker) {
                    try {
                        val byteAvailable: Int = inputStream!!.available()
                        if (byteAvailable > 0) {
                            val packetByte = ByteArray(byteAvailable)
                            inputStream?.read(packetByte)
                            for (i in 0 until byteAvailable) {
                                var b = packetByte[i]
                                if (b == delimiter) {
                                    val encodedByte =
                                        ByteArray(readBufferPosition)
                                    System.arraycopy(
                                        readBuffer, 0,
                                        encodedByte, 0,
                                        encodedByte.size
                                    )
                                    val data = String(encodedByte, Charset.defaultCharset())
                                    readBufferPosition = 0
                                    handler.post(Runnable { lblPrinterName.setText(data) })
                                } else {
                                    b = readBuffer.get(readBufferPosition)
//                                    readBuffer.get(readBufferPosition++) = b
                                }
                            }
                        }
                    } catch (ex: java.lang.Exception) {
                        stopWorker = true
                    }
                }
            })
            thread.start()
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
    }

    @Throws(IOException::class)
    fun printData() {
        try {
            var msg: String = editTextContent.text.toString()
            msg += "\n"
            outputStream!!.write(msg.toByteArray())
            lblPrinterName.text = "Printing Text..."
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
    }

    // Disconnect Printer //
    @Throws(IOException::class)
    fun disconnectBT() {
        try {
            stopWorker = true
            outputStream!!.close()
            inputStream!!.close()
            bluetoothSocket?.close()
            lblPrinterName.text = "Printer Disconnected."
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
    }
}

