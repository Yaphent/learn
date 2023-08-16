package yaphet;

import org.apache.zookeeper.KeeperException;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */




    @Test
    public void work()
    {
        for (int i = 0; i < 10; i++) {
           new Thread(()->{
               ZKLock zk = new ZKLock();
               zk.setThreadName(Thread.currentThread().getName());
               System.out.println(zk.getThreadName()+"--------------------争抢锁");
               zk.lock();
               System.out.println(zk.getThreadName()+"--------------------干活");
               try {
                   zk.unLock();
               } catch (InterruptedException e) {
                   e.printStackTrace();
               } catch (KeeperException e) {
                   e.printStackTrace();
               }
           }).start();
        }





    }




}
