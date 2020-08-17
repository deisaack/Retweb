package co.ke.deisaac.retweb.ui.main

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import co.ke.deisaac.retweb.R
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset
import java.util.*


class MainActivity : AppCompatActivity() {
    var bluetoothAdapter: BluetoothAdapter? = null
    var bluetoothSocket: BluetoothSocket? = null
    var bluetoothDevice: BluetoothDevice? = null
    var outputStream: OutputStream? = null
    var inputStream: InputStream? = null
    var thread: Thread? = null
    var readBuffer: ByteArray? = null
    private var readBufferPosition = 0


    @Volatile
    var stopWorker = false
    var lblPrinterName: TextView? = null
    var textBox: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create object of controls
        val btnConnect: Button = findViewById<View>(R.id.btnConnect) as Button
        val btnDisconnect: Button = findViewById<View>(R.id.btnDisconnect) as Button
        val btnPrint: Button = findViewById<View>(R.id.btnPrint) as Button
        textBox = findViewById<View>(R.id.editTextContent) as EditText
        lblPrinterName = findViewById<View>(R.id.lblPrinterName) as TextView
        btnConnect.setOnClickListener {
            try {
                FindBluetoothDevice()
                openBluetoothPrinter()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        btnDisconnect.setOnClickListener {
            try {
                disconnectBT()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        btnPrint.setOnClickListener {
            try {
                printData()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun FindBluetoothDevice() {
        try {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            if (bluetoothAdapter == null) {
                lblPrinterName!!.text = "No Bluetooth Adapter found"
            }
            if (bluetoothAdapter!!.isEnabled) {
                val enableBT = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBT, 0)
            }
            val pairedDevice =
                bluetoothAdapter!!.bondedDevices
            if (pairedDevice.size > 0) {
                for (pairedDev in pairedDevice) {

                    // My Bluetoth printer name is BTP_F09F1A
                    if (pairedDev.name == "BTP_F09F1A") {
                        bluetoothDevice = pairedDev
                        lblPrinterName!!.text = "Bluetooth Printer Attached: " + pairedDev.name
                        break
                    }
                }
            }
            lblPrinterName!!.text = "Bluetooth Printer Attached"
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    // Open Bluetooth Printer
    @Throws(IOException::class)
    fun openBluetoothPrinter() {
        try {

            //Standard uuid from string //
            val uuidSting = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
            bluetoothSocket = bluetoothDevice!!.createRfcommSocketToServiceRecord(uuidSting)
            bluetoothSocket!!.connect()
            outputStream = bluetoothSocket?.outputStream
            inputStream = bluetoothSocket?.inputStream
            beginListenData()
        } catch (ex: Exception) {
        }
    }

    fun beginListenData() {
        try {
            val handler = Handler()
            val delimiter: Byte = 10
            stopWorker = false
            readBufferPosition = 0
            readBuffer = ByteArray(1024)
            thread = Thread(Runnable {
                while (!Thread.currentThread().isInterrupted && !stopWorker) {
                    try {
                        val byteAvailable = inputStream!!.available()
                        if (byteAvailable > 0) {
                            val packetByte = ByteArray(byteAvailable)
                            inputStream!!.read(packetByte)
                            for (i in 0 until byteAvailable) {
                                val b = packetByte[i]
                                if (b == delimiter) {
                                    val encodedByte =
                                        ByteArray(readBufferPosition)
                                    System.arraycopy(
                                        readBuffer, 0,
                                        encodedByte, 0,
                                        encodedByte.size
                                    )
                                    val data = String(encodedByte, Charset.defaultCharset())//"US-ASCII")
                                    readBufferPosition = 0
                                    handler.post { lblPrinterName!!.text = data }
                                } else {
                                    readBuffer!![readBufferPosition++] = b
                                }
                            }
                        }
                    } catch (ex: Exception) {
                        stopWorker = true
                    }
                }
            })
            thread!!.start()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    // Printing Text to Bluetooth Printer //
    @Throws(IOException::class)
    fun printData() {
        try {
            var msg = textBox!!.text.toString()
            msg += "\n"
            outputStream!!.write(msg.toByteArray())
            lblPrinterName!!.text = "Printing Text..."
        } catch (ex: Exception) {
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
            bluetoothSocket!!.close()
            lblPrinterName!!.text = "Printer Disconnected."
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

//    fun doPrint(){
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, MainActivity.PERMISSION_BLUETOOTH);
//        } else {
//            // Your code HERE
//        }
//
//        val printer = EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 48f, 32)
//        printer.printFormattedText(
//                "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(
//                    printer,
//                    this.applicationContext.resources
//                        .getDrawableForDensity(R.drawable.logo, DisplayMetrics.DENSITY_MEDIUM)
//                ).toString() + "</img>\n" +
//                        "[L]\n" +
//                        "[C]<u><font size='big'>ORDER N°045</font></u>\n" +
//                        "[L]\n" +
//                        "[C]================================\n" +
//                        "[L]\n" +
//                        "[L]<b>BEAUTIFUL SHIRT</b>[R]9.99e\n" +
//                        "[L]  + Size : S\n" +
//                        "[L]\n" +
//                        "[L]<b>AWESOME HAT</b>[R]24.99e\n" +
//                        "[L]  + Size : 57/58\n" +
//                        "[L]\n" +
//                        "[C]--------------------------------\n" +
//                        "[R]TOTAL PRICE :[R]34.98e\n" +
//                        "[R]TAX :[R]4.23e\n" +
//                        "[L]\n" +
//                        "[C]================================\n" +
//                        "[L]\n" +
//                        "[L]<font size='tall'>Customer :</font>\n" +
//                        "[L]Raymond DUPONT\n" +
//                        "[L]5 rue des girafes\n" +
//                        "[L]31547 PERPETES\n" +
//                        "[L]Tel : +33801201456\n" +
//                        "[L]\n" +
//                        "[C]<barcode type='ean13' height='10'>831254784551</barcode>\n" +
//                        "[C]<qrcode size='20'>http://www.developpeur-web.dantsu.com/</qrcode>"
//            )
//    }

}

