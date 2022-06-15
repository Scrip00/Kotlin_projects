package com.scrip0.mvvmshoppinglist.ui.shoppinglist

import com.scrip0.mvvmshoppinglist.data.db.entities.ShoppingItem

interface AddDialogListener {
    fun onAddButtonClicked(item: ShoppingItem)
}