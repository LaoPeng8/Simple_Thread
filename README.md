# Simple_Thread
多线程入门(复习语法-复习自2019年学习JavaSE时的多线程语法)

遇到一个问题, 原本编译后的文件应该存在与target/classes中, 不知为何却生成在了项目根目录/${project.build.directory}/该目录下, 
百度后发现该${project.build.directory}是maven的一个参数,默认为target/, 那么在我没有配置该参数的情况下, 存放在target/中是正常的,
但是在我配置的情况下,编译后的文件却出现在了${project.build.directory}目录下, 我估计是maven的${project.build.directory}参数不知为何取不到值了,
所以就直接是参数的形式出现在了目录名上, 我估计的是当我配置该参数值为target/后应该没问题了, 但是很奇怪网上并没有讲解该参数如何配置的,
我尝试过`<properties><project><build><directory>target/</></></>`不行, `<properties><project-build-directory>target<properties/>`,
也不行, 后只能将项目删了重新new project, 逐解决. (加上写该文件耗时40分钟, 艹)

记录一个小问题  
调用Arrays.asList()生产的List的add、remove方法时报异常 java.lang.UnsupportedOperationException  
原因:  
Arrays.asList() 返回的是Arrays的内部类ArrayList, 而不是java.util.ArrayList.  
Arrays的内部类ArrayList和java.util.ArrayList都是继承AbstractList, remove、add等方法AbstractList中是默认就是 throw UnsupportedOperationException  
java.util.ArrayList重新了这些方法而Arrays的内部类ArrayList没有重新，所以会抛出异常。


### ThreadPoolExecutor
```java
public class ThreadPoolExecutor extends AbstractExecutorService {

    private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));
    private static final int COUNT_BITS = Integer.SIZE - 3;

    // 1 << 29 实际上是表示线程池状态的最高位的第三位为1, 此时 - 1, 就变成了表示线程状态的最高3位为0, 表示线程数量的低29位为全1 (该操作 HashMap中也出现过)
    private static final int COUNT_MASK = (1 << COUNT_BITS) - 1;

    // runState is stored in the high-order bits
    private static final int RUNNING    = -1 << COUNT_BITS;
    private static final int SHUTDOWN   =  0 << COUNT_BITS;
    private static final int STOP       =  1 << COUNT_BITS;
    private static final int TIDYING    =  2 << COUNT_BITS;
    private static final int TERMINATED =  3 << COUNT_BITS;

    // Packing and unpacking ctl
    private static int runStateOf(int c)     { return c & ~COUNT_MASK; }

    /**
     * 获取 线程数量
     * 通过 ctl | 0001111...111 获取到低29位的数字
     *
     * @param c
     * @return
     */
    private static int workerCountOf(int c)  { return c & COUNT_MASK; }

    /**
     * 可以看到该方法出现在初始化 ctl 变量时, 还有设置线程池状态时
     * 那么该方法实际上作用就是 设置 ctl 变量的值, 换句话是就是 设置线程池的状态 和 线程个数
     * 
     * ctlOf(RUNNING, 0) 效果就是, 先将 -1(101 -> 111)左移29位, 也就是int的最高3位, 然后位运算 | 0, 那么得到的结果就是 最高3位设置为了RUNNING,剩下29位都是0表示线程个数
     * ctl.set(ctlOf(TERMINATED, 0)); 效果就是, 3(011)左移29位, 就设置了TERMINATED, 011 | 0, 最后结果就是 01100..., 
     * 如果是 (ctlOf(RUNNING, 7)), 最后结果就是 11100... | 0000...111 最后结果就是 1110000...0000111 即表示RUNNING, 线程数7
     * 
     * @param rs
     * @param wc
     * @return
     */
    private static int ctlOf(int rs, int wc) { return rs | wc; }
    
    /**
     * 先从 execute 方法开始, 该方法是接收一个 线程,并执行
     * @param command
     */
    public void execute(Runnable command) {
        if (command == null)
            throw new NullPointerException();

        /**
         * 在上面可以看到 ctl 它是一个原子的int
         * 一个int是32bit, 5种状态 RUNNING, SHUTDOWN, STOP, TIDYING, TERMINATED 实际上只需要3bit 表示; 即 111 = -1 ~ 011 = 3;
         * 32 - 3 = 29; 即一个int还剩下29位, 为了不浪费,这29位现在被作用于保存线程的个数
         * 即 ctl这一个int变量, 相当于保存了2个变量的作用 = 保存线程池的状态 + 线程的个数
         * 
         * ctl 初始化时 new AtomicInteger(ctlOf(RUNNING, 0));
         * 实际上就是 设置 ctl的值为: 线程池状态为运行 + 线程个数 0
         */
        int c = ctl.get();
        
        // 如果线程数量 < 核心线程数 则 直接新建线程并设置为核心线程
        if (workerCountOf(c) < corePoolSize) {
            if (addWorker(command, true))
                return;
            c = ctl.get();
        }
        
        // 如果当前线程池处于运行状态 且 向工作队列中插入任务(线程) 成功, 则...
        if (isRunning(c) && workQueue.offer(command)) {
            int recheck = ctl.get();
            if (! isRunning(recheck) && remove(command))
                reject(command);
            else if (workerCountOf(recheck) == 0)
                addWorker(null, false);
        }
        else if (!addWorker(command, false))
            reject(command);
    }
}
```
