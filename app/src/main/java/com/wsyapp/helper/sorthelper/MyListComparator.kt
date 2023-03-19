package com.wsyapp.helper.sorthelper

import com.wsyapp.data.database.entity.Cart

class MyListComparator:Comparator<Cart> {
    override fun compare(lhs: Cart?, rhs: Cart?): Int {
        if (lhs == null || rhs == null) return 0
        return if (lhs.id_product!! < rhs.id_product!!)
            1
        else -1
    }
}