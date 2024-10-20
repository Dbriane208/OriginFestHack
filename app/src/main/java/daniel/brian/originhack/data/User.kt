package daniel.brian.originhack.data

data class User (
    val username: String,
    val email: String,
    val phone: String,
){
    // Firebase uses this constructor
    constructor() : this ("","","")
}
