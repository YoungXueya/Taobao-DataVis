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
@WebServlet("/age_gender.do")
public class ageAndgender extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


         DB register = new DB();
         ResultSet rs;
         int[] ageData=new int[8];
        int[] genderData=new int[3];


        try {
            for (int i = 1; i <9; ++i) {

                String sql = "select * from user_info,user_log where user_info.user_id=user_log.user_id and age_range="+i+" group by user_info.user_id";

                rs = register.executeQuery(sql);
                rs.last();
                int rowCount = rs.getRow();
                ageData[i-1] = rowCount;
//                System.out.println(i);
//                System.out.println(ageData[i-1]);

            }
            for(int i=0;i<3;i++) {

                String sql = "select * from user_info,user_log where user_info.user_id=user_log.user_id and gender=" + i + " group by user_info.user_id";
                rs = register.executeQuery(sql);
                rs.last();
                int rowCount = rs.getRow();
                genderData[i] = rowCount;
                System.out.println(genderData[i]);
            }
        }catch (SQLException sqle){
            sqle.printStackTrace();
        }

        JSONObject json=new JSONObject();
        try {
            json.put("Age1", ageData[0]);
            json.put("Age2", ageData[1]);
            json.put("Age3", ageData[2]);
            json.put("Age4", ageData[3]);
            json.put("Age5", ageData[4]);
            json.put("Age6", ageData[5]);
            json.put("Age7", ageData[7]+ageData[6]);
            json.put("male",genderData[1]);
            json.put("female",genderData[0]);
            json.put("gunknown",genderData[2]);



        }catch (JSONException jse){
            jse.printStackTrace();
        }
        resp.setContentType("text/html; charset=UTF-8");
        resp.getWriter().print(json);
        resp.getWriter().flush();
        resp.getWriter().close();
    }
}

