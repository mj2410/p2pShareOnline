package com.p2pShareOnline.model;

import com.p2pShareOnline.business.User;
import com.p2pShareOnline.data.UserIO;
import com.p2pShareOnline.util.CookieUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String url;

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            Cookie[] cookies = request.getCookies();
            String emailAddress = CookieUtil.getCookieValue(cookies,"emailCookie");

            if (emailAddress == null || emailAddress.equals(""))
                url = "/register.jsp";
            else {
                ServletContext sc = request.getServletContext();
                String path = sc.getRealPath("WEB-INF/EmailList.txt");
                user = UserIO.getUser(emailAddress,path);
                session.setAttribute("user",user);
                url = "/client.jsp";
            }
        } else {
            url = "/client.jsp";
        }
        getServletContext().getRequestDispatcher(url).forward(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
