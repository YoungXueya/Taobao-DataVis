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
import java.net.StandardSocketOptions;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 雪亚 on 2017/5/4.
 */
@WebServlet("/certainDay.do")
public class certainDay extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String d = req.getParameter("day");

        String[] ymd = d.split("-");
        int time_stamp=Integer.parseInt(ymd[1]+ymd[2]);
        System.out.println(time_stamp);


        DB register = new DB();
        ResultSet rs;
        int[] ageData = new int[8];
        int[] genderData = new int[3];



        try {
            for (int i = 1; i < 9; ++i) {

                String sql = "select * from user_info,user_log where user_info.user_id=user_log.user_id  and time_stamp ="+time_stamp+" and age_range="+i+" group by user_info.user_id";
                System.out.println(sql);
                rs = register.executeQuery(sql);
                if(rs.next()) {
                    rs.last();
                    int rowCount = rs.getRow();
                    ageData[i - 1] = rowCount;
                }else {
                    ageData[i - 1] = 0;
                }
            }


            for (int i = 0; i < 3; i++) {
                String sql = "select * from user_info,user_log where user_info.user_id=user_log.user_id  and time_stamp ="+time_stamp+" and gender="+i+" group by user_info.user_id";
              rs = register.executeQuery(sql);
                if(rs.next()) {
                    rs.last();
                    int rowCount = rs.getRow();
                    genderData[i] = rowCount;
                }else {
                    genderData[i]=0;
                }
                System.out.println(genderData[i]);
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        JSONObject json = new JSONObject();
        try {
            json.put("Age1", ageData[0]);
            json.put("Age2", ageData[1]);
            json.put("Age3", ageData[2]);
            json.put("Age4", ageData[3]);
            json.put("Age5", ageData[4]);
            json.put("Age6", ageData[5]);
            json.put("Age7", ageData[7] + ageData[6]);
            json.put("male", genderData[1]);
            json.put("female", genderData[0]);
            json.put("gunknown", genderData[2]);


        } catch (JSONException jse) {
            jse.printStackTrace();
        }
        System.out.println(json);
        resp.setContentType("text/html; charset=UTF-8");
        resp.getWriter().print(json);
        resp.getWriter().flush();
        resp.getWriter().close();
    }
}

