package phongvan.hischoolbackend.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import phongvan.hischoolbackend.Payload.Request.LoginRequest;
import phongvan.hischoolbackend.Payload.Request.SignupRequest;
import phongvan.hischoolbackend.Payload.Response.MessageResponse;
import phongvan.hischoolbackend.Payload.Response.UserInfoResponse;
import phongvan.hischoolbackend.Repository.RoleRepository;
import phongvan.hischoolbackend.Repository.UserRepository;
import phongvan.hischoolbackend.entity.ERole;
import phongvan.hischoolbackend.entity.Role;
import phongvan.hischoolbackend.entity.User;
import phongvan.hischoolbackend.security.jwt.JwtUtils;
import phongvan.hischoolbackend.security.services.UserDetailsImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        boolean userExists = userRepository.existsByUsername(loginRequest.getUsername());
        try {
            if (!userExists) {
                return ResponseEntity
                        . ok()
                        .body(new MessageResponse(-1, "Error: USERNAME OR PASSWORD IS INCORRECT!",null));
            } else {
                    Authentication authentication = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

                    ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

                    List<String> roles = userDetails.getAuthorities().stream()
                            .map(item -> item.getAuthority())
                            .collect(Collectors.toList());


                    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                            .body(new UserInfoResponse(userDetails.getId(),
                                    userDetails.getUsername(),
                                    userDetails.getEmail(),
                                    userDetails.getPhone(),
                                    roles, 0, "Sign in success!"));

            }
        }catch(AuthenticationException e) {
                return ResponseEntity
                        .ok()
                        .body(new MessageResponse(-1, "Error: Sign in!",null));
            }

        }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1,"Error: Username is already taken!","username"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1,"Error: Email is already in use!","email"));
        }
        if (userRepository.existsByPhone(signUpRequest.getPhone())) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1,"Error: Phone is already in use!","phone"));
        }

        User user = User.builder()
                .username(signUpRequest.getUsername())
                .email( signUpRequest.getEmail())
                .phone(signUpRequest.getPhone())
                .gender(signUpRequest.getGender())
                .password(encoder.encode(signUpRequest.getPassword()))
                .build();

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "manager":
                        Role managerRole = roleRepository.findByName(ERole.ROLE_MANAGER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(managerRole);

                        break;
                    case "headteacher":
                        Role headteacherRole = roleRepository.findByName(ERole.ROLE_HEADTEACHER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(headteacherRole);

                        break;
                    case "teacher":
                        Role teacherRole = roleRepository.findByName(ERole.ROLE_TEACHER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(teacherRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        user.setActive(true);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse(0,"User registered successfully!",null));
    }
    @PostMapping("/signout")
    public ResponseEntity<?> signout() {
        ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
        if(jwtCookie != null){
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(new MessageResponse(0,"Sign out successfully!",null));
        }else {
            return ResponseEntity.status(500).body(new MessageResponse(-1,"Error sign out!",null));
        }

    }
}