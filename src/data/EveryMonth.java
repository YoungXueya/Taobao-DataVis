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
@WebServlet("/month.do1")
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
                String sql = "select * from user_log ,user_info where time_stamp like '" + month + "%'  and user_info.user_id=user_log.user_id group by user_info.user_id";


                rs = register.executeQuery(sql);


                rs.last();
                int rowCount = rs.getRow();
                monthData[i] = rowCount;
                String totalNum = "insert into month_data (total,month) values('" + rowCount + "','" + month + "') ";
                System.out.println(month);
                register.update(totalNum);
                for (int m = 0; m < 7; m++) {

                    int Count = 0;
                    String sql1 = "select * from user_info,user_log where user_info.user_id=user_log.user_id  and  time_stamp like '" + month + "%' and age_range=" + m + " group by user_info.user_id";


                    System.out.println(sql1);
                    rs = register.executeQuery(sql1);
                    if (rs.next()) {
                        rs.last();
                        Count = rs.getRow();
                        System.out.println(Count);
                        String age2 = "update  month_data  set age" + m + " = '" + Count + "' where month = '" + month + "';";
                          System.out.println(age2);
                        register.update(age2);
                    } else {
                        //register.update(age1);
                    }
                }
                for (int n = 0; n < 3; n++) {
                    int Count = 0;
                    String sql2 = "select * from user_info,user_log where user_info.user_id=user_log.user_id  and  time_stamp like '" + month + "%' and gender=" + n + " group by user_info.user_id";


                    System.out.println(sql2);
                    rs = register.executeQuery(sql2);
                    if (rs.next()) {
                        rs.last();
                        Count = rs.getRow();
                        System.out.println(Count);
                        String gender = "update  month_data  set gender" + n + " = '" + Count + "' where month = '" + month + "';";
                        register.update(gender);
                    } else {

                    }
                    // System.out.println(genderData[i]);
                }

                month = month + 1;

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

