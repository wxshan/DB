import org.bson.Document;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Administrator on 2017/8/17 0017.
 */
public class DBHelper {
    public static final String url = "jdbc:mysql://192.168.25.35:4000/test";
    public static final String name = "com.mysql.jdbc.Driver";
    public static final String user = "root";
    public static final String password = "";

    public Connection conn = null;
    public PreparedStatement pst = null;
    public int COUNT=1;
    public String sql = "insert into wifi1 (deviceId,lng,lat,pos,mhz,g,signaldb,sa,saName,rtime,otime,wtime,cc) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";



    public DBHelper() {
        try {

            Class.forName(name);//指定连接类型
            conn = DriverManager.getConnection(url, user, password);//获取连接
            conn.setAutoCommit(false);
            pst = conn.prepareStatement(sql);//准备执行语句
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //拼接sql
    public void Mosaic(Document p) throws SQLException {
        if ( COUNT <= 100000 ) {
            pst.setString(1,p.getString("deviceId"));
            pst.setString(2,String.valueOf(p.getDouble("lng")));
            pst.setString(3,String.valueOf(p.getDouble("lat")));
            pst.setString(4,p.getString("pos"));
            pst.setInt(5,p.getInteger("mhz"));
            pst.setInt(6,p.getInteger("g"));
            pst.setInt(7,p.getInteger("signaldb"));
            pst.setString(8,p.getString("sa"));
            pst.setString(9,p.getString("saName"));
            pst.setLong(10,p.getLong("rtime"));
            pst.setLong(11,p.getLong("otime"));
            pst.setLong(12,p.getLong("wtime"));
            pst.setLong(13,p.getInteger("cc"));
            pst.addBatch();

            if (COUNT % 5000 == 0) {
                pst.executeBatch();
                conn.commit();
                pst.clearParameters();
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
