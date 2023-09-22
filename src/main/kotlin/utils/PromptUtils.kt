package utils

fun promptLanguage(): String {
    val validLanguages = listOf("en", "ru", "fr", "de", "uk", "be", "pt", "it") // добавляем поддерживаемые языки сюда
    var language = ""
    while (language !in validLanguages) {
        print("Choose a language to search for articles (en, ru, fr and others): ")
        language = readlnOrNull() ?: ""
        if (language !in validLanguages) {
            println("Invalid language. Please choose from the provided options.")
        }
    }
    return language
}

fun promptQuery(): String {
    print("Enter your request for wikipedia: ")
    return readlnOrNull() ?: ""
}