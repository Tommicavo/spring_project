package com.project.ecom.restcontrollers;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.ecom.auth.JwtService;
import com.project.ecom.models.dtos.ErrorDto;
import com.project.ecom.models.dtos.LoginDto;
import com.project.ecom.models.dtos.SigninDto;
import com.project.ecom.models.entities.BlackTokenEntity;
import com.project.ecom.models.entities.UserEntity;
import com.project.ecom.services.AuthService;
import com.project.ecom.services.BlackTokenService;
import com.project.ecom.services.UserService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthRestController {
	
	@Autowired
	private AuthService authService;

	@Autowired
	private UserService userService;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private PasswordEncoder pwdEncoder;

	@Autowired
	private BlackTokenService tokenService;

	@PostMapping("/signin")
	@CacheEvict(value = {"users", "roles"}, allEntries = true)
	public ResponseEntity<?> signin(@RequestBody SigninDto signinDto) {

		List<ErrorDto> errorList = authService.validateSigninData(signinDto);
		if (!errorList.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorList);
		}

		try {
			String encodedPwd = pwdEncoder.encode(signinDto.getPassword());
			UserEntity user = userService.insertUser(signinDto, encodedPwd);
			return ResponseEntity.ok().body(user);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto("signin", "signin Exception: " + e.getMessage()));
		}
	}

	@PostMapping("/login")
	@CacheEvict(value = {"users", "roles"}, allEntries = true)
	public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {

		List<ErrorDto> errorList = authService.validateLoginData(loginDto);
		if (!errorList.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorList);
		}

		String token = null;
		try {
			UserEntity user = userService.getUserByEmail(loginDto.getEmail());

			if (user == null) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto("login", "User is null"));
			}
			
			boolean isRightPassword = pwdEncoder.matches(loginDto.getPassword(), user.getPasswordUser());
			
			if (isRightPassword) {
				token = jwtService.createToken(user);
				return ResponseEntity.ok().body(token);
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("login", "Wrong password for email: " + user.getEmailUser()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto("login", "login Exception: " + e.getMessage()));
		}
	}

	@DeleteMapping("/logout")
	@PreAuthorize("isAuthenticated()")
	@CacheEvict(value = {"users", "roles"}, allEntries = true)
	public ResponseEntity<?> logout(HttpServletRequest request) {
		try {
			String token = jwtService.getTokenFromRequest(request);
			String tokenError = null;
			if (token == null || token.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto("logout", "NULL OR EMPTY TOKEN"));
			}
			Map<Boolean, String> validationResult = jwtService.validateToken(token);
			boolean isValidToken = validationResult.containsKey(true);
			if (!isValidToken) {
				tokenError = validationResult.get(false);
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDto("logout", tokenError));
			}
			tokenService.insertBlackToken(new BlackTokenEntity(token));
			return ResponseEntity.ok().body("Logout ok");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto("logout", "Exception in logout: " + e.getMessage()));
		}
	}
}
