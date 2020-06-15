package com.company

import java.time.LocalDate

class Spent (var dateSpent: LocalDate, var amntSpent: Double, var ctgSpent: Category) : Comparable<Spent>{

    override operator fun compareTo(o: Spent): Int {
        return this.ctgSpent.compareTo(o.ctgSpent)
    }
}