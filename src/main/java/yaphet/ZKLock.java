package yaphet;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: wuyfk
 * @Date: 2023/8/15 17:28
 * @Description:
 */
public class ZKLock implements AsyncCallback.StringCallback, AsyncCallback.Create2Callback, AsyncCallback.ChildrenCallback,Watcher, AsyncCallback.StatCallback {


    public ZooKeeper getZk() {
        return zk;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }

    private ZooKeeper zk ;

    private String threadName;

    private String pathName;

    private CountDownLatch latch = new CountDownLatch(1);
    public void lock(){
        try {
            Assert.notNull(threadName,"线程名称不可为空");
            //创建注册
            zk.create("/lock",threadName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL, (Create2Callback) this,threadName);
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //create back
    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
        System.out.println(threadName+"  create back path : name+ctx"+path+name+ctx);
        if(null != name) {
            this.pathName=name;
            zk.getChildren("/", false, this, ctx);
        }
    }
    //children call back
    @Override
    public void processResult(int rc, String path1, Object ctx, List<String> children) {
        Collections.sort(children);
        int i = children.indexOf(pathName.substring(1));
        System.out.println(threadName+"  children call back :"+children+"----------path"+pathName+"index"+i);
        if(i==0){
            System.out.println(threadName+"  children call back countDown :"+pathName);
            latch.countDown();
        }else{
            try {
                System.out.println(threadName+"  children call back exists :"+"/"+children.get(i-1));
                zk.exists("/"+children.get(i-1),this,this,ctx);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println(threadName+"  watch :"+event);
        switch (event.getType()) {
            case None:
                break;
            case NodeCreated:
                break;
            case NodeDeleted:
                System.out.println(threadName+"  NodeDeleted ");
                zk.getChildren("/", false, this,"abc");
                break;
            case NodeDataChanged:
                break;
            case NodeChildrenChanged:
                break;
            case DataWatchRemoved:
                break;
            case ChildWatchRemoved:
                break;
            case PersistentWatchRemoved:
                break;
        }
    }
    public void unLock() throws InterruptedException, KeeperException {
        zk.delete(pathName,-1);
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }


    //stat call back
    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
            System.out.println(threadName+" exists call back :path+ctx+stat"+path+ctx+stat);
    }

    @Override
    public void processResult(int rc, String path, Object ctx, String name, Stat stat) {


        System.out.println(threadName+"  create back path : name+ctx"+path+name+ctx);
        if(null != name) {
            this.pathName=name;
            zk.getChildren("/", false, this, ctx);
        }
    }
}
