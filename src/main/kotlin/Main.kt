fun main() {
    getWords()
}

fun getWords() {
    val listOfWords = getListOfWords()
    val wordToSearch = getSearchWord()
    findWord(listOfWords = listOfWords, searchWord = wordToSearch)
}

fun findWord(listOfWords: List<String>, searchWord: String) {
    val word = listOfWords.indexOf(searchWord)
    println( if (word >= 0) word + 1 else "Not Found")
}

fun getSearchWord() = readLine()!!

fun getListOfWords() = readLine()!!.split(" ")
