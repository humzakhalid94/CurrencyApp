package mhk.app.currencyconverter.common

sealed class Events<T>(val data:T?=null,val message:String?=null) {
    class Success<T>(data: T?):Events<T>(data)
    class Error<T>(data: T?=null,message: String?) : Events<T>(data=data,message=message)
    class Loading<T>(data:T?=null) : Events<T>(data)

}