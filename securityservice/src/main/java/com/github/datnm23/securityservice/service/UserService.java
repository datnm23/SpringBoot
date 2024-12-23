package com.github.datnm23.securityservice.service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserService {

    final PasswordEncoder passwordEncoder;

    final UserRepository userRepository;

    final RoleRepository roleRepository;

    final UserCustomRepository userCustomRepository;

    public UserResponse getDetail(UUID id) throws ObjectNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("User not found with id: " + id));
        return toUserResponse(user);
    }

    public UserResponse createUser(CreateUserRequest request) throws ExistedUserException {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isPresent()) {
            throw new ExistedUserException("Email already exists");
        }

        Set<Role> roles = roleRepository.findByName(Roles.USER).stream().collect(Collectors.toSet());

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode("123")) // TODO: Replace with random password generation
                .roles(roles)
                .status(UserStatus.ACTIVATED)
                .build();
        userRepository.save(user);
        return toUserResponse(user);
    }

    public CommonSearchResponse<UserSearchResponse> searchUser(UserSearchRequest request) {
        List<SearchUserDto> result = userCustomRepository.searchUser(request);

        Long totalRecord = 0L;
        List<UserSearchResponse> userResponses = new ArrayList<>();
        if (!result.isEmpty()) {
            totalRecord = result.get(0).getTotalRecord();
            userResponses = result.stream()
                    .map(this::toUserSearchResponse)
                    .collect(Collectors.toList());
        }

        int totalPage = (int) Math.ceil((double) totalRecord / request.getPageSize());

        return CommonSearchResponse.<UserSearchResponse>builder()
                .totalRecord(totalRecord)
                .totalPage(totalPage)
                .data(userResponses)
                .pageInfo(new CommonSearchResponse.CommonPagingResponse(request.getPageSize(), request.getPageIndex()))
                .build();
    }

    private UserResponse toUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        // Map other fields as needed
        return response;
    }

    private UserSearchResponse toUserSearchResponse(SearchUserDto dto) {
        UserSearchResponse response = new UserSearchResponse();
        response.setId(dto.getId());
        response.setEmail(dto.getEmail());
        response.setStatus(dto.getStatus());
        // Map other fields as needed
        return response;
    }

}