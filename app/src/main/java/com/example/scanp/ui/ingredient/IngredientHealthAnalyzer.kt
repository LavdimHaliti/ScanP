package com.example.scanp.ui.ingredient

import androidx.compose.ui.graphics.Color

/**
 * Risk level of an ingredient
 */
enum class RiskLevel(val displayName: String, val color: Color) {
    HIGH("High Risk", Color(0xFFD32F2F)),
    MEDIUM("Medium Risk", Color(0xFFFF9800)),
    LOW("Low Risk", Color(0xFFFBC02D))
}

/**
 * Information about an unhealthy ingredient found in the product
 */
data class UnhealthyIngredient(
    val name: String,
    val riskLevel: RiskLevel,
    val category: String,
    val description: String,
    val potentialRisks: List<String>
)

/**
 * Result of ingredient health analysis
 */
data class IngredientHealthResult(
    val healthPercentage: Int,
    val healthRating: String,
    val unhealthyIngredients: List<UnhealthyIngredient>,
    val allRisks: List<String>
)

/**
 * Database of known unhealthy ingredients and additives
 */
object IngredientDatabase {
    data class IngredientInfo(
        val keywords: List<String>,
        val riskLevel: RiskLevel,
        val category: String,
        val description: String,
        val potentialRisks: List<String>
    )

    private val unhealthyIngredients = listOf(
        IngredientInfo(
            keywords = listOf("high fructose corn syrup", "hfcs", "glucose-fructose syrup"),
            riskLevel = RiskLevel.HIGH,
            category = "Added Sugar",
            description = "Highly processed sweetener linked to obesity and diabetes",
            potentialRisks = listOf("Obesity", "Type 2 Diabetes", "Fatty Liver Disease", "Increased triglyceride levels")
        ),
        IngredientInfo(
            keywords = listOf("aspartame", "e951"),
            riskLevel = RiskLevel.HIGH,
            category = "Artificial Sweetener",
            description = "Artificial sweetener that may cause issues for people with phenylketonuria",
            potentialRisks = listOf("Headaches", "Digestive issues", "May affect gut bacteria")
        ),
        IngredientInfo(
            keywords = listOf("sucralose", "splenda", "e955"),
            riskLevel = RiskLevel.HIGH,
            category = "Artificial Sweetener",
            description = "Artificial sweetener that may alter glucose metabolism",
            potentialRisks = listOf("Altered insulin response", "Gut bacteria disruption", "May increase blood sugar")
        ),
        IngredientInfo(
            keywords = listOf("monosodium glutamate", "msg", "e621"),
            riskLevel = RiskLevel.HIGH,
            category = "Flavor Enhancer",
            description = "Flavor enhancer that may cause sensitivity reactions in some people",
            potentialRisks = listOf("Headaches", "Flushing", "Sweating", "Chest pain (in sensitive individuals)")
        ),
        IngredientInfo(
            keywords = listOf("bha", "butylated hydroxyanisole", "e320"),
            riskLevel = RiskLevel.HIGH,
            category = "Preservative",
            description = "Preservative classified as possibly carcinogenic",
            potentialRisks = listOf("Potential carcinogen", "Hormone disruption", "Liver damage with long-term exposure")
        ),
        IngredientInfo(
            keywords = listOf("bht", "butylated hydroxytoluene", "e321"),
            riskLevel = RiskLevel.HIGH,
            category = "Preservative",
            description = "Preservative with potential health concerns",
            potentialRisks = listOf("Potential carcinogen", "Hormone disruption", "May affect liver and kidneys")
        ),
        IngredientInfo(
            keywords = listOf("sodium nitrite", "sodium nitrate", "e250", "e251"),
            riskLevel = RiskLevel.HIGH,
            category = "Preservative",
            description = "Used in processed meats, can form carcinogenic compounds",
            potentialRisks = listOf("Formation of nitrosamines (carcinogenic)", "Increased cancer risk", "May affect blood oxygen levels")
        ),
        IngredientInfo(
            keywords = listOf("partially hydrogenated", "trans fat", "hydrogenated oil"),
            riskLevel = RiskLevel.HIGH,
            category = "Trans Fat",
            description = "Artificial trans fats that increase heart disease risk",
            potentialRisks = listOf("Heart disease", "Increased LDL (bad) cholesterol", "Decreased HDL (good) cholesterol", "Inflammation")
        ),
        IngredientInfo(
            keywords = listOf("red 40", "allura red", "e129", "yellow 5", "tartrazine", "e102", "yellow 6", "sunset yellow", "e110", "blue 1", "brilliant blue", "e133", "red 3", "erythrosine", "e127"),
            riskLevel = RiskLevel.HIGH,
            category = "Artificial Color",
            description = "Synthetic dyes linked to behavioral issues in children",
            potentialRisks = listOf("Hyperactivity in children", "Behavioral issues", "Allergic reactions", "Potential carcinogen (Red 3)")
        ),
        IngredientInfo(
            keywords = listOf("saccharin", "e954"),
            riskLevel = RiskLevel.HIGH,
            category = "Artificial Sweetener",
            description = "Artificial sweetener linked to bladder cancer in animal studies",
            potentialRisks = listOf("Potential carcinogen", "Allergic reactions in sensitive individuals")
        ),
        IngredientInfo(
            keywords = listOf("cyclamate", "e952"),
            riskLevel = RiskLevel.HIGH,
            category = "Artificial Sweetener",
            description = "Artificial sweetener banned in the US due to carcinogenicity concerns",
            potentialRisks = listOf("Potential carcinogen", "May cause bladder tumors (animal studies)")
        ),
        IngredientInfo(
            keywords = listOf("acesulfame potassium", "ace k", "e950"),
            riskLevel = RiskLevel.HIGH,
            category = "Artificial Sweetener",
            description = "Artificial sweetener that may disrupt gut microbiome and insulin response",
            potentialRisks = listOf("Gut bacteria disruption", "Altered insulin response", "Potential carcinogen in long-term studies")
        ),
        IngredientInfo(
            keywords = listOf("potassium bromate", "e924a"),
            riskLevel = RiskLevel.HIGH,
            category = "Dough Conditioner",
            description = "Flour improver classified as possible human carcinogen",
            potentialRisks = listOf("Potential carcinogen", "Kidney and thyroid damage", "Banned in EU, Canada, Brazil")
        ),
        IngredientInfo(
            keywords = listOf("azodicarbonamide", "e927a", "ada"),
            riskLevel = RiskLevel.HIGH,
            category = "Dough Conditioner",
            description = "Forms semicarbazide, a possible carcinogen",
            potentialRisks = listOf("Potential carcinogen", "Respiratory issues (in manufacturing)", "Banned in EU, Australia")
        ),
        IngredientInfo(
            keywords = listOf("titanium dioxide", "e171"),
            riskLevel = RiskLevel.HIGH,
            category = "Whitening Agent",
            description = "Nanoparticles may be genotoxic; banned for food in EU",
            potentialRisks = listOf("Possible genotoxicity", "DNA damage (animal studies)", "Inflammatory response")
        ),
        IngredientInfo(
            keywords = listOf("propylparaben", "e216"),
            riskLevel = RiskLevel.HIGH,
            category = "Preservative",
            description = "Suspected endocrine disruptor",
            potentialRisks = listOf("Hormone disruption", "Reduced sperm count", "Breast cancer concern (studies ongoing)")
        ),
        IngredientInfo(
            keywords = listOf("sodium metabisulfite", "e223", "sulfite", "bisulfite", "potassium metabisulfite", "e224"),
            riskLevel = RiskLevel.HIGH,
            category = "Preservative",
            description = "Can trigger severe allergic and asthmatic reactions",
            potentialRisks = listOf("Severe allergic reactions", "Asthma attacks in sensitive individuals", "Respiratory distress")
        ),
        IngredientInfo(
            keywords = listOf("bov", "brominated vegetable oil"),
            riskLevel = RiskLevel.HIGH,
            category = "Emulsifier",
            description = "Bromine accumulates in body; disrupts thyroid function",
            potentialRisks = listOf("Thyroid disruption", "Accumulation in body fat", "Neurological issues (high doses)", "Banned in EU, Japan")
        ),

        IngredientInfo(
            keywords = listOf("sodium benzoate", "e211", "potassium benzoate", "e212"),
            riskLevel = RiskLevel.MEDIUM,
            category = "Preservative",
            description = "Common preservative that can form benzene when combined with vitamin C",
            potentialRisks = listOf("Benzene formation (with vitamin C)", "May cause hyperactivity", "Allergic reactions")
        ),
        IngredientInfo(
            keywords = listOf("potassium sorbate", "e202", "sodium sorbate", "calcium sorbate"),
            riskLevel = RiskLevel.MEDIUM,
            category = "Preservative",
            description = "Preservative that may cause skin allergies in some people",
            potentialRisks = listOf("Skin allergies", "May be genotoxic at high doses")
        ),
        IngredientInfo(
            keywords = listOf("palm oil", "palm kernel oil"),
            riskLevel = RiskLevel.MEDIUM,
            category = "Oil",
            description = "High in saturated fats, environmental concerns",
            potentialRisks = listOf("Increased cholesterol", "Increased risk of heart disease", "Environmental impact")
        ),
        IngredientInfo(
            keywords = listOf("corn syrup", "glucose syrup", "malt syrup"),
            riskLevel = RiskLevel.MEDIUM,
            category = "Added Sugar",
            description = "Processed sweeteners that spike blood sugar",
            potentialRisks = listOf("Blood sugar spikes", "Weight gain", "Increased risk of diabetes")
        ),
        IngredientInfo(
            keywords = listOf("artificial flavor", "artificial flavour", "nature identical flavoring"),
            riskLevel = RiskLevel.MEDIUM,
            category = "Artificial Additive",
            description = "Chemically created flavors with unknown long-term effects",
            potentialRisks = listOf("Allergic reactions", "Unknown chemical exposure", "May contain solvents or preservatives")
        ),
        IngredientInfo(
            keywords = listOf("carrageenan", "e407"),
            riskLevel = RiskLevel.MEDIUM,
            category = "Thickener",
            description = "Seaweed-derived thickener that may cause digestive issues",
            potentialRisks = listOf("Digestive inflammation", "May worsen IBS symptoms", "Potential inflammatory response")
        ),
        IngredientInfo(
            keywords = listOf("caramel color", "e150c", "e150d"),
            riskLevel = RiskLevel.MEDIUM,
            category = "Artificial Color",
            description = "Contains 4-methylimidazole (4-MEI), a possible carcinogen",
            potentialRisks = listOf("Potential carcinogen (4-MEI)", "Found in colas and dark sauces")
        ),
        IngredientInfo(
            keywords = listOf("propylene glycol", "e1520"),
            riskLevel = RiskLevel.MEDIUM,
            category = "Humectant",
            description = "May cause skin, liver, or kidney issues in high doses",
            potentialRisks = listOf("Skin irritation", "Potential liver and kidney damage (high exposure)")
        ),
        IngredientInfo(
            keywords = listOf("polysorbate 80", "e433"),
            riskLevel = RiskLevel.MEDIUM,
            category = "Emulsifier",
            description = "Linked to gut inflammation and metabolic syndrome in animal studies",
            potentialRisks = listOf("Gut inflammation", "Increased intestinal permeability", "Metabolic syndrome")
        ),
        IngredientInfo(
            keywords = listOf("calcium propionate", "e282"),
            riskLevel = RiskLevel.MEDIUM,
            category = "Preservative",
            description = "Common in bread; may cause migraines and behavioral issues",
            potentialRisks = listOf("Migraines", "Behavioral issues in children", "Irritability")
        ),
        IngredientInfo(
            keywords = listOf("soybean oil", "canola oil", "cottonseed oil", "vegetable oil"),
            riskLevel = RiskLevel.MEDIUM,
            category = "Oil",
            description = "Highly processed seed oils high in omega-6 (pro-inflammatory)",
            potentialRisks = listOf("Promotes inflammation when overconsumed", "High omega-6 to omega-3 ratio")
        ),
        IngredientInfo(
            keywords = listOf("maltodextrin"),
            riskLevel = RiskLevel.MEDIUM,
            category = "Added Sugar",
            description = "Highly processed carbohydrate that spikes blood sugar faster than sugar",
            potentialRisks = listOf("Rapid blood sugar spikes", "May alter gut bacteria", "Highly processed")
        ),
        IngredientInfo(
            keywords = listOf("invert sugar", "invert syrup"),
            riskLevel = RiskLevel.MEDIUM,
            category = "Added Sugar",
            description = "Similar to HFCS; contains high levels of free fructose",
            potentialRisks = listOf("Blood sugar spikes", "Fructose metabolism burden on liver")
        ),
        IngredientInfo(
            keywords = listOf("sucrose", "table sugar", "cane sugar", "refined sugar"),
            riskLevel = RiskLevel.LOW,
            category = "Added Sugar",
            description = "Common sugar that should be consumed in moderation",
            potentialRisks = listOf("Tooth decay", "Weight gain if consumed in excess", "Blood sugar spikes")
        ),
        IngredientInfo(
            keywords = listOf("fructose", "fruit sugar"),
            riskLevel = RiskLevel.LOW,
            category = "Added Sugar",
            description = "Natural sugar that can be problematic in large amounts",
            potentialRisks = listOf("Liver stress in high amounts", "May contribute to insulin resistance")
        ),
        IngredientInfo(
            keywords = listOf("saturated fat", "animal fat", "lard", "butter"),
            riskLevel = RiskLevel.LOW,
            category = "Fat",
            description = "Should be limited in a heart-healthy diet",
            potentialRisks = listOf("Increased cholesterol levels", "Increased heart disease risk if consumed in excess")
        ),
        IngredientInfo(
            keywords = listOf("salt", "sodium", "sea salt", "himalayan salt"),
            riskLevel = RiskLevel.LOW,
            category = "Sodium",
            description = "Essential mineral but excessive intake is harmful",
            potentialRisks = listOf("High blood pressure", "Increased risk of stroke", "Kidney strain", "Water retention")
        ),
        IngredientInfo(
            keywords = listOf("soy lecithin", "e322", "sunflower lecithin"),
            riskLevel = RiskLevel.LOW,
            category = "Emulsifier",
            description = "Common emulsifier generally recognized as safe",
            potentialRisks = listOf("Possible soy allergen", "Mild digestive issues in sensitive individuals")
        ),
        IngredientInfo(
            keywords = listOf("xanthan gum", "e415", "guar gum", "e412"),
            riskLevel = RiskLevel.LOW,
            category = "Thickener",
            description = "Common thickeners generally safe for most people",
            potentialRisks = listOf("Gas and bloating in some people", "Digestive discomfort in large amounts")
        ),
        // New LOW RISK additions
        IngredientInfo(
            keywords = listOf("natural flavors", "natural flavour"),
            riskLevel = RiskLevel.LOW,
            category = "Flavoring",
            description = "Vague term; may include solvents and preservatives, but generally recognized as safe",
            potentialRisks = listOf("May hide less desirable ingredients", "Potential allergens (depending on source)")
        ),
        IngredientInfo(
            keywords = listOf("citric acid"),
            riskLevel = RiskLevel.LOW,
            category = "Acidity Regulator",
            description = "Commonly derived from black mold (Aspergillus niger), safe for most",
            potentialRisks = listOf("Potential allergen for mold-sensitive individuals", "Can erode tooth enamel in excess")
        ),
        IngredientInfo(
            keywords = listOf("yeast extract", "autolyzed yeast extract"),
            riskLevel = RiskLevel.LOW,
            category = "Flavor Enhancer",
            description = "Natural source of glutamates (similar to MSG), safe for most",
            potentialRisks = listOf("May trigger MSG sensitivity in some people", "Headaches in sensitive individuals")
        )
    )

    /**
     * Analyze ingredients string and return health analysis
     */
    fun analyze(ingredients: String?): IngredientHealthResult {
        if (ingredients.isNullOrBlank()) {
            return IngredientHealthResult(
                healthPercentage = 100,
                healthRating = "Unknown",
                unhealthyIngredients = emptyList(),
                allRisks = emptyList()
            )
        }

        val foundUnhealthy = mutableListOf<UnhealthyIngredient>()
        val allRisks = mutableSetOf<String>()

        // Parse ingredients - split by common delimiters
        val ingredientList = ingredients.split(",", ";", "•", "\n")
            .map { it.trim().lowercase().replace(Regex("\\s*\\([^)]*\\)"), "") } // Remove parentheses content
            .filter { it.isNotBlank() }

        // Check each ingredient against our database
        for (ingredient in ingredientList) {
            for (info in unhealthyIngredients) {
                val matched = info.keywords.any { keyword ->
                    ingredient.contains(keyword, ignoreCase = true)
                }
                if (matched) {
                    val unhealthy = UnhealthyIngredient(
                        name = info.keywords.first().replaceFirstChar { it.uppercase() },
                        riskLevel = info.riskLevel,
                        category = info.category,
                        description = info.description,
                        potentialRisks = info.potentialRisks
                    )
                    // Avoid duplicates
                    if (foundUnhealthy.none { it.name == unhealthy.name }) {
                        foundUnhealthy.add(unhealthy)
                        allRisks.addAll(info.potentialRisks)
                    }
                    break
                }
            }
        }

        // Calculate health percentage
        val healthPercentage = calculateHealthPercentage(foundUnhealthy, ingredientList.size)

        // Determine health rating
        val healthRating = when {
            healthPercentage >= 80 -> "Good"
            healthPercentage >= 60 -> "Moderate"
            healthPercentage >= 40 -> "Poor"
            else -> "Very Poor"
        }

        return IngredientHealthResult(
            healthPercentage = healthPercentage,
            healthRating = healthRating,
            unhealthyIngredients = foundUnhealthy,
            allRisks = allRisks.toList()
        )
    }

    private fun calculateHealthPercentage(unhealthy: List<UnhealthyIngredient>, totalIngredients: Int): Int {
        if (totalIngredients == 0) return 100

        var deduction = 0
        for (ingredient in unhealthy) {
            deduction += when (ingredient.riskLevel) {
                RiskLevel.HIGH -> 20
                RiskLevel.MEDIUM -> 10
                RiskLevel.LOW -> 5
            }
        }

        // Also factor in the ratio of unhealthy to total ingredients
        val unhealthyRatio = (unhealthy.size.toDouble() / totalIngredients) * 30
        deduction += unhealthyRatio.toInt()

        return (100 - deduction).coerceIn(0, 100)
    }
}
