package com.example.bdapplication.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.bdapplication.model.Item
import com.example.bdapplication.model.Menu

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "tes-item.db",  null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        android.util.Log.d("DB_INIT", "Creating tables...")
        val createItemTableQuery = """
            CREATE TABLE IF NOT EXISTS item(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                description TEXT NOT NULL
            )
        """.trimIndent()
        db?.execSQL(createItemTableQuery)

        val createMenuTableQuery = """
            CREATE TABLE IF NOT EXISTS menu(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                price REAL NOT NULL,
                ingredientid TEXT NOT NULL,
                quantity REAL NOT NULL,
                unit TEXT NOT NULL
            )
        """.trimIndent()
        db?.execSQL(createMenuTableQuery)

        val createMenuIngredientTableQuery = """
        CREATE TABLE IF NOT EXISTS menu_ingredient(
            menu_id INTEGER NOT NULL,
            ingredient_id INTEGER NOT NULL,
            quantity REAL NOT NULL,
            unit TEXT NOT NULL,
            PRIMARY KEY (menu_id, ingredient_id),
            FOREIGN KEY (menu_id) REFERENCES menu(id) ON DELETE CASCADE,
            FOREIGN KEY (ingredient_id) REFERENCES item(id) ON DELETE CASCADE
        )
    """.trimIndent()
        db?.execSQL(createMenuIngredientTableQuery)
    }

    /* ==== Item CRUD ==== */
    fun getItems() : List<Item> {
        val items = arrayListOf<Item>()
        val db = readableDatabase
        val query = "SELECT * FROM item ORDER BY id DESC"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        if (cursor.count > 0){
            do {
                val item = Item(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("description"))
                )
                items.add(item)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return items
    }

    fun insert(item: Item) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", item.name)
            put("description", item.description)
        }
        val result = db.insert("item", null, values)
        if (result == -1L) {
            android.util.Log.e("DB_ERROR", "Failed to insert item")
        } else {
            android.util.Log.d("DB_SUCCESS", "Item inserted successfully with ID: $result")
        }
        db.close()
    }


    fun updateItem(item: Item){
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", item.name)
            put("description", item.description)
        }
        db.update("item", values, "id = ?", arrayOf(item.id.toString()))
        db.close()
    }

    fun deleteItem(id: String) {
        val db = writableDatabase
        db.delete("item", "id = ?", arrayOf(id))
        db.close()
    }

    fun deleteAllItem() {
        val db = writableDatabase
        db.delete("item", null, null)
        db.close()
    }

    /* ====  Menu CRUD ==== */

    fun getMenus() : List<Menu> {
        val menus = arrayListOf<Menu>()
        val db = readableDatabase
        val query = "SELECT * FROM menu"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        if (cursor.count > 0){
            do {
                val menu = Menu(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                )
                menus.add(menu)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return menus
    }

    fun insertMenu(menu: Menu) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", menu.name)
            put("price", menu.price)
        }
        db.insert("menu", null, values)
        db.close()
    }

    fun updateMenu(menu: Menu) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", menu.name)
            put("price", menu.price)
        }
        db.update("menu", values, "id = ?", arrayOf(menu.id.toString()))
        db.close()
    }

    fun deleteMenu(id: String) {
        val db = writableDatabase
        db.delete("menu", "id = ?", arrayOf(id))
        db.close()
    }

    fun deleteAllMenus() {
        val db = writableDatabase
        db.delete("menu", null, null)
        db.close()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS item")
        db?.execSQL("DROP TABLE IF EXISTS menu")
        onCreate(db)
    }


    /* ====  Ingredients CRUD ==== */

    fun getIngredientsForMenu(menuId: Int): List<Item> {
        val ingredients = mutableListOf<Item>()
        val db = readableDatabase
        val query = """
        SELECT item.id, item.name, item.description 
        FROM item 
        INNER JOIN menu_ingredient ON item.id = menu_ingredient.ingredient_id
        WHERE menu_ingredient.menu_id = ?
    """
        val cursor = db.rawQuery(query, arrayOf(menuId.toString()))

        if (cursor.moveToFirst()) {
            do {
                val item = Item(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("description"))
                )
                ingredients.add(item)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return ingredients
    }


    fun addIngredientToMenu(menuId: Int, ingredientId: Int, quantity: Double, unit: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("menu_id", menuId)
            put("ingredient_id", ingredientId)
            put("quantity", quantity)
            put("unit", unit)
        }
        db.insert("menu_ingredient", null, values)
        db.close()
    }

    fun removeIngredientFromMenu(menuId: Int, ingredientId: Int) {
        val db = writableDatabase
        db.delete("menu_ingredient", "menu_id = ? AND ingredient_id = ?", arrayOf(menuId.toString(), ingredientId.toString()))
        db.close()
    }

    fun insertMenuWithIngredients(menu: Menu, ingredients: List<Pair<Int, Pair<Double, String>>>) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            val values = ContentValues().apply {
                put("name", menu.name)
                put("price", menu.price)
            }
            val menuId = db.insert("menu", null, values)

            if (menuId != -1L) {
                for (ingredient in ingredients) {
                    val ingredientValues = ContentValues().apply {
                        put("menu_id", menuId)
                        put("ingredient_id", ingredient.first)
                        put("quantity", ingredient.second.first)
                        put("unit", ingredient.second.second)
                    }
                    db.insert("menu_ingredient", null, ingredientValues)
                }
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
        db.close()
    }

    fun getMenusWithIngredients(): List<Menu> {
        val menus = arrayListOf<Menu>()
        val db = readableDatabase

        val menuQuery = "SELECT * FROM menu"
        val menuCursor = db.rawQuery(menuQuery, null)

        if (menuCursor.moveToFirst()) {
            do {
                val menuId = menuCursor.getInt(menuCursor.getColumnIndexOrThrow("id"))
                val name = menuCursor.getString(menuCursor.getColumnIndexOrThrow("name"))
                val price = menuCursor.getDouble(menuCursor.getColumnIndexOrThrow("price"))

                val ingredientsQuery = """
                SELECT item.id, item.name, menu_ingredient.quantity, menu_ingredient.unit
                FROM menu_ingredient
                INNER JOIN item ON menu_ingredient.ingredient_id = item.id
                WHERE menu_ingredient.menu_id = ?
            """
                val ingredientCursor = db.rawQuery(ingredientsQuery, arrayOf(menuId.toString()))

                val ingredients = arrayListOf<String>()
                if (ingredientCursor.moveToFirst()) {
                    do {
                        val ingredientName = ingredientCursor.getString(ingredientCursor.getColumnIndexOrThrow("name"))
                        val quantity = ingredientCursor.getDouble(ingredientCursor.getColumnIndexOrThrow("quantity"))
                        val unit = ingredientCursor.getString(ingredientCursor.getColumnIndexOrThrow("unit"))
                        ingredients.add("$ingredientName ($quantity $unit)")
                    } while (ingredientCursor.moveToNext())
                }
                ingredientCursor.close()

                val menu = Menu(menuId, name, price)
                menus.add(menu)
            } while (menuCursor.moveToNext())
        }

        menuCursor.close()
        db.close()
        return menus
    }


}
