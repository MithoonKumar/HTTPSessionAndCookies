package com.example.HttpSession.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MainController {
    Map<String, String> userDb = new HashMap<>();


    public void MainController() {
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
    public String sendLoginStatus (HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> paramMap = request.getParameterMap();
        String userName = paramMap.get("userName")[0];
        String password = paramMap.get("password")[0];
        HttpSession session = request.getSession(false);
        if (session == null) {
            session = request.getSession();
            if (userName.equals("mithoon")) {
                session.setAttribute("sessionId" , userName);
            }
            if (userName.equals("sachin")) {
                session.setAttribute("sessionId", userName);
            }
            Cookie cookie = new Cookie("sessionId", session.getAttribute("sessionId").toString());
            response.addCookie(cookie);
        }
        return "logoutPage";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/page")
    @ResponseBody
    public String getPage(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "Unauthorized to access the page";
        } else {
            String userName = session.getAttribute("sessionId").toString();
            return "Protected Page access provided to " + userName;
        }
    }
}
