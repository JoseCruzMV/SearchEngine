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

enum class SearchStrategies{
    ALL, ANY, NONE
}

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
            FIND_PERSON -> setSearchStrategy(listOfPeople = listOfPeople, peopleInvertedIndex = peopleInvertedIndex)
            PRINT_ALL_PEOPLE -> printAllPeople(listOfPeople = listOfPeople)
            EXIT -> {
                println("\nBye!")
                break
            }
            else -> println("Incorrect option! Try again.")
        }
    }
}

/* Asks user for searching strategy and calls the find person method */
fun setSearchStrategy(listOfPeople: MutableList<Person>, peopleInvertedIndex: Map<String, MutableSet<Int>>) {
    println("Select a matching strategy: ALL, ANY, NONE")
    when(val strategy = readLine()!!) {
        SearchStrategies.ALL.name -> findPerson(listOfPeople = listOfPeople, peopleInvertedIndex = peopleInvertedIndex, searchStrategy = strategy)
        SearchStrategies.ANY.name -> findPerson(listOfPeople = listOfPeople, peopleInvertedIndex = peopleInvertedIndex, searchStrategy = strategy)
        SearchStrategies.NONE.name -> findPerson(listOfPeople = listOfPeople, peopleInvertedIndex = peopleInvertedIndex, searchStrategy = strategy)
        else -> return
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
fun findPerson(
    listOfPeople: MutableList<Person>,
    peopleInvertedIndex: Map<String, MutableSet<Int>>,
    searchStrategy: String
) {
    println("\nEnter a name or email to search all matching people.")
    // Use lowercase to make insensitive case
    val query = readLine()!!.split(" ").map { it.lowercase() }
    val peopleFound = mutableSetOf<Int>()
    var successfulSearch = true
    var first = true
    when(searchStrategy){
        SearchStrategies.ALL.name -> {
            for (element in query) {
                if (peopleInvertedIndex.containsKey(element) && first) { // search the first term and creates a reference
                    peopleFound.addAll(peopleInvertedIndex[element]!!)
                    first = false
                } else if (peopleInvertedIndex.containsKey(element)) { // search new terms after first
                    val aux = mutableSetOf<Int>() // aux
                    // add all the new terms that were not in the first search
                    peopleFound.forEach { if (!peopleInvertedIndex[element]!!.contains(it)) aux.add(it) }
                    peopleFound.removeAll(aux) // removes all the terms that appears only this search
                } else {
                    successfulSearch = false
                    break
                }
            }
        }
        SearchStrategies.ANY.name -> {
            for (element in query) {
                if (peopleInvertedIndex.containsKey(element)){
                    peopleFound.addAll(peopleInvertedIndex[element]!!) // It will add all elements that match with query
                }
            }
            if (peopleFound.size <= 0) successfulSearch = false // If there was no match the search is unsuccessful
        }
        SearchStrategies.NONE.name -> {
            val aux = listOfPeople.indices.toMutableSet()   // set with all possible indices of people
            for (element in query) {
                if (peopleInvertedIndex.containsKey(element)) {
                    successfulSearch = true
                    aux.removeAll(peopleInvertedIndex[element]!!) // removes all the indices found
                } else {
                    successfulSearch = false
                }
            }
            peopleFound.addAll(aux) // Gives peopleFound the indices not removed
        }
    }

    if (successfulSearch) {
        print("${peopleFound.size} person" + if (peopleFound.size > 1) "s " else " ")
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


