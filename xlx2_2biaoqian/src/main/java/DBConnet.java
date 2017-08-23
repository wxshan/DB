
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.sql.SQLException;
import java.text.ParseException;
import static com.mongodb.client.model.Filters.*;

/**
 * Created by Administrator on 2017/8/17 0017.
 * 数据库之间的操作
 */
public class DBConnet {

    //获取今天开始的毫秒数
    public long starttime = 1497513208;
    //获取今天结束的毫秒数
    public  long endtime= starttime + 86400;
    //查询结束的时间
    public long end =1503331200;

    public void Connet() throws SQLException, ParseException {
        //mongoDB的相关连接操作
        MongoClient mongoClient = new MongoClient("192.168.25.21", 27017);
        MongoDatabase database = mongoClient.getDatabase("Wifi");
        MongoCollection<Document> track = database.getCollection("MobileWifiNew");
        //mysql的连接
        DBHelper db = new DBHelper();
        //获取系统当前的毫秒数

        while(endtime <= end) {
            MongoCursor<Document> mongoCursor = find(track);

                while (mongoCursor.hasNext()) {
                    Document t = mongoCursor.next();
                    try {
                        db.mosaic(t);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }


        }
        db.close();
    }


    public  MongoCursor<Document> find(MongoCollection<Document> table) {

        //FindIterable<Document> t = table.find().skip(skip).limit(limit).batchSize(30);
        Document sortKey = new Document("rtime", 1);
        FindIterable<Document> t = table.find(and(gte("rtime", starttime), lte("rtime", endtime))).sort(sortKey);
        MongoCursor<Document> mongoCursor = t.iterator();
        starttime = starttime + 86400;
        endtime = endtime + 86400;
        return mongoCursor;
    }
}
