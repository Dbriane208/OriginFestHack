package daniel.brian.originhack.utils

import android.util.Patterns

sealed class RegisterValidation {
    data object Success : RegisterValidation()
    data class Failed(val message: String) : RegisterValidation()
}

data class RegisterFieldState(
    val email: RegisterValidation,
    val password: RegisterValidation
)

fun validateEmail(email: String) : RegisterValidation{
    if (email.isEmpty()){
        return RegisterValidation.Failed("Email cannot be empty")
    }

    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        return RegisterValidation.Failed("Wrong Email format")
    }

    return  RegisterValidation.Success
}

fun validatePassword(password: String): RegisterValidation{
    if (password.isEmpty()){
        return RegisterValidation.Failed("Password cannot be empty")
    }

    if (password.length < 6){
        return RegisterValidation.Failed("Password should contain at least 6 characters")
    }

    if (password.length > 20){
        return RegisterValidation.Failed("Password should not contain more than 20 characters")
    }

    return RegisterValidation.Success
}