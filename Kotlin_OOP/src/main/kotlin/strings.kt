fun main() {
    val string = "help me"
    println("Please, ${string.toUpperCase().reversed()}")

    val inputStr = requestStr()
    println(inputStr!!.toUpperCase())

    for(i in 'z' downTo 'A' step 2) {
        println(i)
    }

    val arr = arrayOfNulls<Int>(10)
    arr[1] = 1
    println(arr.size)
}

fun requestStr(): String? {
    return readLine()
}