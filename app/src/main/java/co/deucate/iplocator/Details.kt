package co.deucate.iplocator

data class Details(

        val dat: String,
        val region: String = "NY",
        val regionName: String = "New York",
        val timeZone: String = "UTC",
        val zip: String,
        val country: String,
        val city: String,
        val org: String,
        val countryCode: String,
        val isp: String,
        val query: String,
        val lon: Double = 0.0,
        val lat: Double = 0.0,
        val status: String

)