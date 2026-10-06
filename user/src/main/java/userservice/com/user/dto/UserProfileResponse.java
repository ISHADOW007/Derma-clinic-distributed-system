package userservice.com.user.dto;

public record UserProfileResponse(
        Long id,
        String email,
        String name
) {
}
