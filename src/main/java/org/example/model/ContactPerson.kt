package org.example.model

import com.google.api.services.people.v1.model.Person
import org.json.JSONException
import org.json.JSONObject

data class ContactPerson(
    val name: String,
    val birthday: String
) {
    companion object {
        fun fromPerson(person: Person): ContactPerson? {
            if (person.names != null && person.names.isNotEmpty() &&
                person.birthdays != null && person.birthdays.isNotEmpty()) {
                val name = person.names[0].displayName;
                val birthday = parseJson(person.birthdays[0].toString())

                if(name.contains(Regex("[^a-zA-Z0-9\\s]"))) {
                    val cleanedName = name.replace(Regex("[^a-zA-Z0-9\\s]"), "").removeSuffix(" ")
                    return ContactPerson(cleanedName, birthday)
                }
                return ContactPerson(name, birthday)
            }
            return null
        }

        private fun parseJson(jsonString: String): String {
            try {
                val jsonObject = JSONObject(jsonString)

                val dateObject = jsonObject.getJSONObject("date")
                val day = dateObject.getInt("day")
                val month = dateObject.getInt("month")

                try {
                    val year = dateObject.getInt("year")
                    return "$day/$month/$year"
                } catch (ignored: JSONException) {}

                return "$day/$month"

            } catch (e: JSONException) {
                println(e.message)
                return ""
            }
        }
    }
}