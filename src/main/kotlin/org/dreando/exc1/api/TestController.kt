package org.dreando.exc1.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.time.Instant

@RestController
@RequestMapping("/test")
class TestController {

    data class Response(val timestamp: Instant)

    @GetMapping
    fun get(): Mono<Response> = Mono.just(Response(Instant.now()))
}