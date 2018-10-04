package com.example.HttpSession.Controllers;

import com.example.HttpSession.Services.JwtTokenGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MainController {


    private JwtTokenGeneratorService jwtTokenGeneratorService;

    Map<String, String> userDb = new HashMap<>();

    @Autowired
    public void MainController(JwtTokenGeneratorService jwtTokenGeneratorService) {
        this.jwtTokenGeneratorService = jwtTokenGeneratorService;
        userDb.put("mithoon", "pass");
        userDb.put("sachin", "word");
    }


    @RequestMapping(method = RequestMethod.GET, value = "/login")
    public String getLoginPage () {
        return "loginPage";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/logout")
    public String logout (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/loginData")
    public String sendLoginStatus (HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        Map<String, String[]> paramMap = request.getParameterMap();
        String userName = paramMap.get("userName")[0];
        String password = paramMap.get("password")[0];
        if (!userDb.get(userName).equals(password)) {
            return "error";
        } else {
            String generatedJwtToken = jwtTokenGeneratorService.generateJwtToken(userName);
            Cookie cookie = new Cookie("jwtToken" , generatedJwtToken);
            response.addCookie(cookie);
        }
        return "logoutPage";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/page")
    @ResponseBody
    public String getPage(HttpServletRequest request) throws UnsupportedEncodingException {
        String jwtToken = request.getHeader("Authorization");
        String userName;
        if (jwtToken != null) {
            userName = jwtTokenGeneratorService.decodeJwtToken(jwtToken);
            return "Protected Page access provided to " + userName;
        } else {
            return "Unauthorized to access the page";
        }
    }
}
