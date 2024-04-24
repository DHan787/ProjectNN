package edu.neu.projnn.projectnn.Service;

import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ConcurrencyService {
    private final AtomicInteger activeSessions = new AtomicInteger(0);
    private final Queue<String> waitingQueue = new LinkedList<>();

    public synchronized boolean requestAccess(String sessionId) {
        if (activeSessions.get() == 0) {
            activeSessions.incrementAndGet();
            return true;  // 直接访问
        } else {
            waitingQueue.add(sessionId);
            return false;  // 需要等待
        }
    }

    public synchronized void releaseAccess() {
        if (!waitingQueue.isEmpty()) {
            waitingQueue.poll();  // 移除队列中的一个等待用户
        }
        activeSessions.decrementAndGet();
    }

    public synchronized int getQueueSize() {
        return waitingQueue.size();  // 返回队列长度
    }
}
