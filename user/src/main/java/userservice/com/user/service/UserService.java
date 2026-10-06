package userservice.com.user.service;


import org.springframework.data.domain.Page;
import userservice.com.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);

    List<UserDto> getAllUsers();

    UserDto getUserById(Long id);

    UserDto updateUser(Long id, UserDto userDto);

    void deleteUser(Long id);

    Page<UserDto> searchUsers(
            Integer minAge,
            Integer maxAge,
            int page,
            int size,
            String sortDirection
    );
}
