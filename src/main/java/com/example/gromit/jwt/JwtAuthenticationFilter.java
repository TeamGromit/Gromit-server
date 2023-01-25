package com.example.gromit.jwt;

import com.example.gromit.exception.BadRequestException;
import com.example.gromit.exception.UnauthorizedException;
import com.example.gromit.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


//@Component
//@RequiredArgsConstructor
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,JwtService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = jwtService.getToken((HttpServletRequest) request);

        // 토큰이 존재한다면
        if(token!=null){

            // 토큰을 검증
            if(jwtService.validateToken(token)){

                //권한
                Authentication authentication = jwtService.getAuthentication(token);

                // security 세션에 등록
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else{
                throw new UnauthorizedException("유효하지 않은 토큰입니다.");
            }

        }else{
            throw new BadRequestException("토큰 값이 필요 합니다.");
        }

        chain.doFilter(request,response);
    }

//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        String token = jwtService.getToken((HttpServletRequest) request);
//
//        // 토큰이 존재한다면
//        if(token!=null){
//
//            // 토큰을 검증
//            if(jwtService.validateToken(token)){
//
//                //권한
//                Authentication authentication = jwtService.getAuthentication(token);
////                System.out.println("authentication = " + authentication);
//
//                // security 세션에 등록
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }else{
//                throw new UnauthorizedException("유효하지 않은 토큰입니다.");
//            }
//
//        }
//
//        chain.doFilter(request,response);
//    }
}
