package data;

/**
 * Created by 雪亚 on 2017/5/4.
 */


import DAO.DB;
import org.json.JSONException;
import org.json.JSONObject;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

/**
 * Created by 雪亚 on 2016/11/15.
 */
@WebServlet("/day.do1")
public class EverydayData extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        DB register = new DB();
        int time=510;

        ResultSet rs;
        int data=0;
        int[] day=new int[185];

        try {
            for (int i = 0; i <185; i++) {
                int rowCount=0;
             String sql = "select * from user_log,user_info where time_stamp= (select min(time_stamp) from user_log where time_stamp >"+time+") and user_info.user_id=user_log.user_id group by user_info.user_id";
             rs = register.executeQuery(sql);
                String total ="insert into day_data (total) values('" + rowCount + "') where time_stamp ="+time+"";


              if(  rs.next()) {
                  time = rs.getInt("time_stamp");
              }
              else{
                  sql="select min(time_stamp) from user_log where time_stamp>"+time;
                  ResultSet resultSet=register.executeQuery(sql);
                  if(resultSet.next())
                  time=resultSet.getInt("min(time_stamp)");
              }
                rs.last();
                rowCount = rs.getRow();
                day[i] = rowCount;
                 String totalNum = "insert into day_data (total,day) values('" + rowCount + "','" + time + "') ";

                register.update(totalNum);
                System.out.println(time);


            for (int m = 0; m < 7; m++) {

                int Count=0;
                String sql1 = "select * from user_info,user_log where user_info.user_id=user_log.user_id  and time_stamp ="+time+" and age_range="+m+" group by user_info.user_id";



                System.out.println(sql1);
                rs = register.executeQuery(sql1);
                if(rs.next()) {
                    rs.last();
                    Count = rs.getRow();
                    System.out.println(Count);
                    String age2="update  day_data  set age"+m+" = '"+Count+"' where day = '"+time+"';";
                    register.update(age2);
                }else {
                    //register.update(age1);
                }
            }
            for (int n = 0; n < 3; n++) {
               int Count=0;
                String sql2 = "select * from user_info,user_log where user_info.user_id=user_log.user_id  and time_stamp ="+time+" and gender="+n+" group by user_info.user_id";



                System.out.println(sql2);
                rs = register.executeQuery(sql2);
                if(rs.next()) {
                    rs.last();
                    Count = rs.getRow();
                    System.out.println(Count);
                    String gender="update  day_data  set gender"+n+" = '"+Count+"' where day = '"+time+"';";
                    register.update(gender);
                }else {

                }
               // System.out.println(genderData[i]);
            }


            }
        }catch (SQLException sqle){
            sqle.printStackTrace();
        }

        JSONObject json=new JSONObject();
        try {
            json.put("day", day);
        }catch (JSONException jse){
            jse.printStackTrace();
        }
        resp.setContentType("text/html; charset=UTF-8");
        resp.getWriter().print(json);
        resp.getWriter().flush();
        resp.getWriter().close();
    }
}
