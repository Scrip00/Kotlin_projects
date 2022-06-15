package com.scrip0.mvvmshoppinglist.data.repositories

import com.scrip0.mvvmshoppinglist.data.db.ShoppingDatabase
import com.scrip0.mvvmshoppinglist.data.db.entities.ShoppingItem

class ShoppingRepository(
    private val db: ShoppingDatabase
) {
    suspend fun upsert(item: ShoppingItem) = db.getShoppingDao().upsert(item)

    suspend fun delete(item: ShoppingItem) = db.getShoppingDao().delete(item)

    fun getAllShoppingItems() = db.getShoppingDao().getAllShoppingItems()
}