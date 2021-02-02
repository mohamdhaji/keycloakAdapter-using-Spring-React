package com.dress.shop.controllers;

import com.dress.shop.domain.*;
import com.dress.shop.exceptions.AuthException;
import com.dress.shop.services.KeyCloakService;
import com.dress.shop.services.MapValidationErrorService;
import com.dress.shop.validator.UserValidator;
import org.keycloak.adapters.spi.AuthOutcome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/keycloak")
@CrossOrigin
public class KeycloakController {


	@Autowired
	private MapValidationErrorService mapValidationErrorService;

	@Autowired
	private UserValidator userValidator;

	@Autowired
	KeyCloakService keyClockService;

	/*
	 * Get token for the first time when user log in. We need to pass
	 * credentials only once. Later communication will be done by sending token.
	 */

	@RequestMapping(value = "/token", method = RequestMethod.POST)
	public ResponseEntity<?> getTokenUsingCredentials(@Valid @RequestBody UserCredentials userCredentials,BindingResult result) {

		ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
		if(errorMap!=null) return errorMap;




		LoginResponse responseToken = null;
			responseToken = keyClockService.getToken(userCredentials);



		return new ResponseEntity<>(responseToken, HttpStatus.OK);

	}

	/*
	 * When access token get expired than send refresh token to get new access
	 * token. We will receive new refresh token also in this response.Update
	 * client cookie with updated refresh and access token
	 */
	@RequestMapping(value = "/refreshtoken", method = RequestMethod.GET)
	public ResponseEntity<?> getTokenUsingRefreshToken(@RequestHeader(value = "Authorization") String refreshToken) {

		LoginResponse responseToken = null;
		try {

			responseToken = keyClockService.getByRefreshToken(refreshToken);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(responseToken, HttpStatus.OK);

	}

	/*
	 * Creating user in keycloak passing UserDTO contains username, emailid,
	 * password, firtname, lastname
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> createUser(@Valid @RequestBody User user, BindingResult result) {

		// Validate passwords match
		userValidator.validate(user,result);

		ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
		if(errorMap!=null) return errorMap;

		try {

			return keyClockService.createUserInKeyCloak(user);
		}

		catch (Exception ex) {

			ex.printStackTrace();
			return new ResponseEntity<>("Username==" + user.getUserName() + " could not be created in keycloak", HttpStatus.BAD_REQUEST);

		}

	}

	@PostMapping("/updateuser/{uemail}")
	public ResponseEntity<?> updateUser(HttpServletRequest request,@RequestBody UpdatedUser updatedUser,@PathVariable("uemail") String  uemail){

		String role="user";

		if (keyClockService.authenticate(request,role).equals(AuthOutcome.AUTHENTICATED)) {


	return new ResponseEntity<>(keyClockService.updateUserInKeyCloak(updatedUser,uemail),HttpStatus.OK);

		}else {
			return new ResponseEntity("Hi!, you are NOT auhorized !", HttpStatus.UNAUTHORIZED);

		}

	}

//	@RequestMapping(value = "/auth", method = RequestMethod.POST)
//	public ResponseEntity<?> getUserInfo(HttpServletRequest request){
//
//		TokenPayload tokenPayload=keyClockService.authenticate(request);
//
//		if(tokenPayload ==null)
//			throw new AuthException("token is not valid");
//
//		return new ResponseEntity<>(tokenPayload, HttpStatus.OK);
//
//	}

}
