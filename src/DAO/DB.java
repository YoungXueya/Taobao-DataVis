package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {
	// JDBC 驱动器名称和数据库的 URL
    static final String JDBC_DRIVER="com.mysql.jdbc.Driver";  

    static final String DB_URL="jdbc:mysql://localhost:3306/vc?useUnicode=true&characterEncoding=UTF-8" ;

  

    //  数据库的凭据
    static final String USER = "root";
    static final String PASS = "1234";
    
    
    Connection conn=null;
    Statement stmt=null;
    PreparedStatement ps=null;
    
    public DB(){
    	
    }
    public boolean executeAll(String sql){
    	try{
        	//  注册JDBC
            Class.forName("com.mysql.jdbc.Driver");

          
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            
            // 执行 SQL 查询
            stmt = conn.createStatement();
            boolean rs =  stmt.execute(sql);
            return rs;


//            // 清理环境
//            rs.close();
//            stmt.close();
//            conn.close();
         }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
         }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
         }finally{

         } //end try
		return true;
    	
    }
    
    public ResultSet executeQuery(String sql){
    	try{
        	
            Class.forName("com.mysql.jdbc.Driver");


            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            
            // 执行 SQL 查询
            stmt = conn.createStatement();
            ResultSet rs =  stmt.executeQuery(sql);
            return rs;

         
//            // 清理环境
//            rs.close();
//            stmt.close();
//            conn.close();
         }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
         }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
         }finally{

         } //end try
    	return null;
    }	
    
    //返回刚插入的id
    	public int update(String sql){
        	try{

                Class.forName("com.mysql.jdbc.Driver");


                conn = DriverManager.getConnection(DB_URL,USER,PASS);
                
                // 执行 SQL 查询
                ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS );
                ps.executeUpdate();
                ResultSet rs =ps.getGeneratedKeys();
                final int ID_COLUMN_INDEX=1;
              
                while(rs.next()){
                	return rs.getInt(ID_COLUMN_INDEX );
                }
                
             
//                // 清理环境
//                rs.close();
//                stmt.close();
//                conn.close();
             }catch(SQLException se){
                // 处理 JDBC 错误
                se.printStackTrace();
             }catch(Exception e){
                // 处理 Class.forName 错误
                e.printStackTrace();
             }finally{

             } //end try
        	return 0;
    	
    }
}