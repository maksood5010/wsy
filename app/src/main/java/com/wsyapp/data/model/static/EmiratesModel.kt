package com.wsyapp.data.model.static

data class EmiratesModel(val name: String, val city_id: String) {

    constructor() : this("", "")

    fun getLocationList(): List<EmiratesModel> {
        var emratesList = mutableListOf<EmiratesModel>()

        val emiratesModel1 = EmiratesModel("Abu Dhabi", "1")
        val emiratesModel2 = EmiratesModel("Dubai", "2")
        val emiratesModel3 = EmiratesModel("Sharjah", "3")
        val emiratesModel4 = EmiratesModel("Umm al-Quwain", "4")
        val emiratesModel5 = EmiratesModel("Ajman", "5")
        val emiratesModel6 = EmiratesModel("Ras al-Khaimah", "6")
        val emiratesModel7 = EmiratesModel("Fujairah", "7")
        val emiratesModel8 = EmiratesModel("Al Ain", "8")

        emratesList.add(emiratesModel1)
        emratesList.add(emiratesModel2)
        emratesList.add(emiratesModel3)
        emratesList.add(emiratesModel4)
        emratesList.add(emiratesModel5)
        emratesList.add(emiratesModel6)
        emratesList.add(emiratesModel7)
        emratesList.add(emiratesModel8)
        return emratesList
    }
}