import java.sql.*;

public class DBConnect {

    public static void main(String []args){

	try{
	Class.forName("com.mysql.jdbc.Driver");
	Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/robocode", "root", "");
	Statement stmt = conn.createStatement();
	ResultSet rs = stmt.executeQuery("select * from player");
	while(rs.next()){
	    System.out.println(rs.getString(1));
	}
	}
	catch(Exception e){System.err.println("FUCKED UP: " + e);}
    }


}
