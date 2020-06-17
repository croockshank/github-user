package com.genadidharma.github.ui.util

import kotlin.math.ln
import kotlin.math.pow

class Constants {
    companion object {
        const val BLUR_RADIUS = 15
        const val SKELETON_ITEM_COUNT = 10

        fun convertNumber(count: Int?): String {
            if (count != null) {

                if (count < 1000) return "" + count;
                val exp = (Math.log(count.toDouble()) / ln(1000.0));
                return String.format(
                    "%.0f%c",
                    count / 1000.0.pow(exp),
                    "kMGTPE"[(exp - 1).toInt()]
                );
            }
            return "-"
        }
    }
}