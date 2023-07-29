package com.example.readsms

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_SMS),
                11
            )
        } else {
//            readSms()
            readSms2()
        }
    }

    private fun readSms() {
        val numberCol = Telephony.TextBasedSmsColumns.ADDRESS
        val textCol = Telephony.TextBasedSmsColumns.BODY
        val typeCol = Telephony.TextBasedSmsColumns.TYPE // 1 - Inbox, 2 - Sent

        val projection = arrayOf(numberCol, textCol, typeCol)

        val cursor = contentResolver.query(
            Telephony.Sms.CONTENT_URI,
            projection, null, null, null
        )

        val numberColIdx = cursor!!.getColumnIndex(numberCol)
        val textColIdx = cursor.getColumnIndex(textCol)
        val typeColIdx = cursor.getColumnIndex(typeCol)

        while (cursor.moveToNext()) {
            val number = cursor.getString(numberColIdx)
            val text = cursor.getString(textColIdx)
            val type = cursor.getString(typeColIdx)

            Log.d("MY_APP", "$number $text $type")
        }

        cursor.close()
    }

    private fun readSms2() {
        // Create Inbox box URI
        val inboxURI: Uri = Uri.parse("content://sms/inbox")

// List required columns
        val reqCols = arrayOf("_id", "address", "body", "date")

// Get Content Resolver object, which will deal with Content Provider
        val cr = contentResolver

// Fetch Inbox SMS Message from Built-in Content Provider
        val cursor: Cursor? = cr.query(inboxURI, reqCols, null, null, null)

        val numberColIdx = cursor!!.getColumnIndex(reqCols[0])
        val textColIdx = cursor.getColumnIndex(reqCols[1])
        val typeColIdx = cursor.getColumnIndex(reqCols[2])
        val dateIdx = cursor.getColumnIndex(reqCols[3])

        while (cursor.moveToNext()) {
            val number = cursor.getString(numberColIdx)
            val text = cursor.getString(textColIdx)
            val type = cursor.getString(typeColIdx)
            val date = cursor.getString(dateIdx)

            val c = Calendar.getInstance()
            c.timeInMillis = date.toLong()
            val dateFormatted = "${c[Calendar.DAY_OF_MONTH]}/${c[Calendar.MONTH].plus(1)}/${c[Calendar.YEAR]}"
//            val dateFormatted = "${c.time}"

            Log.d("MY_APP", "$number $text $type $dateFormatted")
        }

        cursor.close()

    }
    
}