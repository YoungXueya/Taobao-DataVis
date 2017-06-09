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

@WebServlet("/day.do")
public class EveryDayData extends HttpServlet {
     @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        DB everyday = new DB();
        int time = 510;

        int total=0;
        ResultSet rs;
        int data = 0;
         int[] day=new int[185];

           try {
                for (int i = 0; i <185; i++) {
                    String sql = "select * from day_data where day= (select min(day) from day_data where day >" + time + ")";
                    rs = everyday.executeQuery(sql);
                    if (rs.next()) {
                        time = rs.getInt("day");
                    } else {
                        sql = "select min(day) from day_data where day>" + time;
                        ResultSet resultSet = everyday.executeQuery(sql);
                        if (resultSet.next())
                            time = resultSet.getInt("min(day)");
                    }
                    total = rs.getInt("total");
                    day[i] = total;
                }
           }catch (SQLException sql){
               sql.printStackTrace();
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
