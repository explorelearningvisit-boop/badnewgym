package com.example.badnewgym.feature.memberintelligence.domain.format

import java.text.NumberFormat
import java.util.Locale

object MoneyFormat {
    fun inr(amount: Double): String {
        val formatted = NumberFormat.getIntegerInstance(Locale("en", "IN")).format(amount.toLong())
        return "\u20B9 $formatted"
    }
}
