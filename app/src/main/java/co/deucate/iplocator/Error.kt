package co.deucate.iplocator

data class Error(

        val message: String = "Please try again later",
        val query: String,
        val status: String

)