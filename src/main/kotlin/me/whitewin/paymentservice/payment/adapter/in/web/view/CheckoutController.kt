package me.whitewin.paymentservice.payment.adapter.`in`.web.view

import me.whitewin.paymentservice.common.IdempotencyCreator
import me.whitewin.paymentservice.common.WebAdapter
import me.whitewin.paymentservice.payment.adapter.`in`.web.request.CheckoutRequest
import me.whitewin.paymentservice.payment.application.port.`in`.CheckoutCommand
import me.whitewin.paymentservice.payment.application.port.`in`.CheckoutUseCase
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import reactor.core.publisher.Mono

@Controller
@WebAdapter
class CheckoutController(
    private val checkoutUseCase: CheckoutUseCase
) {

    @GetMapping("/")
    fun checkoutPage(request: CheckoutRequest, model: Model): Mono<String> {
        val command = CheckoutCommand(
            cartId = request.cartId,
            buyerId = request.buyerId,
            productIds = request.productIds,
            idempotencyKey = IdempotencyCreator.create(request.seed)
        )

        return checkoutUseCase.checkout(command)
            .map {
                model.addAttribute("orderId", it.orderId)
                model.addAttribute("orderName", it.orderName)
                model.addAttribute("amount", it.amount)
                "checkout"
            }
    }
}