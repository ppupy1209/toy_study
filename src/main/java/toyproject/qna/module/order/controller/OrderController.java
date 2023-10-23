package toyproject.qna.module.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toyproject.qna.global.utils.UriCreator;
import toyproject.qna.module.order.dto.OrderPostDto;
import toyproject.qna.module.order.service.OrderService;

import javax.validation.Valid;
import java.net.URI;

@RequestMapping("/orders")
@RequiredArgsConstructor
@RestController
public class OrderController {

    private final static String ORDER_DEFAULT_URL = "/orders";
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity postOrder(@Valid @RequestBody OrderPostDto orderPostDto) {
        Long orderId = orderService.createOrder(orderPostDto);

        URI location = UriCreator.createUri(ORDER_DEFAULT_URL, orderId);

        return ResponseEntity.created(location).build();
    }

}
