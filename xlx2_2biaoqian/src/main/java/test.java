/**
 * Created by carl on 17-7-28.
 */

import java.sql.SQLException;
import java.text.ParseException;

public class test {
    public static void main(String[] args) throws SQLException {


        //连接到Db
        DBConnet DBConnet = new DBConnet();
        try {
            DBConnet.Connet();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


}
