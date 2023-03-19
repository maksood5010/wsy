package com.wsyapp.helper.sorthelper

import com.wsyapp.data.model.response.CartItemModel

class SecondComparator:Comparator<CartItemModel> {
    override fun compare(lhs: CartItemModel?, rhs: CartItemModel?): Int {
        if (lhs == null || rhs == null) return 0
        return if (lhs.id!! < rhs.id!!)
            1
        else -1
    }
}