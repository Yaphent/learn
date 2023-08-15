package yaphet;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception {

        System.out.println( "Hello World!" );

        CountDownLatch countDownLatch = new CountDownLatch(1);
        ZooKeeper zk = new ZooKeeper("192.168.177.101:2181,192.168.177.102:2181,192.168.177.103:2181,192.168.177.104:2181",
                2000,(event)->{
            Watcher.Event.KeeperState state = event.getState();
            System.out.println("new __________________"+event.toString());
            System.out.println("state __________________"+state.toString());
            countDownLatch.countDown();
        });
        countDownLatch.await();
        ZooKeeper.States state = zk.getState();
        System.out.println("创建执行后的状态"+state.toString());
        String s = zk.create("/yaphetzookeeper", "firstData".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("create============="+s);
        byte[] data = zk.getData("/yaphetzookeeper",System.out::println, new Stat());
        final Stat  stat=new Stat();
        //int rc, String path, Object ctx, byte[] data, Stat stat
        zk.getData("/yaphetzookeeper", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                 System.out.println("触发回调：：：：：：：：：：：：：：：：： "+event.toString());
                try {
                    //true   default Watch  被重新注册   new zk的那个watch
                    zk.getData("/yaphetzookeeper",this  ,stat);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },( rc,  path,  ctx, resultdata,  restat)->{
            System.out.println("rc*************"+rc);
            System.out.println("path*************"+path);
            System.out.println("ctx*************"+ctx);
            System.out.println("data*************"+new String(resultdata));
            System.out.println("stat*************"+restat.toString());

        },"aaa");
        System.out.println("getData,直接拿数据。");

        //触发回调
        Stat stat1 = zk.setData("/yaphetzookeeper", "newdata1".getBytes(), 0);

        System.out.println("stat1----------"+new String(stat1.toString()));
        Stat stat12 = zk.setData("/yaphetzookeeper", "newdata2".getBytes(), 1);
        System.out.println("stat12----------"+new String(stat12.toString()));


        Thread.sleep(11111111);
    }
}
