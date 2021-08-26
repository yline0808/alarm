package net.ddns.yline.alarm.table

import java.io.Serializable

internal data class TimeRepeat(val minute: Int, val count: Int) : Serializable