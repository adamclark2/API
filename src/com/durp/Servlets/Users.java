package com.durp.Servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

import com.google.gson.*;

import com.durp.Model.Managers.*;
import com.durp.Model.Exceptions.*;
import com.durp.Model.*;

/**
 * Access Users via the web
 *
 *@author Adam
 */
public class Users extends HttpServlet {

    private DatabaseManager dm;
    private PreparedStatement getCountOfUsersWithEmail;
    private PreparedStatement getUserInfoFromEmail;

    @Override
    public void destroy() {
        super.destroy();
        try {
            getUserInfoFromEmail.close();
            getCountOfUsersWithEmail.close();
            dm.close();
        }catch (Exception e){
            //TODO add better exception handling destroy()
        }
    }

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            dm = new DatabaseManager();
            Connection c = dm.getConnection();
            getUserInfoFromEmail = c.prepareStatement("SELECT EMail,Password,FirstName,LastName FROM Users WHERE 1=1 AND Email=?;");
            getCountOfUsersWithEmail = c.prepareStatement("SELECT COUNT(*) FROM Users WHERE 1=1 AND Email=?;");
        }catch (Exception e){
            //TODO add better exception handling in init()
            throw new ServletException("ERROR: " + e.getMessage() + "STACK:\n\n" + e.getStackTrace());
        }
    }

    /**
     * POST to a User
     *
     * Actions include:
     * <ul>
     *     <li>registerUser</li>
     *     <li>login</li>
     * </ul>
     *
     * @author Adam
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        String action = req.getParameter("Action");
        String APIKey = req.getParameter("APIKey");
        action = action == null? "": action;

        if(!APIKeyManager.verifyKey(APIKey)){
            //APIKey Invalid, return standard message
            out.println(APIKeyManager.getErrorMessage());
            return;
        }

        switch (action){
            case "registerUser":
                registerUser();
                break;

            case "login":
                String s = req.getParameter("Content");
                try{
                    String loginString = login(new Gson().fromJson(s, User.class));
                    out.println(loginString);
                }catch(Exception e){
                    out.println(new Gson().toJson(new UserError(e.getMessage())));
                }

                break;

            default:
                out.println("UNSUPPORTED");
                break;
        }
    }

    /**
     * register a User
     *
     * TODO implement user registration
     * @author Adam
     * @return String- the JSON the server should send to the client
     */
    private String registerUser(){
        return "NOT IMPLEMENTED!";
    }

    /**
     * login
     *
     * This is called from post
     * @author Adam
     * @return
     */
    private String login(User user){
        //TODO add real functionality, this just gets the user and returns the user
        try {
            User u = getUser(user.email);
            //Passwords should be hashed and salted,
            //but the client does not need to know it
            //That's why we have session tokens!
            u.password = null;

            return new Gson().toJson(u);
        }catch (Exception e){
            ExceptionHandler.handleException(e, this.getClass().getName());
            //TODO implement ExceptionHandler
            return "ERROR! :<p>" + e.getStackTrace() + "<p><p>Message:" + e.getMessage();
        }
    }

    /**
     * Ensure a user is valid
     *
     * TODO check email format, Create new Exceptions for different scenarios
     * @author Adam
     */
    private void validateUser(String email) throws Exception{
        //Ensure the user is in the database
        getCountOfUsersWithEmail.setString(1, email);
        ResultSet rs = getCountOfUsersWithEmail.executeQuery();
        rs.next();
        if(rs.getInt(1) != 1){
            throw new InvalidUserException("The user count was invalid count:" + rs.getInt(1));
        }
        rs.close();
    }

    /**
     * Given email get a User
     *
     * @author Adam
     * @param email
     * @return
     * @throws Exception
     */
    private User getUser(String email) throws Exception{
        validateUser(email);

        getUserInfoFromEmail.setString(1, email);
        ResultSet rs = getUserInfoFromEmail.executeQuery();
        rs.next();
        User u = new User();
        u.email = rs.getString("Email").trim();
        u.password = rs.getString("Password").trim();
        u.firstName = rs.getString("FirstName").trim();
        u.lastName = rs.getString("LastName").trim();

        rs.close();
        return u;
    }

}
