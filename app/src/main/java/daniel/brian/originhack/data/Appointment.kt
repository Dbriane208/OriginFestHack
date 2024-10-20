package daniel.brian.originhack.data

data class Appointment(
    val meeting: String,
    val dateTime: String,
    val name: String,
){
    constructor(): this( "","","" )
}