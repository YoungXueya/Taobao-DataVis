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
@WebServlet("/day.do")
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
             String sql = "select * from user_log where time_stamp= (select min(time_stamp) from user_log where time_stamp >"+time+")";


                rs = register.executeQuery(sql);

              if(  rs.next()) {
                  time = rs.getInt("time_stamp");
              }
                rs.last();
                int rowCount = rs.getRow();
                day[i] = rowCount;
                System.out.println(time);

                //time=rs.getInt("time_stamp");

                System.out.println(day[i]);

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
