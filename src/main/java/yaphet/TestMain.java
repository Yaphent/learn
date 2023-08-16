package yaphet;

import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * @Author: wuyfk
 * @Date: 2023/8/16 0:23
 * @Description:
 */
public class TestMain {
    public static void main(String[] args) throws InterruptedException {
        ZooKeeper  zooKeeper=  ZKUtils.getZk();
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                ZKLock zk = new ZKLock();
                zk.setZk(zooKeeper);
                zk.setThreadName(Thread.currentThread().getName());
                System.out.println(zk.getThreadName()+"--------------------争抢锁");
                zk.lock();
                System.out.println(zk.getThreadName()+"--------------------干活");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    zk.unLock();
                    System.out.println(zk.getThreadName()+"--------------------释放锁完毕");
                    countDownLatch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
        countDownLatch.await();
        zooKeeper.close();
    }
}
