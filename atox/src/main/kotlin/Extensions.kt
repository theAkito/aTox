package ltd.evilcorp.atox

import androidx.fragment.app.Fragment
import ltd.evilcorp.atox.di.ViewModelFactory

val Fragment.vmFactory: ViewModelFactory
    get() = (requireActivity() as MainActivity).vmFactory

class NoSuchArgumentException(arg: String) : Exception("No such argument: $arg")

fun Fragment.requireStringArg(key: String) =
    arguments?.getString(key) ?: throw NoSuchArgumentException(key)

fun String.truncated(length: Int): String =
    if (this.length > length) {
        this.take(length - 1) + "…"
    } else {
        this
    }
