package daniel.brian.originhack.utils

sealed class Resource <out T>(
    val data: T? = null,
    val message: String? = null,
){
    class Success<T>(data: T?) : Resource<T>(data)
    class Error(message: String?) : Resource<Nothing>(message = message)
    class Loading<T> : Resource<T>()
    class Unspecified<T> : Resource<T>()
}