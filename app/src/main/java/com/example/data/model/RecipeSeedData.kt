package com.example.data.model

object RecipeSeedData {
    val featuredRecipes: List<Recipe> = listOf(
        Recipe(
            id = 1001,
            name = "Pasta con tomate y albahaca",
            ingredients = listOf(
                "200 g de pasta rigatoni",
                "300 g de tomates cherry",
                "1 diente de ajo picado",
                "1 cda. de aceite de oliva",
                "20 g de queso parmesano",
                "6 hojas de albahaca fresca",
                "Al gusto sal y pimienta negra"
            ),
            instructions = listOf(
                "Cuece la pasta: Hierve agua con sal y cocina los rigatoni según el envase, unos 12 min, hasta que estén al dente. Reserva 100 ml del agua y escurre.",
                "Prepara la base: Mientras se cuece la pasta, corta los tomates por la mitad. Calienta el aceite en una sartén y sofríe el ajo 1 min a fuego medio, sin dorarlo.",
                "Cocina la salsa: Añade los tomates, sal y pimienta. Cocina 8-10 min, removiendo y aplastando algunos tomates para que suelten su jugo.",
                "Mezcla y sirve: Incorpora la pasta y 2–3 cucharadas del agua reservada. Mezcla 1 min; añade más agua si hace falta. Apaga el fuego y termina con la albahaca y el parmesano rallado."
            ),
            prepTimeMinutes = 10,
            cookTimeMinutes = 15,
            servings = 2,
            difficulty = "Fácil",
            cuisine = "Italiana",
            caloriesPerServing = 480,
            tags = listOf("Rápidas", "Vegetarianas", "Pasta", "Italiana"),
            image = "https://images.unsplash.com/photo-1621996346565-e3d5d6281691?auto=format&fit=crop&w=800&q=80",
            rating = 4.8,
            reviewCount = 128,
            mealType = listOf("Almuerzo", "Cena")
        ),
        Recipe(
            id = 1002,
            name = "Ensalada mediterránea",
            ingredients = listOf(
                "150 g de garbanzos cocidos",
                "1 pepino en cubos",
                "200 g de tomates cherry variados",
                "60 g de queso feta desmenuzado",
                "30 g de aceitunas kalamata",
                "2 cdas. de aceite de oliva virgen extra",
                "1 cda. de zumo de limón fresco",
                "1 pizca de orégano seco y sal"
            ),
            instructions = listOf(
                "Lava y corta: Lava bien los tomates cherry y córtalos por la mitad. Pela parcialmente el pepino y córtalo en cubos medianos.",
                "Mezcla los ingredientes: En un bol grande añade los garbanzos escurridos, los tomates, el pepino y las aceitunas kalamata.",
                "Aliña: En un vaso mezcla el aceite de oliva, zumo de limón, orégano y una pizca de sal. Vierte sobre la ensalada.",
                "Emplata: Decora con el queso feta desmenuzado y un toque extra de orégano antes de servir fría."
            ),
            prepTimeMinutes = 15,
            cookTimeMinutes = 0,
            servings = 2,
            difficulty = "Fácil",
            cuisine = "Mediterránea",
            caloriesPerServing = 320,
            tags = listOf("Rápidas", "Vegetarianas", "Ensaladas", "Saludable"),
            image = "https://images.unsplash.com/photo-1540420773420-3366772f4999?auto=format&fit=crop&w=800&q=80",
            rating = 4.7,
            reviewCount = 94,
            mealType = listOf("Almuerzo", "Cena")
        ),
        Recipe(
            id = 1003,
            name = "Salmón al limón con brócoli",
            ingredients = listOf(
                "2 lomos de salmón fresco (150 g c/u)",
                "250 g de ramilletes de brócoli",
                "1 limón cortado en rodajas y zumo",
                "2 cdas. de aceite de oliva",
                "2 dientes de ajo laminados",
                "Al gusto sal marina y pimienta negra",
                "1 ramita de eneldo fresco"
            ),
            instructions = listOf(
                "Prepara el brócoli: Cocina el brócoli al vapor durante 4-5 minutos hasta que esté tierno pero crujiente.",
                "Sella el salmón: En una sartén con aceite caliente a fuego medio-alto, coloca el salmón con la piel hacia abajo durante 4 minutos.",
                "Dorar y aromatizar: Da la vuelta al salmón, añade las rodajas de limón y los ajos laminados. Cocina 3 minutos más bañando con los jugos.",
                "Servir: Sirve el salmón junto con el brócoli, añade unas gotas de zumo de limón y decora con eneldo fresco."
            ),
            prepTimeMinutes = 10,
            cookTimeMinutes = 20,
            servings = 2,
            difficulty = "Media",
            cuisine = "Saludable",
            caloriesPerServing = 510,
            tags = listOf("Cena", "Pescados", "Saludable"),
            image = "https://images.unsplash.com/photo-1467003909585-2f8a72700288?auto=format&fit=crop&w=800&q=80",
            rating = 4.9,
            reviewCount = 142,
            mealType = listOf("Cena", "Almuerzo")
        ),
        Recipe(
            id = 1004,
            name = "Tostadas de aguacate y huevo",
            ingredients = listOf(
                "2 rebanadas de pan de masa madre",
                "1 aguacate maduro",
                "2 huevos de campo",
                "1 cda. de semillas de chía o sésamo",
                "1 chorrito de aceite de oliva",
                "Copos de chile al gusto",
                "Sal y pimienta recién molida"
            ),
            instructions = listOf(
                "Tuesta el pan: Tuesta las rebanadas de pan de masa madre hasta que queden doradas y crujientes.",
                "Prepara el aguacate: Tritura el aguacate con un tenedor o córtalo en láminas finas. Añade unas gotas de limón, sal y pimienta.",
                "Pocha los huevos: Hierve agua con una gota de vinagre, crea un remolino suave y cocina los huevos durante 3 minutos.",
                "Monta y decora: Unta o coloca el aguacate sobre el pan tostado, corona con el huevo pochado y espolvorea semillas y copos de chile."
            ),
            prepTimeMinutes = 10,
            cookTimeMinutes = 5,
            servings = 1,
            difficulty = "Fácil",
            cuisine = "Desayuno",
            caloriesPerServing = 390,
            tags = listOf("Rápidas", "Vegetarianas", "Desayuno", "Fácil"),
            image = "https://images.unsplash.com/photo-1525351484163-7529414344d8?auto=format&fit=crop&w=800&q=80",
            rating = 4.6,
            reviewCount = 88,
            mealType = listOf("Desayuno", "Snack")
        )
    )

    fun translateApiRecipe(apiRecipe: Recipe): Recipe {
        val spanishTitle = when (apiRecipe.name.trim()) {
            "Classic Margherita Pizza" -> "Pizza Margherita Clásica"
            "Vegetarian Stir-Fry" -> "Salteado de Verduras"
            "Chocolate Chip Cookies" -> "Galletas con Chispas de Chocolate"
            "Chicken Alfredo Pasta" -> "Pasta Alfredo con Pollo"
            "Mango Salsa Chicken" -> "Pollo con Salsa de Mango"
            "Berry Banana Smoothie" -> "Batido de Frutos Rojos y Plátano"
            "Spicy Beef Tacos" -> "Tacos de Ternera Picante"
            "Creamy Tomato Soup" -> "Sopa Cremosa de Tomate"
            "Mediterranean Chickpea Salad" -> "Ensalada Mediterránea de Garbanzos"
            "Caprese Salad" -> "Ensalada Caprese"
            "Guacamole" -> "Guacamole Casero"
            "Chicken Caesar Salad" -> "Ensalada César con Pollo"
            "Avocado Toast" -> "Tostadas de Aguacate"
            "Vegetable Lasagna" -> "Lasaña Vegetal"
            "Quinoa Salad" -> "Ensalada de Quinoa"
            else -> apiRecipe.name
        }

        val spanishCuisine = when (apiRecipe.cuisine.lowercase()) {
            "italian" -> "Italiana"
            "mexican" -> "Mexicana"
            "asian" -> "Asiática"
            "mediterranean" -> "Mediterránea"
            "american" -> "Americana"
            "greek" -> "Griega"
            "indian" -> "India"
            "japanese" -> "Japonesa"
            "thai" -> "Tailandesa"
            else -> apiRecipe.cuisine
        }

        val spanishDifficulty = when (apiRecipe.difficulty.lowercase()) {
            "easy" -> "Fácil"
            "medium" -> "Media"
            "hard" -> "Difícil"
            else -> apiRecipe.difficulty
        }

        val spanishTags = apiRecipe.tags.map { tag ->
            when (tag.lowercase()) {
                "pizza" -> "Pizza"
                "italian" -> "Italiana"
                "vegetarian" -> "Vegetarianas"
                "quick" -> "Rápidas"
                "easy" -> "Fácil"
                "pasta" -> "Pasta"
                "salad" -> "Ensaladas"
                "healthy" -> "Saludable"
                "dinner" -> "Cena"
                "lunch" -> "Almuerzo"
                "dessert" -> "Postres"
                "breakfast" -> "Desayuno"
                "cookies" -> "Galletas"
                "mexican" -> "Mexicana"
                else -> tag.replaceFirstChar { it.uppercase() }
            }
        }.toMutableList()

        if (apiRecipe.prepTimeMinutes + apiRecipe.cookTimeMinutes <= 30 && !spanishTags.contains("Rápidas")) {
            spanishTags.add("Rápidas")
        }

        return apiRecipe.copy(
            name = spanishTitle,
            cuisine = spanishCuisine,
            difficulty = spanishDifficulty,
            tags = spanishTags
        )
    }
}
