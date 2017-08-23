import org.bson.Document;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Administrator on 2017/8/17 0017.
 */
public class DBHelper {
    public static final String url = "jdbc:mysql://127.0.0.1:3306/test";
    public static final String name = "com.mysql.jdbc.Driver";
    public static final String user = "root";
    public static final String password = "root";

    public Connection conn = null;
    public PreparedStatement pst = null;
    public int COUNT =1;
    public String prefix ="insert into wifi1 (deviceId,lng,lat,pos,mhz,g,signaldb,sa,saName,rtime,otime,wtime,cc) values ";
    public StringBuffer suffix = new StringBuffer();


    public DBHelper() {
        try {

            Class.forName(name);//指定连接类型
            conn = DriverManager.getConnection(url, user, password);//获取连接
            conn.setAutoCommit(false);
            pst = conn.prepareStatement("");//准备执行语句
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //拼接sql
    public void mosaic(Document p) throws SQLException {
        if ( COUNT <= 100000 ) {
            suffix.append("("+"'" +p.getString("deviceId")+"'" +","
                    +String.valueOf(p.getDouble("lng")) +","
                    +String.valueOf(p.getDouble("lat")) +","
                    +"'"+p.getString("pos")+"'" +","
                    +p.getInteger("mhz") +","
                    +p.getInteger("g") +","
                    +p.getInteger("signaldb") +","
                    +"'" +p.getString("sa")    +"'" +","
                    +"'" +p.getString("saName")+"'" +","
                    +p.getLong("rtime") +","
                    +p.getLong("otime") +","
                    +p.getLong("wtime") +","
                    +p.getInteger("cc")
                    +"),");


            if (COUNT % 10000 == 0) {
                String sql = prefix  + suffix.substring(0, suffix.length() - 1);
                pst.addBatch(sql);
                pst.executeBatch();
                conn.commit();
                pst.clearParameters();
                suffix = new StringBuffer();
            }
            COUNT++;
        }else {
            COUNT = 0;
        }
    }

    public void close() {
        try {
            this.conn.close();
            this.pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
