package net.ddns.yline.alarm.table

import java.io.Serializable

data class Vibration(val name:String, val timing:LongArray) : Serializable