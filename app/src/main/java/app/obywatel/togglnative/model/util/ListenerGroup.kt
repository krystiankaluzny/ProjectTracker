package app.obywatel.togglnative.model.util

open class ListenerGroup<T> {

    protected val listeners: MutableList<T> = mutableListOf()

    operator fun plusAssign(listener: T) {
        listeners += listener
    }

    operator fun minusAssign(listener: T) {
        listeners -= listener
    }
}

