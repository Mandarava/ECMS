package com.finance.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.nio.ByteBuffer;
import java.util.concurrent.ThreadFactory;

public class DisruptorTest {

    /**
     * 1.建Event类（数据对象）
     * <p>
     * 2.建立一个生产数据的工厂类，EventFactory，用于生产数据；
     * <p>
     * 3.监听事件类（处理Event数据）
     * <p>
     * 4.实例化Disruptor，配置参数，绑定事件；
     * <p>
     * 5.建存放数据的核心 RingBuffer，生产的数据放入 RungBuffer。
     */
    public static void main(String[] args) {
        disruptorTest();
    }

    private static void disruptorTest() {
        LongEventFactory factory = new LongEventFactory();
        int ringBufferSize = 1024 * 1024; // 创建bufferSize ,也就是RingBuffer大小，必须是2的N次方
        // BlockingWaitStrategy 是最低效的策略，但其对CPU的消耗最小并且在各种不同部署环境中能提供更加一致的性能表现
        // WaitStrategy BLOCKING_WAIT = new BlockingWaitStrategy();
        // SleepingWaitStrategy 的性能表现跟BlockingWaitStrategy差不多，对CPU的消耗也类似，但其对生产者线程的影响最小，适合用于异步日志类似的场景
        // WaitStrategy SLEEPING_WAIT = new SleepingWaitStrategy();
        // YieldingWaitStrategy 的性能是最好的，适合用于低延迟的系统。在要求极高性能且事件处理线数小于CPU逻辑核心数的场景中，推荐使用此策略；例如，CPU开启超线程的特性
        // WaitStrategy YIELDING_WAIT = new YieldingWaitStrategy();
        //创建disruptor
        Disruptor<LongEvent> disruptor =
                new Disruptor<>(factory, ringBufferSize, new ThreadFactory() {
                    private int counter = 0;
                    private String prefix = "DisruptorWorker";

                    @Override
                    public Thread newThread(Runnable r) {
                        Thread t = new Thread(r, prefix + "-" + counter++);
                        t.setDaemon(true);
                        return t;
                    }
                }, ProducerType.SINGLE, new YieldingWaitStrategy());
        // 连接消费事件方法
        disruptor.handleEventsWith(new LongEventHandler());
        // 启动
        disruptor.start();
        // 发布事件
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        LongEventProducerWithTranslator producer = new LongEventProducerWithTranslator(ringBuffer);
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        long begin = System.currentTimeMillis();
        for (long l = 0; l < 50000000; l++) {
            byteBuffer.putLong(0, l);
            producer.enqueue(byteBuffer);
        }
        long end = System.currentTimeMillis();
        System.out.println(50000000 / (end - begin) * 1000);
        disruptor.shutdown();//关闭 disruptor，方法会堵塞，直至所有的事件都得到处理；
    }
}
