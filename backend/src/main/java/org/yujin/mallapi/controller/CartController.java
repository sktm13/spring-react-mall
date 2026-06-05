package org.yujin.mallapi.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yujin.mallapi.dto.CartItemDTO;
import org.yujin.mallapi.dto.CartItemListDTO;
import org.yujin.mallapi.service.CartService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    // 카트 내용 변화
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/change")
    public List<CartItemListDTO> changeCart(
            @RequestBody CartItemDTO itemDTO,
            Principal principal) {

        String email = principal.getName();

        // 프론트에서 email을 보내더라도 신뢰하지 않고,
        // JWT 인증된 사용자 email로 서버에서 직접 세팅
        itemDTO.setEmail(email);

        log.info("change cart itemDTO: {}", itemDTO);

        if (itemDTO.getQty() <= 0) {
            return cartService.remove(itemDTO.getCino());
        }

        return cartService.addOrModify(itemDTO);
    }

    // 사용자의 카트를 불러옴
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/items")
    public List<CartItemListDTO> getCartitems(Principal principal) {

        String email = principal.getName();

        log.info("cart owner email: {}", email);

        return cartService.getCartItems(email);
    }

    // 카트 삭제
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{cino}")
    public List<CartItemListDTO> removeFromCart(@PathVariable("cino") Long cino) {

        log.info("cart item no(remove): {}", cino);

        return cartService.remove(cino);
    }
}