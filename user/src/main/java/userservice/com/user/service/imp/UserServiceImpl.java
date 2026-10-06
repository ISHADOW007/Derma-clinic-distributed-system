package userservice.com.user.service.imp;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import userservice.com.user.Entity.User;
import userservice.com.user.Kafka.UserCreatedEvent;
import userservice.com.user.dto.UserDto;
import userservice.com.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import userservice.com.user.Kafka.UserKafkaProducer;
import userservice.com.user.service.UserService;


import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserKafkaProducer userKafkaProducer;

    @Override
    public UserDto createUser(UserDto userDto) {

        User user = User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .phone(userDto.getPhone())
                .age(userDto.getAge())
                .build();

        User savedUser = userRepository.save(user);
        UserCreatedEvent event = UserCreatedEvent.builder()
                .userId(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .phone(savedUser.getPhone())
                .age(savedUser.getAge())
                .build();

        userKafkaProducer.sendUserCreatedEvent(event);


        return convertToDto(savedUser);
    }

    @Override
    public List<UserDto> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public UserDto getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found with id: " + id));

        return convertToDto(user);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found with id: " + id));

        user.setName(
                userDto.getName() != null
                        ? userDto.getName()
                        : user.getName()
        );

        user.setEmail(
                userDto.getEmail() != null
                        ? userDto.getEmail()
                        : user.getEmail()
        );

        user.setPhone(
                userDto.getPhone() != null
                        ? userDto.getPhone()
                        : user.getPhone()
        );

        user.setAge(
                userDto.getAge() != null
                        ? userDto.getAge()
                        : user.getAge()
        );

        User updatedUser = userRepository.save(user);

        return convertToDto(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {

        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }

        userRepository.deleteById(id);
    }

    private UserDto convertToDto(User user) {

        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .age(user.getAge())
                
                .build();
    }



    @Override
    public Page<UserDto> searchUsers(
            Integer minAge,
            Integer maxAge,
            int page,
            int size,
            String sortDirection) {

        Sort sort = Sort.by(
                Sort.Direction.fromString(sortDirection),
                "createdAt"
        );

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<User> users;

        if (minAge != null && maxAge != null) {

            users = userRepository.findByAgeBetween(
                    minAge,
                    maxAge,
                    pageable
            );

        } else if (minAge != null) {

            users = userRepository.findByAgeGreaterThanEqual(
                    minAge,
                    pageable
            );

        } else if (maxAge != null) {

            users = userRepository.findByAgeLessThanEqual(
                    maxAge,
                    pageable
            );

        } else {

            users = userRepository.findAll(pageable);
        }

        return users.map(this::convertToDto);
    }
}