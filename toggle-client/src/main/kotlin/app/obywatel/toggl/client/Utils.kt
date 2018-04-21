package app.obywatel.toggl.client

open class EnumCompanion<in T, out V>(private val valueMap: Map<T, V>) {

    fun fromValue(type: T) = valueMap[type] ?: throw IllegalArgumentException()
}