package iii_conventions

import java.util.*

data class MyDate(val year: Int, val month: Int, val dayOfMonth: Int) : Comparable<MyDate> {
    override fun compareTo(other: MyDate): Int {
        val thisDate = Calendar.getInstance()
        val otherDate = Calendar.getInstance()

        thisDate.set(year, month, dayOfMonth)
        otherDate.set(other.year, other.month, other.dayOfMonth)

        return ((thisDate.timeInMillis - otherDate.timeInMillis) / 86400000).toInt()
    }

}

class DateRange(override val start: MyDate, override val endInclusive: MyDate) : ClosedRange<MyDate>, Iterable<MyDate> {
    override fun iterator(): Iterator<MyDate> {
        return DateIterator(this)
    }

    override fun contains(value: MyDate): Boolean {
        return value >= start && value <= endInclusive
    }
}

operator fun MyDate.rangeTo(other: MyDate): DateRange = DateRange(this, other)

class DateIterator(private val dateRange: DateRange) : Iterator<MyDate> {
    private var current : MyDate = dateRange.start
    override fun hasNext(): Boolean = current <= dateRange.endInclusive

    override fun next(): MyDate {
        val temp = current
        current = current.nextDay()
        return temp
    }
}

enum class TimeInterval {
    DAY,
    WEEK,
    YEAR
}

class MultipliedInterval(val timeInterval: TimeInterval, val repeatCount: Int)

operator fun TimeInterval.times(number: Int) = MultipliedInterval(this, number)

operator fun MyDate.plus(timeInterval: TimeInterval) = addTimeIntervals(timeInterval, 1)
operator fun MyDate.plus(multipliedInterval: MultipliedInterval) = addTimeIntervals(multipliedInterval.timeInterval, multipliedInterval.repeatCount)
