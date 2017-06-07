package data;

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

/**
 * Created by 雪亚 on 2017/5/4.
 */
@WebServlet("/month.do")
public class EveryMonth extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        DB register = new DB();
        int month=5;

        ResultSet rs;
        int data=0;
        int[] monthData=new int[7];

        try {
            for (int i = 0; i <7; i++) {
             String sql = "select * from user_log ,user_info where time_stamp like '"+month+"%'  and user_info.user_id=user_log.user_id group by user_info.user_id";


                rs = register.executeQuery(sql);

                month=month+1;
                rs.last();
                int rowCount = rs.getRow();
                monthData[i] = rowCount;
                System.out.println(month);

                //time=rs.getInt("time_stamp");

                System.out.println(monthData[i]);

            }
        }catch (SQLException sqle){
            sqle.printStackTrace();
        }

        JSONObject json=new JSONObject();
        try {
            json.put("May", monthData[0]);
            json.put("Jun", monthData[1]);
            json.put("Jly", monthData[2]);
            json.put("Aug", monthData[3]);
            json.put("Sep", monthData[4]);
            json.put("Oct", monthData[5]);
            json.put("Noc", monthData[6]);

        }catch (JSONException jse){
            jse.printStackTrace();
        }
        resp.setContentType("text/html; charset=UTF-8");
        resp.getWriter().print(json);
        resp.getWriter().flush();
        resp.getWriter().close();
    }
}

