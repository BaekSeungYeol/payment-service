package me.whitewin.paymentservice.payment.adapter.out.web.product.client

import me.whitewin.paymentservice.payment.domain.Product
import reactor.core.publisher.Flux

interface ProductClient {
    fun getProducts(cartId: Long, productIds: List<Long>): Flux<Product>
}