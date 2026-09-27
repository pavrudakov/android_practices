package com.example.android_practice.data.repository

import com.example.android_practice.data.model.Brewery

class MockBreweryRepository : BreweryRepository {

    private val mockBreweries = listOf(
        Brewery(
            id = "2907b143-57b4-49ec-aa41-07df64d1e14b",
            name = "MadTree Brewing",
            breweryType = "regional",
            street = "3301 Madison Rd",
            city = "Cincinnati",
            stateProvince = "Ohio",
            postalCode = "45209-1132",
            country = "United States",
            phone = "5138368595",
            websiteUrl = "http://www.madtreebrewing.com",
            latitude = 39.1563725,
            longitude = -84.4239715
        ),
        Brewery(
            id = "51240f6f-e020-4e7b-9e33-ae6b3597e29d",
            name = "Sierra Nevada Brewing Co",
            breweryType = "large",
            street = "1075 E 20th St",
            city = "Chico",
            stateProvince = "California",
            postalCode = "95928-6850",
            country = "United States",
            phone = "5308933555",
            websiteUrl = "http://www.sierranevada.com",
            latitude = 39.7246028,
            longitude = -121.8157154
        ),
        Brewery(
            id = "6b4e6a8e-2495-46f0-a33d-71b312b9d041",
            name = "Dogfish Head Craft Brewery",
            breweryType = "regional",
            street = "6 Cannery Village Center",
            city = "Milton",
            stateProvince = "Delaware",
            postalCode = "19968-1269",
            country = "United States",
            phone = "3026841000",
            websiteUrl = "http://www.dogfish.com",
            latitude = 38.771239,
            longitude = -75.311742
        ),
        Brewery(
            id = "9c372f88-4621-4f8e-bf33-2a4bdf34110a",
            name = "Stone Brewing",
            breweryType = "regional",
            street = "1999 Citracado Pkwy",
            city = "Escondido",
            stateProvince = "California",
            postalCode = "92029-1311",
            country = "United States",
            phone = "7602947866",
            websiteUrl = "http://www.stonebrewing.com",
            latitude = 33.1158308,
            longitude = -117.1200234
        ),
        Brewery(
            id = "a1835697-3932-4e4b-97e3-05f32bdf602a",
            name = "Russian River Brewing Co",
            breweryType = "brewpub",
            street = "725 4th St",
            city = "Santa Rosa",
            stateProvince = "California",
            postalCode = "95404-4408",
            country = "United States",
            phone = "7075452337",
            websiteUrl = "http://www.russianriverbrewing.com",
            latitude = 38.441221,
            longitude = -122.711894
        ),
        Brewery(
            id = "b8451122-38ef-4171-884d-222a76f2d2bb",
            name = "Deschutes Brewery",
            breweryType = "regional",
            street = "901 SW Simpson Ave",
            city = "Bend",
            stateProvince = "Oregon",
            postalCode = "97702-3118",
            country = "United States",
            phone = "5413858606",
            websiteUrl = "http://www.deschutesbrewery.com",
            latitude = 44.047514,
            longitude = -121.323674
        ),
        Brewery(
            id = "c1294875-12aa-4bb5-985e-990a88dfb211",
            name = "Founders Brewing Co",
            breweryType = "regional",
            street = "235 Grandville Ave SW",
            city = "Grand Rapids",
            stateProvince = "Michigan",
            postalCode = "49503-4039",
            country = "United States",
            phone = "6167761195",
            websiteUrl = "http://www.foundersbrewing.com",
            latitude = 42.958564,
            longitude = -85.674068
        ),
        Brewery(
            id = "d3019811-54bc-418a-a92c-1123eab9f011",
            name = "Bells Brewery",
            breweryType = "regional",
            street = "8938 Krum Ave",
            city = "Galesburg",
            stateProvince = "Michigan",
            postalCode = "49053-9721",
            country = "United States",
            phone = "2693822332",
            websiteUrl = "http://www.bellsbeer.com",
            latitude = 42.285223,
            longitude = -85.452668
        ),
        Brewery(
            id = "e4581109-192a-4ef8-a28d-1928374a2001",
            name = "Allagash Brewing Company",
            breweryType = "micro",
            street = "50 Industrial Way",
            city = "Portland",
            stateProvince = "Maine",
            postalCode = "04103-1029",
            country = "United States",
            phone = "8003305385",
            websiteUrl = "http://www.allagash.com",
            latitude = 43.702951,
            longitude = -70.317585
        ),
        Brewery(
            id = "f5928123-091a-4ab2-881e-2819033f1122",
            name = "New Belgium Brewing Co",
            breweryType = "large",
            street = "500 Linden St",
            city = "Fort Collins",
            stateProvince = "Colorado",
            postalCode = "80524-2452",
            country = "United States",
            phone = "9702210524",
            websiteUrl = "http://www.newbelgium.com",
            latitude = 40.589886,
            longitude = -105.067332
        ),
        Brewery(
            id = "11223344-5566-7788-9900-aabbccddeeff",
            name = "Anchor Brewing Co",
            breweryType = "closed",
            street = null,
            city = "San Francisco",
            stateProvince = "California",
            postalCode = "94107",
            country = "United States",
            phone = null,
            websiteUrl = null,
            latitude = null,
            longitude = null
        ),
        Brewery(
            id = "99887766-5544-3322-1100-ffeeddccbbaa",
            name = "Rhinegeist Brewery",
            breweryType = "micro",
            street = "1910 Elm St",
            city = "Cincinnati",
            stateProvince = "Ohio",
            postalCode = "45202-3023",
            country = "United States",
            phone = "5138132739",
            websiteUrl = "http://www.rhinegeist.com",
            latitude = 39.117183,
            longitude = -84.519782
        )
    )

    override suspend fun getBreweries(): List<Brewery> {
        return mockBreweries
    }

    override suspend fun getBreweryById(id: String): Brewery? {
        return mockBreweries.find { it.id == id }
    }
}
