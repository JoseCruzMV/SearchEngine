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
    getUserOption(listOfPeople = listOfPeople)
}

/* Asks user to choose an action */
fun getUserOption(listOfPeople: MutableList<Person>) {
    while (true) {
        showMenu()
        when (readLine()!!.toIntOrNull()) {
            FIND_PERSON -> findPerson(listOfPeople = listOfPeople)
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

/* Asks for the number of searches to the user */
/*fun searchAction(listOfPeople: MutableList<Person>) {
    println("\nEnter the number of search queries:")
    var numberOfQueries = readLine()!!.toInt()
    while (numberOfQueries > 0) {
        findPerson(listOfPeople = listOfPeople)
        numberOfQueries--
    }
}*/

/* Look for given query in people list: searching by name, lastname or email */
fun findPerson(listOfPeople: MutableList<Person>) {
    var foundPerson = false
    var personFoundHeadline = true
    println("\nEnter a name or email to search all suitable people.")
    val query = readLine()!!
    for (person in listOfPeople) {
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
    }
    if (!foundPerson) println("No matching people found.")
}

/* Ask for user data */
fun getData(peopleData: List<String>): MutableList<Person> {
    val listOfPeople = mutableListOf<Person>()
    /*println("Enter the number of people:")
    var dataSize = readLine()!!.toInt()
    println("Enter all people: ")
    while (dataSize > 0) {
        val data = readLine()!!.split(" ")
        when (data.size) { // Different constructors according to user data
            1 -> listOfPeople.add(Person(name = data[0])) // only name
            2 -> listOfPeople.add(Person(name = data[0], lastName = data[1])) // name and last name
            else -> listOfPeople.add(Person(name = data[0], lastName = data[1], email = data[2])) // name, last name and email
        }
        dataSize--
    }*/

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


