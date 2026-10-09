package com.example.scanp.data.mapper

import com.example.scanp.data.domain.ProductDomain
import com.example.scanp.data.local.entities.ProductEntity
import com.example.scanp.data.remote.dto.ProductDto
import com.example.scanp.util.ProductTypeEnum

/**
 * Mapper utilities to convert between DTO, Entity, and Domain models.
 * The `isSaved` flag indicates whether the product already exists in the local database.
 */

fun ProductEntity.toProductDomain(isSaved: Boolean = false): ProductDomain = ProductDomain(
    barcode = barcode,
    name = name,
    productType = ProductTypeEnum.productTypeFromApi(productType),
    ingredients = ingredients,
    energyKcal = energyKcal,
    proteins = proteins,
    carbs = carbs,
    sugars = sugars,
    fat = fat,
    saturatedFat = saturatedFat,
    fibre = fibre,
    salt = salt,
    isSaved = isSaved,
    imageUrl = imageUrl
)

fun ProductDomain.toProductEntity(): ProductEntity = ProductEntity(
    barcode = barcode,
    name = name,
    productType = productType?.name,
    ingredients = ingredients,
    energyKcal = energyKcal,
    proteins = proteins,
    carbs = carbs,
    sugars = sugars,
    fat = fat,
    saturatedFat = saturatedFat,
    fibre = fibre,
    salt = salt,
    imageUrl = imageUrl
)

fun ProductDto.toProductDomain(barcode: String, isSaved: Boolean = false): ProductDomain {

    val productType = ProductTypeEnum.productTypeFromApi(productType)

    return ProductDomain(
        barcode = barcode,
        name = productName,
        productType = ProductTypeEnum.productTypeFromApi(productType.name),
        ingredients = ingredientsText,
        energyKcal = nutriments?.energyKcal,
        proteins = nutriments?.proteins,
        carbs = nutriments?.carbohydrates,
        sugars = nutriments?.sugars,
        fat = nutriments?.fat,
        saturatedFat = nutriments?.saturatedFat,
        fibre = nutriments?.fibre,
        salt = nutriments?.salt,
        imageUrl = imageSmallUrl,
        isSaved = isSaved
    )
}
