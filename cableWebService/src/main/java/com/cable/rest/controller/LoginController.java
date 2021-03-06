package com.cable.rest.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import lombok.extern.log4j.Log4j;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cable.rest.dto.LoginDto;
import com.cable.rest.dto.LoginResponseDto;
import com.cable.rest.exception.BadRequestException;
import com.cable.rest.exception.InvalidRequestException;
import com.cable.rest.response.ErrorResource;

@RestController
@RequestMapping("/login")
@Log4j
public class LoginController extends BaseController {
	
	/**initialize the AtomicInteger class*/
    AtomicInteger ctr=new AtomicInteger(1);
	
	 /** This method validates whether the provided user is authorized*/
    @RequestMapping(value = "/validateuser", method = RequestMethod.POST)
    public @ResponseBody LoginResponseDto login(@RequestBody @Valid LoginDto loginDto,BindingResult bindingResult, HttpServletRequest request) 
    {
    	
    	if (bindingResult.hasErrors()){
			log.info("Request Validation Call");
			throw new InvalidRequestException("Exception", bindingResult);
		}
		
    	Object response= sendtoMQ(loginDto, "loginUser", "loginService");
    	
    	if(response.getClass() == ErrorResource.class){
    		log.info("Service Exception Call");
			throw new BadRequestException("Exception", (ErrorResource)response);
		}
    	
        LoginResponseDto loginRespDto =(LoginResponseDto) response;
        
        if (loginRespDto !=null && loginRespDto.isAuthenticationStatus() == true) {
            // Create a session id if login is successful
            HttpSession session = request.getSession(true);
            // Set the session id in the login Response DTO
            loginRespDto.setSessionid(session.getId());
            log.info("Session created " + session.getId());
            session.setAttribute("userDetailId",loginRespDto.getUser().getUserId());

            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            Integer ctrValue=ctr.getAndIncrement();
            Authentication authentication = 
            		new UsernamePasswordAuthenticationToken(ctrValue, loginRespDto.getUser(), authorities);
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            log.info("LoginController.login method End");
        
        }
        return loginRespDto;	
    }
	
    
    /** This method invalidate the session whether the provided user is*/
    // authorized
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public void logout(HttpServletRequest request) {
        log.info("Logged out called");
        HttpSession session = request.getSession(true);
        session.invalidate();
    }
	
	
	

}
