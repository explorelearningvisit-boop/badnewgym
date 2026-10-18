package com.example.badnewgym.feature.memberintelligence.domain.format

import java.text.NumberFormat
import java.util.Locale

object MoneyFormat {
    /**
     * Renders an amount as `₹2,800`.
     *
     * The rupee sign sits flush against the digits (no space) so the value matches the
     * approved design and stays compact inside narrow surfaces such as the 200dp card.
     */
    fun inr(amount: Double): String {
        val formatted = NumberFormat.getIntegerInstance(Locale("en", "IN")).format(amount.toLong())
        return "\u20B9$formatted"
    }
}
