package com.banquemisr.currency.ui.core

object TimeAgo {
    private const val SECOND_MILLIS = 1000
    private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private const val DAY_MILLIS = 24 * HOUR_MILLIS

    fun getTimeAgo(time: Long): String {
        var time = time
        when {
            time < 1000000000000L -> {
                time *= 1000
            }
        }
        val now = System.currentTimeMillis()
        when {
            time > now || time <= 0 -> {
                return ""
            }
            else -> {
                val diff = now - time
                return when {
                    diff < MINUTE_MILLIS -> "just now"
                    diff < 2 * MINUTE_MILLIS -> "a minute ago"
                    diff < 3 * MINUTE_MILLIS -> "two minutes ago"
                    diff < 50 * MINUTE_MILLIS -> "${diff / MINUTE_MILLIS} minutes ago"
                    diff < 90 * MINUTE_MILLIS -> "an hour ago"
                    diff < 90 * 2 * MINUTE_MILLIS -> "two hours ago"
                    diff < 11 * HOUR_MILLIS -> "${diff / HOUR_MILLIS} hours ago"
                    diff < 24 * HOUR_MILLIS -> "${diff / HOUR_MILLIS} hours ago"
                    diff < 48 * HOUR_MILLIS -> "yesterday"
                    diff / DAY_MILLIS < 3 -> "2 days ago"
                    diff / DAY_MILLIS in 3..11 -> "${diff / DAY_MILLIS} days ago"
                    else -> "${diff / DAY_MILLIS} days ago"
                }
            }
        }
    }
}