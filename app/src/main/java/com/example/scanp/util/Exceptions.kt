package com.example.scanp.util

class ProductNotFoundException(val barcode: String): Exception("Product not found: $barcode")

class ApiHttpException(val code: Int): Exception("API error: $code")