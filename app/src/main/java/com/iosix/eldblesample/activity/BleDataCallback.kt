package com.iosix.eldblesample.activity

import com.iosix.eldblelib.EldBleDataCallback
import com.iosix.eldblelib.EldBroadcast
import com.iosix.eldblelib.EldBroadcastTypes

class BleDataCallback: EldBleDataCallback() {

    override fun OnDataRecord(dataRec: EldBroadcast?, RecordType: EldBroadcastTypes?) {
        super.OnDataRecord(dataRec, RecordType)


        val callback: EldBleDataCallback = object : EldBleDataCallback() {
            override fun OnDataRecord(dataRec: EldBroadcast?, RecordType: EldBroadcastTypes?) {

            }
        }
    }
}