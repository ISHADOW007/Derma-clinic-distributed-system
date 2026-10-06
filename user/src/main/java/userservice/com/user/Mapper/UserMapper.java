package userservice.com.user.Mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import userservice.com.user.Entity.User;
import userservice.com.user.dto.SignupRequest;
import userservice.com.user.dto.UserDto;
import userservice.com.user.dto.UserProfileResponse;
import userservice.com.user.security.JwtUserPrincipal;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(SignupRequest signupRequest);

    @Mapping(source = "userId", target = "id")
    UserProfileResponse toUserProfileResponse(JwtUserPrincipal user);

    UserDto toUserDto(User user);

}

