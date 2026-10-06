package userservice.com.user.dto;


public record AuthResponse(
        String token,
        UserProfileResponse user
) {

}
