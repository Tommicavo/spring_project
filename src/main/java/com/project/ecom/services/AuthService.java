package com.project.ecom.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.ecom.models.dtos.ErrorDto;
import com.project.ecom.models.dtos.LoginDto;
import com.project.ecom.models.dtos.SigninDto;

@Service
public class AuthService {
	
	public List<ErrorDto> validateSigninData(SigninDto signinDto) {
		List<ErrorDto> errorList = new ArrayList<>();
		return errorList;
	}

	public List<ErrorDto> validateLoginData(LoginDto loginDto) {
		List<ErrorDto> errorList = new ArrayList<>();
		return errorList;
	}
}
