import java.io.*;                
import javax.servlet.*;
import javax.servlet.http.*;    
import java.util.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class FacultyAddAbsenteeServlet extends HttpServlet  
{

    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://localhost/AMS";
    
    static final String USER = "root";
    static final String PASS = "aarthi00*";


  public void doGet(HttpServletRequest request , HttpServletResponse res) throws ServletException,  IOException  
  {

        HttpSession session = request.getSession(false);
        Connection conn=null;
        PreparedStatement stmt=null;
        CallableStatement cstmt=null;
        String query;
        ArrayList<String> ids = new ArrayList<String>();
        if(session == null){
            res.sendRedirect("./index.html");
        }
        else{
            res.setContentType("text/html") ;
            String fid=(String)session.getAttribute("username");
            String acid=(String)request.getParameter("acid");
            String stu_id=(String)request.getParameter("student_id");
            Integer sess_id=Integer.parseInt(request.getParameter("session_id"));
            String text="";
            try{
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection(DB_URL,USER,PASS);
                query="{call proc_add_absentee(?,?,?)}";
                cstmt=conn.prepareCall(query);
                cstmt.setString(1,stu_id);
                cstmt.setString(2,acid);
                cstmt.setInt(3,sess_id);
                cstmt.executeUpdate();
                query="{call proc_student_percentage(?,?,?,?)}";
                cstmt=conn.prepareCall(query);
                cstmt.setString(1,stu_id);
                cstmt.setString(2,acid);
                cstmt.registerOutParameter(3,Types.INTEGER);
                cstmt.registerOutParameter(4,Types.INTEGER);
                cstmt.executeUpdate();
                cstmt.close();
                conn.close();
            }
            catch(SQLException se){
                se.printStackTrace();
            }
            catch(Exception e){
            e.printStackTrace();
            }
            finally{
                try{
                    if(stmt!=null)
                        stmt.close();
                }
                catch(SQLException se2){
                }
                try{
                    if(conn!=null)
                        conn.close();
                }
                catch(SQLException se){
                    se.printStackTrace();
                }
            }  
            request.setAttribute("acid", acid);
            RequestDispatcher rd=request.getRequestDispatcher("./CourseAttendance");  
            rd.forward(request, res);
        }
    }

    public void doPost(HttpServletRequest request , HttpServletResponse res) throws ServletException,  IOException
    {
        doGet(request,res);
    }

}