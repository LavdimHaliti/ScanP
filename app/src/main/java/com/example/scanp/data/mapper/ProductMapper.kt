package com.example.scanp.data.mapper

import com.example.scanp.data.domain.ProductDomain
import com.example.scanp.data.local.entities.ProductEntity
import com.example.scanp.data.remote.dto.ProductDto

/**
 * Mapper utilities to convert between DTO, Entity, and Domain models.
 * The `isSaved` flag indicates whether the product already exists in the local database.
 */

fun ProductEntity.toProductDomain(isSaved: Boolean = false): ProductDomain = ProductDomain(
    barcode = barcode,
    name = name,
    ingredients = ingredients,
    energyKcal = energyKcal,
    proteins = proteins,
    carbs = carbs,
    sugars = sugars,
    fat = fat,
    saturatedFat = saturatedFat,
    fibre = fibre,
    salt = salt,
    isSaved = isSaved
)

fun ProductDomain.toProductEntity(): ProductEntity = ProductEntity(
    barcode = barcode,
    name = name,
    ingredients = ingredients,
    energyKcal = energyKcal,
    proteins = proteins,
    carbs = carbs,
    sugars = sugars,
    fat = fat,
    saturatedFat = saturatedFat,
    fibre = fibre,
    salt = salt
)

fun ProductDto.toProductDomain(barcode: String, isSaved: Boolean = false): ProductDomain =
    ProductDomain(
        barcode = barcode,
        name = productName,
        ingredients = ingredientsText,
        energyKcal = nutriments?.energyKcal,
        proteins = nutriments?.proteins,
        carbs = nutriments?.carbohydrates,
        sugars = nutriments?.sugars,
        fat = nutriments?.fat,
        saturatedFat = nutriments?.saturatedFat,
        fibre = nutriments?.fibre,
        salt = nutriments?.salt,
        isSaved = isSaved
    )
