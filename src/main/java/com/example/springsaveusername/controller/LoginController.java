package com.example.springsaveusername.controller;


import com.example.springsaveusername.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@SessionAttributes("user")
public class LoginController {


    @ModelAttribute("user")
    public User setUpUserForm() {
        return new User();
    }

    @RequestMapping("/login")
    public String Index(@CookieValue(value = "setUser", defaultValue = "") String setUser,
                        @CookieValue(value = "loginCount", defaultValue = "0") Long counter,
                        HttpServletResponse response, Model model) {
        counter++;
        Cookie loginCountCookie = new Cookie("loginCount", String.valueOf(counter));
        loginCountCookie.setMaxAge(10);
        response.addCookie(loginCountCookie);
        Cookie userCookie = new Cookie("setUser", setUser);
        model.addAttribute("cookieValue", userCookie);
        model.addAttribute("loginCount", counter);
        return "/login";
    }


    @PostMapping("/doLogin")
    public String doLogin(@ModelAttribute("user") User user, Model model,
                          @CookieValue(value = "setUser", defaultValue = "") String setUser,
                          HttpServletResponse response, HttpServletRequest request) {

        if (user.getEmail().equals("admin@gmail.com")
                && user.getPassword().equals("123456")) {
            if (user.getEmail() != null) {
                setUser = user.getEmail();
            }


            Cookie cookie = new Cookie("setUser", setUser);
            cookie.setMaxAge(10);
            response.addCookie(cookie);


            Cookie[] cookies = request.getCookies();

            for (Cookie ck : cookies) {

                if (!ck.getName().equals("setUser")) {
                    ck.setValue("");
                }
                model.addAttribute("cookieValue", ck);
            }
            model.addAttribute("message", "Login success. Welcome!");
        } else {
            user.setEmail("");
            Cookie cookie = new Cookie("setUser", setUser);
            model.addAttribute("cookieValue", cookie);
            model.addAttribute("message", "Login failed. Try again.");
        }
        return "/login";
    }
}