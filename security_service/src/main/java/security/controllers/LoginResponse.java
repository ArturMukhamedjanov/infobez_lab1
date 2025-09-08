package security.controllers;

import security.models.auth.Role;
import lombok.Builder;

@Builder(toBuilder = true)
public record LoginResponse(
        String token
) {
}
