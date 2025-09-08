package security.controllers;

import security.auth.AuthenticationRequest;
import security.auth.RegisterRequest;
import security.auth.services.AuthenticationService;
import security.models.orders.Deliverer;
import security.models.orders.Seller;
import security.models.orders.Customer;
import security.models.dto.DelivererDto;
import security.models.dto.SellerDto;
import security.models.auth.Role;
import security.models.dto.CustomerDto;
import security.models.mapper.DelivererMapper;
import security.models.mapper.SellerMapper;
import security.models.mapper.CustomerMapper;
import security.services.UserService;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.Cookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final CustomerMapper customerMapper;
    private final SellerMapper sellerMapper;
    private final DelivererMapper delivererMapper;
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(
            @RequestBody AuthenticationRequest authenticationRequest,
            HttpServletResponse response
    ){
        var authResponse = authenticationService.authenticate(authenticationRequest);
        response.addCookie(createCookie(authResponse.getToken()));
        return ResponseEntity.ok(LoginResponse.builder().role(authResponse.getRole()).build());
    }

    private boolean validateCustomer(CustomerDto customerDto){
        return customerDto.email()!= null
                && customerDto.password() != null
                && customerDto.x() != null
                && customerDto.y() != null;
    }

    private boolean validateSeller(SellerDto sellerDto){
        return sellerDto.email() != null
                && sellerDto.password() != null
                && sellerDto.name() != null
                && sellerDto.x() != null
                && sellerDto.y() != null;
    }

    private boolean validDeliverer(DelivererDto delivererDto){
        return delivererDto.email() != null
                && delivererDto.password() != null
                && delivererDto.distance() != null;
    }

    private Cookie createCookie(String token){
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        return cookie;
    }
}
