package kr.co.direa.gateway.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Configuration
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Value("${constants.frontend}") private String frontend;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
//        if (authentication.getPrincipal() instanceof OAuth2User) {
//            OAuth2User userDetails = (OAuth2User) authentication.getPrincipal();
//            String username = userDetails.getAttribute("preferred_username");
//
//            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//            List<UserDto> userList = authorities.stream()
//                    .map(authority -> new UserDto(username, authority.getAuthority(), null))
//                    .collect(Collectors.toList());
//
//            for (UserDto userDto : userList) {
//                Optional<Users> existingUserOptional = usersService.findByUsername(username);
//                if (existingUserOptional.isPresent()) {
//                    Users existingUser = existingUserOptional.get();
//                    existingUser.setAuth(userDto.getAuth());
//                    usersService.save(new UserDto(existingUser));
//                } else {
//                    usersService.save(userDto);
//                }
//            }
//        }

        Cookie loggedInCookie = new Cookie("loggedIn", "true");
        loggedInCookie.setMaxAge(86400); // 쿠키의 유효 시간 설정 (예: 86400초 = 24시간)
        loggedInCookie.setPath("/"); // 쿠키가 전송될 경로 설정

        // 응답에 쿠키 추가
        response.addCookie(loggedInCookie);

        response.sendRedirect(frontend);
    }
}
