package me.whitewin.paymentservice.payment.application.port.out

import me.whitewin.paymentservice.payment.domain.Product
import reactor.core.publisher.Flux

interface LoadProductPort {
    fun getProducts(cartId: Long, productIds: List<Long>): Flux<Product>
}
