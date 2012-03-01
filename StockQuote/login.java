import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.Properties;

public class login extends HttpServlet{ 
 public void doPost(HttpServletRequest request, HttpServletResponse response)
   throws ServletException,IOException{
  response.setContentType("text/html");
  PrintWriter out = response.getWriter();
  Connection conn = null;
 
  String strQuery1= ""; 
  String strQuery2 = "";
  Statement st1=null;
  Statement st2=null;
  ResultSet rs1=null;
  ResultSet rs2=null;

  HttpSession session = request.getSession(true);
 
  try {
     
     if (request.getParameter("userName") != null && request.getParameter("userName") != " " 
  	 && request.getParameter("password") != null && request.getParameter("password") != " ")
     {

  	 String username = request.getParameter("userName").toString();
  	 String userpass = request.getParameter("password").toString();

	 Class.forName("com.mysql.jdbc.Driver").newInstance();

	 conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1/mysql","root", "letmein");

   	 strQuery1="select * from userregister where username='"+username+"' and  password='"+userpass+"'";

   	 out.println(strQuery1);

  	 st1 = conn.createStatement();
  	 rs1 = st1.executeQuery(strQuery1);

  	 int count1 = 0;
  	 int count2 = 0;

  	 while ( rs1.next() )
  	 {
  	     session.setAttribute("username",rs1.getString(1));
  	     count1++;
  	 }

  	 if (count1 > 0)
  	 {
	     strQuery2="select symbol, symbol1, symbol2 from userstocks where username='"+username+"'";
	     
  	     st2 = conn.createStatement();
  	     rs2 = st2.executeQuery(strQuery2);
  	     while(rs2.next())
	     {
                     session.setAttribute("symbol1", rs2.getString(1));
		     session.setAttribute("symbol2", rs2.getString(2));
		     session.setAttribute("symbol3", rs2.getString(3));

		     count2++;
	     }

	     response.sendRedirect("/examples/jsp/welcome.jsp");
  	 } else{
  	     response.sendRedirect("/examples/jsp/login.jsp");
  	 }

     } else{
  	 response.sendRedirect("login.jsp");
     }
    
  }  catch (Exception e) {
      e.printStackTrace();
  } 
 }
}
