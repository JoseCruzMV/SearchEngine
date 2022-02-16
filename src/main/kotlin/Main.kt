class Person (val name: String,
              val lastName: String = "",
              val email: String = "")
{
    fun printPerson() = println("$name $lastName $email")
}

fun main() {
    getWords()
}

fun getWords() {
    val listOfPeople = getData()
    searchAction(listOfPeople = listOfPeople)
}

fun searchAction(listOfPeople: MutableList<Person>) {
    println("\nEnter the number of search queries:")
    var numberOfQueries = readLine()!!.toInt()
    while (numberOfQueries > 0) {
        findPerson(listOfPeople = listOfPeople)
        numberOfQueries--
    }
}

fun findPerson(listOfPeople: MutableList<Person>) {
    var foundPerson = false
    var personFoundHeadline = true
    println("\nEnter data to search people:")
    val query = readLine()!!
    for (person in listOfPeople) {
        if (person.name.contains(query, ignoreCase = true) ||
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

fun getData(): MutableList<Person> {
    val listOfPeople = mutableListOf<Person>()
    println("Enter the number of people:")
    var dataSize = readLine()!!.toInt()
    println("Enter all people: ")
    while (dataSize > 0) {
        val data = readLine()!!.split(" ")
        when (data.size) {
            1 -> listOfPeople.add(Person(name = data[0]))
            2 -> listOfPeople.add(Person(name = data[0], lastName = data[1]))
            else -> listOfPeople.add(Person(name = data[0], lastName = data[1], email = data[2]))
        }
        dataSize--
    }
    return listOfPeople
}

