package NewData;

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
 * Created by 雪亚 on 2017/6/9.
 */
@WebServlet("/age_gender.do")
public class ageAndGender extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        DB register = new DB();
        ResultSet rs;
        int[] ageData ={0,0,0,0,0,0,0};
        int[] genderData={0,0,0};
        int month = 5;
        try {
            for (int i = 0; i < 7; i++) {
                String sql = "select * from month_data  where month = " + month;

                rs = register.executeQuery(sql);
                if (rs.next()){
                    ageData[0]+=rs.getInt("age0");
                    ageData[1]+=rs.getInt("age1");
                    ageData[2]+=rs.getInt("age2");
                    ageData[3]+=rs.getInt("age3");
                    ageData[4]+=rs.getInt("age4");
                    ageData[5]+=rs.getInt("age5");
                    ageData[0]+=rs.getInt("age6");
                    genderData[0]+=rs.getInt("gender0");
                    genderData[1]+=rs.getInt("gender1");
                    genderData[2]+=rs.getInt("gender2");

                }

                month += 1;


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
            json.put("Age7",  ageData[6]);
            json.put("male", genderData[1]);
            json.put("female", genderData[0]);
            json.put("gunknown", genderData[2]);


        } catch (JSONException jse) {
            jse.printStackTrace();
        }
        resp.setContentType("text/html; charset=UTF-8");
        resp.getWriter().print(json);
        resp.getWriter().flush();
        resp.getWriter().close();
    }
}
