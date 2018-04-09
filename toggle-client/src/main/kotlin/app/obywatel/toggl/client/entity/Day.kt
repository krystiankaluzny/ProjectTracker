package app.obywatel.toggl.client.entity

enum class Day(val value: Byte) {
    SUNDAY(0),
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6);

    companion object {
        private val map = Day.values().associateBy(Day::value)
        fun fromInt(value: Byte) = map[value] ?: throw IllegalArgumentException("Request value out of [0, 6] range")
    }
}