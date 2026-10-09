package com.example.scanp.ui.ingredient

import androidx.compose.ui.graphics.Color
import com.example.scanp.util.ProductTypeEnum

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
 * Database of known unhealthy ingredients and additives, separated by product category.
 */
object IngredientDatabase {

    data class IngredientInfo(
        val keywords: List<String>,
        val riskLevel: RiskLevel,
        val category: String,
        val description: String,
        val potentialRisks: List<String>
    )

    // Food Ingredients
    private val foodIngredients = listOf(
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

    // Beauty Ingredients
    private val beautyIngredients = listOf(
        IngredientInfo(
            keywords = listOf("methylparaben", "propylparaben", "butylparaben", "ethylparaben", "isopropylparaben", "isobutylparaben", "paraben"),
            riskLevel = RiskLevel.HIGH,
            category = "Preservative (Paraben)",
            description = "Endocrine disruptor that mimics estrogen in the body.",
            potentialRisks = listOf("Hormone disruption", "Reproductive issues", "Reduced sperm count", "Breast cancer concern (studies ongoing)")
        ),
        IngredientInfo(
            keywords = listOf("dibutyl phthalate", "dbp", "diethylhexyl phthalate", "dehp", "diethyl phthalate", "dep", "butyl benzyl phthalate", "bbp", "phthalate"),
            riskLevel = RiskLevel.HIGH,
            category = "Plasticizer / Fragrance",
            description = "Linked to reproductive birth defects and DNA damage in sperm.",
            potentialRisks = listOf("Endocrine disruption", "Infertility", "Reproductive birth defects", "Asthma")
        ),
        IngredientInfo(
            keywords = listOf("formaldehyde", "dmdm hydantoin", "diazolidinyl urea", "imidazolidinyl urea", "quaternium-15", "bronopol", "5-bromo-5-nitro-1,3-dioxane"),
            riskLevel = RiskLevel.HIGH,
            category = "Preservative / Formaldehyde Releaser",
            description = "Known human carcinogen; common cause of allergic contact dermatitis.",
            potentialRisks = listOf("Carcinogenic", "Severe allergic reactions", "Skin sensitization", "Asthma")
        ),
        IngredientInfo(
            keywords = listOf("triclosan", "triclocarban"),
            riskLevel = RiskLevel.HIGH,
            category = "Antibacterial Agent",
            description = "Disrupts thyroid function and contributes to antibiotic resistance.",
            potentialRisks = listOf("Endocrine disruption", "Thyroid problems", "Antibiotic resistance", "Skin irritation")
        ),
        IngredientInfo(
            keywords = listOf("toluene", "methylbenzene"),
            riskLevel = RiskLevel.HIGH,
            category = "Solvent",
            description = "Neurotoxin and reproductive toxin found in some nail products.",
            potentialRisks = listOf("Neurotoxicity", "Reproductive damage", "Headaches", "Dizziness")
        ),
        IngredientInfo(
            keywords = listOf("lead acetate", "lead", "mercury", "thimerosal", "arsenic", "cadmium"),
            riskLevel = RiskLevel.HIGH,
            category = "Heavy Metal / Contaminant",
            description = "Heavy metals that accumulate in the body and cause systemic damage.",
            potentialRisks = listOf("Neurotoxicity", "Kidney damage", "Developmental damage", "Carcinogenic")
        ),
        IngredientInfo(
            keywords = listOf("butylphenyl methylpropional", "lilial", "lily aldehyde"),
            riskLevel = RiskLevel.HIGH,
            category = "Fragrance Allergen",
            description = "Classified as a reproductive toxicant (Repr. 1B); banned in EU since 2022.",
            potentialRisks = listOf("Reproductive toxicity", "Fertility issues", "Allergic contact dermatitis")
        ),
        IngredientInfo(
            keywords = listOf("hydroquinone"),
            riskLevel = RiskLevel.HIGH,
            category = "Skin Lightener",
            description = "Banned in cosmetics in many countries; linked to ochronosis and cancer concerns.",
            potentialRisks = listOf("Permanent skin discoloration (ochronosis)", "Skin irritation", "Potential carcinogen")
        ),
        IngredientInfo(
            keywords = listOf("coal tar", "coal tar solution"),
            riskLevel = RiskLevel.HIGH,
            category = "Colorant / Anti-dandruff",
            description = "Classified as a known human carcinogen in high concentrations.",
            potentialRisks = listOf("Carcinogenic", "Skin irritation", "Photosensitivity")
        ),
        IngredientInfo(
            keywords = listOf("sodium lauryl sulfate", "sls", "sodium laureth sulfate", "sles", "ammonium lauryl sulfate", "ammonium laureth sulfate"),
            riskLevel = RiskLevel.MEDIUM,
            category = "Surfactant",
            description = "Harsh cleanser that strips skin of natural oils and can worsen irritation.",
            potentialRisks = listOf("Skin irritation", "Eye irritation", "Dryness", "Can trigger rosacea and eczema")
        ),
        IngredientInfo(
            keywords = listOf("cyclopentasiloxane", "d5", "cyclohexasiloxane", "d6", "cyclotetrasiloxane", "d4"),
            riskLevel = RiskLevel.MEDIUM,
            category = "Silicone / Emollient",
            description = "Cyclic silicones with environmental persistence and potential endocrine concerns.",
            potentialRisks = listOf("Potential endocrine disruption", "Environmental persistence", "Bioaccumulation")
        ),
        IngredientInfo(
            keywords = listOf("methylisothiazolinone", "mit", "methylchloroisothiazolinone", "cmit"),
            riskLevel = RiskLevel.MEDIUM,
            category = "Preservative",
            description = "Potent skin sensitizer; restricted in leave-on cosmetics in the EU.",
            potentialRisks = listOf("Severe allergic contact dermatitis", "Skin sensitization", "Eczema flare-ups")
        ),
        IngredientInfo(
            keywords = listOf("oxybenzone", "benzophenone-3", "benzophenone-4", "homosalate", "octinoxate", "octocrylene"),
            riskLevel = RiskLevel.MEDIUM,
            category = "UV Filter",
            description = "Chemical UV filters with endocrine and environmental concerns; some banned in Hawaii.",
            potentialRisks = listOf("Endocrine disruption", "Skin irritation", "Coral reef damage", "Photoallergic reactions")
        ),
        IngredientInfo(
            keywords = listOf("fragrance", "parfum", "aroma"),
            riskLevel = RiskLevel.MEDIUM,
            category = "Fragrance",
            description = "Umbrella term that can hide dozens of undisclosed chemicals, including allergens.",
            potentialRisks = listOf("Allergic contact dermatitis", "Headaches", "Asthma trigger", "May hide phthalates")
        ),
        IngredientInfo(
            keywords = listOf("aluminum chlorohydrate", "aluminum zirconium", "aluminum chlorohydrate"),
            riskLevel = RiskLevel.MEDIUM,
            category = "Antiperspirant",
            description = "Blocks sweat ducts; long-debated links to breast cancer and Alzheimer's (not conclusively proven).",
            potentialRisks = listOf("Skin irritation", "Possible endocrine disruption (debated)", "Breast cancer concern (studies inconclusive)")
        ),
        IngredientInfo(
            keywords = listOf("talc"),
            riskLevel = RiskLevel.MEDIUM,
            category = "Powder / Filler",
            description = "Cosmetic-grade talc can be contaminated with asbestos; use of talc in the genital area is linked to ovarian cancer.",
            potentialRisks = listOf("Ovarian cancer concern (genital use)", "Respiratory irritation", "Potential asbestos contamination")
        ),
        IngredientInfo(
            keywords = listOf("petrolatum", "mineral oil", "paraffinum liquidum"),
            riskLevel = RiskLevel.MEDIUM,
            category = "Emollient",
            description = "Petroleum-derived occlusive; cosmetic-grade is refined, but can trap impurities and clog pores.",
            potentialRisks = listOf("Pore clogging (comedogenic)", "Potential contamination if unrefined", "Not ideal for acne-prone skin")
        ),
        IngredientInfo(
            keywords = listOf("polyethylene", "microbeads", "polypropylene", "nylon-12", "polymethyl methacrylate", "pmma"),
            riskLevel = RiskLevel.MEDIUM,
            category = "Microplastic / Exfoliant",
            description = "Plastic microbeads used as exfoliants; banned in many countries due to environmental impact.",
            potentialRisks = listOf("Environmental pollution", "Microplastic ingestion by wildlife", "Skin micro-abrasions")
        ),
        IngredientInfo(
            keywords = listOf("cocamidopropyl betaine"),
            riskLevel = RiskLevel.LOW,
            category = "Surfactant",
            description = "Mild surfactant; can cause allergic contact dermatitis in sensitive individuals.",
            potentialRisks = listOf("Allergic contact dermatitis (rare)", "Skin irritation in sensitive individuals")
        ),
        IngredientInfo(
            keywords = listOf("phenoxyethanol"),
            riskLevel = RiskLevel.LOW,
            category = "Preservative",
            description = "Common preservative considered safe up to 1%; restricted in Japan.",
            potentialRisks = listOf("Skin irritation in sensitive individuals", "Eye irritation")
        ),
        IngredientInfo(
            keywords = listOf("glycerin", "glycerol"),
            riskLevel = RiskLevel.LOW,
            category = "Humectant",
            description = "Common, safe moisturizing ingredient.",
            potentialRisks = listOf("Generally safe", "Rare sensitivity in very high concentrations")
        ),
        IngredientInfo(
            keywords = listOf("sodium hyaluronate", "hyaluronic acid"),
            riskLevel = RiskLevel.LOW,
            category = "Humectant",
            description = "Naturally occurring moisturizer, generally well tolerated.",
            potentialRisks = listOf("Generally safe", "Rare injection-site reactions (for fillers)")
        ),
        IngredientInfo(
            keywords = listOf("aloe barbadensis", "aloe vera"),
            riskLevel = RiskLevel.LOW,
            category = "Botanical",
            description = "Soothing botanical; rare allergy risk.",
            potentialRisks = listOf("Rare allergic contact dermatitis", "Generally safe")
        ),
        IngredientInfo(
            keywords = listOf("retinol", "retinyl palmitate", "retinoic acid", "tretinoin"),
            riskLevel = RiskLevel.LOW,
            category = "Active (Vitamin A)",
            description = "Effective anti-aging active; can cause irritation and photosensitivity.",
            potentialRisks = listOf("Skin irritation and peeling", "Sun sensitivity", "Avoid during pregnancy (high doses)")
        ),
        IngredientInfo(
            keywords = listOf("salicylic acid", "beta hydroxy acid", "bha"),
            riskLevel = RiskLevel.LOW,
            category = "Exfoliant",
            description = "Common acne treatment; can irritate dry or sensitive skin.",
            potentialRisks = listOf("Skin dryness", "Irritation", "Sun sensitivity")
        ),
        IngredientInfo(
            keywords = listOf("glycolic acid", "lactic acid", "alpha hydroxy acid", "aha"),
            riskLevel = RiskLevel.LOW,
            category = "Exfoliant",
            description = "Chemical exfoliants; effective but can cause irritation and sun sensitivity.",
            potentialRisks = listOf("Skin irritation", "Sun sensitivity", "Post-inflammatory hyperpigmentation")
        ),
        IngredientInfo(
            keywords = listOf("dimethicone", "cyclopentasiloxane", "dimethiconol"),
            riskLevel = RiskLevel.LOW,
            category = "Silicone",
            description = "Common skin-conditioning silicone; safe but can cause buildup on hair and clog pores for some.",
            potentialRisks = listOf("Pore clogging for some skin types", "Hair buildup with heavy use")
        ),
        IngredientInfo(
            keywords = listOf("butylene glycol", "propylene glycol", "pentylene glycol"),
            riskLevel = RiskLevel.LOW,
            category = "Humectant / Solvent",
            description = "Common solvents and humectants; generally safe, though propylene glycol can irritate sensitive skin.",
            potentialRisks = listOf("Skin irritation in sensitive individuals", "Generally safe")
        )
    )

    // Pet Food Ingredients
    private val petFoodIngredients = listOf(
        IngredientInfo(
            keywords = listOf("xylitol", "birch sugar", "birch bark extract", "wood sugar"),
            riskLevel = RiskLevel.HIGH,
            category = "Sweetener",
            description = "Causes rapid insulin release and severe hypoglycemia in dogs.",
            potentialRisks = listOf("Hypoglycemia (low blood sugar)", "Acute liver failure", "Seizures", "Death")
        ),
        IngredientInfo(
            keywords = listOf("grape", "raisin", "currant", "sultana"),
            riskLevel = RiskLevel.HIGH,
            category = "Fruit",
            description = "Can cause acute kidney failure in dogs. The toxic mechanism is unknown.",
            potentialRisks = listOf("Acute kidney failure", "Vomiting", "Lethargy", "Death")
        ),
        IngredientInfo(
            keywords = listOf("chocolate", "cocoa", "cacao", "caffeine", "theobromine"),
            riskLevel = RiskLevel.HIGH,
            category = "Methylxanthine",
            description = "Contains theobromine and caffeine, which are toxic to dogs and cats.",
            potentialRisks = listOf("Vomiting", "Diarrhea", "Hyperactivity", "Abnormal heart rhythm", "Seizures", "Death")
        ),
        IngredientInfo(
            keywords = listOf("onion", "garlic", "chive", "leek", "shallot", "allium"),
            riskLevel = RiskLevel.HIGH,
            category = "Allium Vegetable",
            description = "Damages red blood cells, leading to anemia. Toxic in all forms (raw, cooked, powdered).",
            potentialRisks = listOf("Anemia", "Weakness", "Pale gums", "Dark urine")
        ),
        IngredientInfo(
            keywords = listOf("ethoxyquin"),
            riskLevel = RiskLevel.HIGH,
            category = "Preservative",
            description = "Synthetic preservative linked to liver damage and suspected carcinogenicity.",
            potentialRisks = listOf("Liver damage", "Potential carcinogen", "Immune system issues")
        ),
        IngredientInfo(
            keywords = listOf("bha", "butylated hydroxyanisole", "bht", "butylated hydroxytoluene"),
            riskLevel = RiskLevel.HIGH,
            category = "Preservative",
            description = "Synthetic preservatives linked to liver damage and cancer in animal studies.",
            potentialRisks = listOf("Liver damage", "Metabolic stress", "Fetal abnormalities", "Cancer concerns")
        ),
        IngredientInfo(
            keywords = listOf("propylene glycol"),
            riskLevel = RiskLevel.HIGH,
            category = "Humectant",
            description = "Causes anemia in cats. Banned in cat food in the US; allowed in some dog foods.",
            potentialRisks = listOf("Anemia (especially in cats)", "Heinz body formation", "Lethargy", "Weakness")
        ),
        IngredientInfo(
            keywords = listOf("macadamia nut", "macadamia"),
            riskLevel = RiskLevel.HIGH,
            category = "Nut",
            description = "Toxic to dogs; causes weakness, tremors, and hyperthermia.",
            potentialRisks = listOf("Weakness", "Tremors", "Hyperthermia", "Hind limb paralysis")
        ),
        IngredientInfo(
            keywords = listOf("avocado", "persin"),
            riskLevel = RiskLevel.HIGH,
            category = "Fruit",
            description = "Contains persin, which is toxic to birds and rabbits; also risky for dogs and cats in large amounts.",
            potentialRisks = listOf("Vomiting", "Diarrhea", "Heart damage (in birds/rabbits)", "Respiratory distress")
        ),
        IngredientInfo(
            keywords = listOf("alcohol", "ethanol", "isopropanol"),
            riskLevel = RiskLevel.HIGH,
            category = "Alcohol",
            description = "Toxic to both dogs and cats; affects the central nervous system.",
            potentialRisks = listOf("CNS depression", "Hypoglycemia", "Vomiting", "Death")
        ),
        IngredientInfo(
            keywords = listOf("hop", "hops"),
            riskLevel = RiskLevel.HIGH,
            category = "Botanical",
            description = "Toxic to dogs; causes malignant hyperthermia.",
            potentialRisks = listOf("Malignant hyperthermia", "Rapid heart rate", "Seizures", "Death")
        ),
        IngredientInfo(
            keywords = listOf("moldy", "aflatoxin", "mycotoxin"),
            riskLevel = RiskLevel.HIGH,
            category = "Contaminant",
            description = "Moldy foods may contain mycotoxins that damage the liver.",
            potentialRisks = listOf("Liver damage", "Vomiting", "Lethargy", "Jaundice")
        ),
        IngredientInfo(
            keywords = listOf("chocolate", "coffee", "tea"),
            riskLevel = RiskLevel.HIGH,
            category = "Stimulant",
            description = "Contains caffeine or theobromine; toxic to pets.",
            potentialRisks = listOf("Hyperactivity", "Tremors", "Seizures", "Cardiac arrhythmia")
        ),
        IngredientInfo(
            keywords = listOf("corn", "wheat", "soy", "corn gluten meal", "wheat gluten", "soybean meal"),
            riskLevel = RiskLevel.LOW,
            category = "Filler",
            description = "Cheap fillers that are difficult for pets to digest and provide little nutritional value.",
            potentialRisks = listOf("Allergies", "Digestive issues", "Bloat", "Obesity")
        ),
        IngredientInfo(
            keywords = listOf("meat by-product", "poultry by-product", "by-product meal", "animal digest"),
            riskLevel = RiskLevel.LOW,
            category = "Protein Source",
            description = "Low-quality protein source; not necessarily toxic but a marker of low-quality nutrition.",
            potentialRisks = listOf("Low nutritional value", "Variable quality", "May contain undeclared tissues")
        ),
        IngredientInfo(
            keywords = listOf("artificial color", "red 40", "yellow 5", "yellow 6", "blue 2", "titanium dioxide"),
            riskLevel = RiskLevel.LOW,
            category = "Artificial Color",
            description = "Synthetic dyes with no nutritional value; used for human appeal.",
            potentialRisks = listOf("Behavioral issues (studies debated)", "Allergic reactions in sensitive pets", "No nutritional benefit")
        ),
        IngredientInfo(
            keywords = listOf("carrageenan", "e407"),
            riskLevel = RiskLevel.MEDIUM,
            category = "Thickener",
            description = "May cause digestive inflammation in pets.",
            potentialRisks = listOf("Digestive inflammation", "IBS-like symptoms", "Potential inflammatory response")
        ),
        IngredientInfo(
            keywords = listOf("caramel color", "e150"),
            riskLevel = RiskLevel.LOW,
            category = "Colorant",
            description = "Added for color; generally recognized as safe but no nutritional value.",
            potentialRisks = listOf("No nutritional value", "Rare sensitivity")
        )
    )

    /**
     * Returns the ingredient list for a given product type.
     */
    fun getIngredientsForType(productType: ProductTypeEnum): List<IngredientInfo> = when (productType) {
        ProductTypeEnum.FOOD -> foodIngredients
        ProductTypeEnum.BEAUTY -> beautyIngredients
        ProductTypeEnum.PET_FOOD -> petFoodIngredients
        ProductTypeEnum.UNKNOWN -> emptyList()
    }

    /**
     * Analyze ingredients string and return health analysis.
     *
     * This overload is category-aware — pick the database based on the
     * product type. Use this one from your UI/repository.
     */
    fun analyzeIngredients(ingredients: String?, productType: ProductTypeEnum): IngredientHealthResult {
        val database = getIngredientsForType(productType)
        return ingredientHealthResult(ingredients, database)
    }

    private fun ingredientHealthResult(
        ingredients: String?,
        database: List<IngredientInfo>
    ): IngredientHealthResult {
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
            .map { it.trim().lowercase().replace(Regex("\\s*\\([^)]*\\)"), "") }
            .filter { it.isNotBlank() }

        for (ingredient in ingredientList) {
            for (info in database) {
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
                    if (foundUnhealthy.none { it.name == unhealthy.name }) {
                        foundUnhealthy.add(unhealthy)
                        allRisks.addAll(info.potentialRisks)
                    }
                    break
                }
            }
        }

        val healthPercentage = calculateHealthPercentage(foundUnhealthy, ingredientList.size)

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
        for ((_, riskLevel) in unhealthy) {
            deduction += when (riskLevel) {
                RiskLevel.HIGH -> 20
                RiskLevel.MEDIUM -> 10
                RiskLevel.LOW -> 5
            }
        }

        val unhealthyRatio = (unhealthy.size.toDouble() / totalIngredients) * 30
        deduction += unhealthyRatio.toInt()

        return (100 - deduction).coerceIn(0, 100)
    }
}