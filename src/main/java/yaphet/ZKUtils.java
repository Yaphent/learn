package yaphet;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * @Author: wuyfk
 * @Date: 2023/8/15 17:19
 * @Description:
 */
public class ZKUtils {


    public static ZooKeeper getZk() {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper("192.168.177.104:2181/lock", 100000, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    System.out.println("event"+event);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return zk;
    }
}
