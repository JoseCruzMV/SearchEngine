import java.io.File

class Person (val name: String,
              val lastName: String = "",
              val email: String = "")
{
    fun printPerson() = println( if (email.isNotEmpty()) "$name $lastName $email" else "$name $lastName")
}

const val FIND_PERSON = 1
const val PRINT_ALL_PEOPLE = 2
const val EXIT = 0

fun main(args: Array<String>) {
    getWords(fileLocation = args[1])
}

fun getWords(fileLocation: String) {
    val file = File(fileLocation)
    val list = file.readLines()
    val listOfPeople = getData(peopleData = list)
    val peopleInvertedIndex = builtPeopleInvertedIndex(listOfPeople = listOfPeople)
    getUserOption(listOfPeople = listOfPeople, peopleInvertedIndex = peopleInvertedIndex)
}

/* It will create a map of with the position of each
element occurs (name, lastname and email) */
fun builtPeopleInvertedIndex(listOfPeople: MutableList<Person>): Map<String, MutableSet<Int>> {
    val peopleInvertedIndex = mutableMapOf<String, MutableSet<Int>>()

    for (i in listOfPeople.indices) {
        if (listOfPeople[i].name.isNotEmpty()) { // If name is not an empty String
            if (peopleInvertedIndex.containsKey(listOfPeople[i].name.lowercase())) { // if the name already exists in map
                peopleInvertedIndex[listOfPeople[i].name.lowercase()]!!.add(i) // It will be sure of not return null and will add the index
            } else { // if the name is new to the map
                peopleInvertedIndex[listOfPeople[i].name.lowercase()] = mutableSetOf(i) // It adds de name and a new int list
            }
        }
        if (listOfPeople[i].lastName.isNotEmpty()) {
            if (peopleInvertedIndex.containsKey(listOfPeople[i].lastName.lowercase())) {
                peopleInvertedIndex[listOfPeople[i].lastName.lowercase()]!!.add(i)
            } else { // if the name is new to the map
                peopleInvertedIndex[listOfPeople[i].lastName.lowercase()] = mutableSetOf(i)
            }
        }
        if (listOfPeople[i].email.isNotEmpty()) {
            if (peopleInvertedIndex.containsKey(listOfPeople[i].email.lowercase())) {
                peopleInvertedIndex[listOfPeople[i].email.lowercase()]!!.add(i)
            } else { // if the name is new to the map
                peopleInvertedIndex[listOfPeople[i].email.lowercase()] = mutableSetOf(i)
            }
        }
    }
//    peopleInvertedIndex.forEach { println("${it.key} -> ${it.value.joinToString()}") }
    return peopleInvertedIndex.toMap()
}

/* Asks user to choose an action */
fun getUserOption(listOfPeople: MutableList<Person>, peopleInvertedIndex: Map<String, MutableSet<Int>>) {
    while (true) {
        showMenu()
        when (readLine()!!.toIntOrNull()) {
            FIND_PERSON -> findPerson(listOfPeople = listOfPeople, peopleInvertedIndex = peopleInvertedIndex)
            PRINT_ALL_PEOPLE -> printAllPeople(listOfPeople = listOfPeople)
            EXIT -> {
                println("\nBye!")
                break
            }
            else -> println("Incorrect option! Try again.")
        }
    }
}

/* Prints all people list */
fun printAllPeople(listOfPeople: MutableList<Person>) {
    println("\n=== List of people ===")
    listOfPeople.forEach { it.printPerson() }
}

/* Users menu */
fun showMenu() {
    println()
    println("=== Menu ===")
    println("1. Find a person")
    println("2. Print all people")
    println("0. Exit")
}

/* Look for given query in people list: searching by name, lastname or email */
fun findPerson(listOfPeople: MutableList<Person>, peopleInvertedIndex: Map<String, MutableSet<Int>>) {
    var foundPerson = false
    var personFoundHeadline = true
    println("\nEnter a name or email to search all matching people.")
    val query = readLine()!!.lowercase()
    /*for (person in listOfPeople) {
        if (person.name.contains(query, ignoreCase = true) || // if person name is similar to given query
            person.lastName.contains(query, ignoreCase = true) ||
            person.email.contains(query, ignoreCase = true)
        ) {
            if (personFoundHeadline) {
                println("\nPeople found:")
                personFoundHeadline = false
            }
            person.printPerson()
            foundPerson = true
        }
    }*/
    if (peopleInvertedIndex.containsKey(query)) {
        val peopleFound = peopleInvertedIndex[query]!!
        println("${peopleFound.size} person" + if (peopleFound.size > 1) "s " else " ")
        println("found:")
        for (i in peopleFound) {
            listOfPeople[i].printPerson()
        }
    } else {
        println("No matching people found.")
    }
}

/* Ask for user data */
fun getData(peopleData: List<String>): MutableList<Person> {
    val listOfPeople = mutableListOf<Person>()

    for (person in peopleData) {
        val data = person.split(" ")
        when (data.size) { // Different constructors according to user data
            1 -> listOfPeople.add(Person(name = data[0])) // only name
            2 -> listOfPeople.add(Person(name = data[0], lastName = data[1])) // name and last name
            else -> listOfPeople.add(Person(name = data[0], lastName = data[1], email = data[2])) // name, last name and email
        }
    }
    return listOfPeople
}


