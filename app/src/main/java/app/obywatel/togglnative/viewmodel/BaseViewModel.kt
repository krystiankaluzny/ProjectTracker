package app.obywatel.togglnative.viewmodel

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

interface ErrorViewModel {
    fun blinkError(error: String)
    fun blinkException(exception: Exception)
    fun showErrorMessage(msg: String?)
    fun hideErrorMessage()
}

abstract class BaseViewModel : ErrorViewModel {

    companion object {
        private const val ERROR_MESSAGE_SHOW_TIME = 10_000L
        private const val UNKNOWN_ERROR_MESSAGE = "Unknown Message"
    }

    val errorMessageVisible = ObservableBoolean(false)
    val errorMessage = ObservableField<String>("")

    fun onClickErrorMessage() = hideErrorMessage()

    override fun blinkError(error: String) {

        launch(UI) {
            showErrorMessage(error)
            delay(ERROR_MESSAGE_SHOW_TIME)
            hideErrorMessage()
        }
    }

    override fun blinkException(exception: Exception) {
        blinkError(exception.message ?: UNKNOWN_ERROR_MESSAGE)
    }

    override fun showErrorMessage(msg: String?) {
        errorMessage.set(msg)
        errorMessageVisible.set(true)
    }

    override fun hideErrorMessage() {
        errorMessageVisible.set(false)
        errorMessage.set("")
    }
}