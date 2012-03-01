import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.Properties;

public class register extends HttpServlet{

    public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException,IOException{

        response.setContentType("text/html");
	PrintWriter out = response.getWriter();
	Connection conn = null;

	String registerInsert = null;
	String detailsInsert = null;
	String stocksInsert = null;
	Statement st = null;
	ResultSet rs = null;
	HttpSession session = request.getSession(true);

	try {

	    String firstName = request.getParameter("fname").toString();
	    String midName = request.getParameter("mname").toString();
	    String lastName = request.getParameter("lname").toString();
	    String address = request.getParameter("addr").toString();
	    String cityName = request.getParameter("city").toString();
	    String stateName = request.getParameter("state").toString();
	    int zipcode = Integer.parseInt(request.getParameter("zip"));
	    String loginName = request.getParameter("login").toString();
	    String userPass = request.getParameter("passwd").toString();
	    String stockSym1 = request.getParameter("stock1").toString();
            String stockSym2 = request.getParameter("stock2").toString();
            String stockSym3 = request.getParameter("stock3").toString();

	    Class.forName("com.mysql.jdbc.Driver").newInstance();

	    conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1/mysql","root", "letmein");

	    registerInsert = "insert into userregister(username, password) values('"+loginName+"','"+userPass+"');";

	    detailsInsert = "insert into userdetails(fname, mname, lname, addr, city, state, zipcode, username) values('"+firstName+"','"+midName+"','"+lastName+"','"+address+"','"+cityName+"','"+stateName+"','"+zipcode+"','"+loginName+"');";

	    stocksInsert = "insert into userstocks(username, symbol, symbol1, symbol2) values('"+loginName+"','"+stockSym1+"','"+stockSym2+"','"+stockSym3+"');";

	    st = conn.createStatement();
	    int val1 = st.executeUpdate(registerInsert);
	    int val2 = st.executeUpdate(detailsInsert);

	    int val3 = st.executeUpdate(stocksInsert);
            
	    out.println("stock insert query executed"); 
	    
	    conn.close();
	    
	    response.sendRedirect("/examples/jsp/login.jsp");
	} catch (Exception ex){
	    ex.printStackTrace();
	}
	session.invalidate();
	out.close();
    }
}
