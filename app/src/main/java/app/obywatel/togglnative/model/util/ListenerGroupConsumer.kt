package app.obywatel.togglnative.model.util

class ListenerGroupConsumer<T> : ListenerGroup<T>() {

    fun accept(action: (T) -> Unit) = listeners.forEach(action)
}