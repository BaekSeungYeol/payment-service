package me.whitewin.paymentservice.payment.adapter.out.web.product

import me.whitewin.paymentservice.common.WebAdapter
import me.whitewin.paymentservice.payment.adapter.out.web.product.client.ProductClient
import me.whitewin.paymentservice.payment.application.port.out.LoadProductPort
import me.whitewin.paymentservice.payment.domain.Product
import reactor.core.publisher.Flux

@WebAdapter
class ProductWebAdapter(
    private val productClient: ProductClient
): LoadProductPort {

    override fun getProducts(cartId: Long, productIds: List<Long>): Flux<Product> {
        return productClient.getProducts(cartId, productIds)
    }
}