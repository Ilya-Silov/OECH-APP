package com.example.oechapp.Security;

import com.example.oechapp.Entity.User;
import com.example.oechapp.Repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService  {

    private UserRepository userRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {



        // Здесь вы можете вернуть объект, реализующий UserDetails
//        return new Oauth2UserImpl(oAuth2User);


        OAuth2User user =  super.loadUser(userRequest);
        return new Oauth2UserImpl(user);
    }

}
