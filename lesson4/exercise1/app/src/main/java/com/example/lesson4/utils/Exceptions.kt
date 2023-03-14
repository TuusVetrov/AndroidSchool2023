package com.example.lesson4.utils

import java.io.IOException

class Exceptions {
    class ApiException(message: String) : IOException(message)
}