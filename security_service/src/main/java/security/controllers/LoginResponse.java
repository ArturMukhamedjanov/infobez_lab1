package security.controllers;

import lombok.Builder;

@Builder(toBuilder = true)
public record LoginResponse(
        String token
) {
}
