package net.goodbai.journaler.execution

import java.util.concurrent.BlockingDeque
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class TaskExecutor private constructor(
        corePoolSize: Int,
        maximumPoolSize: Int,
        workQueue: BlockingDeque<Runnable>?
): ThreadPoolExecutor(
        corePoolSize,maximumPoolSize, 0L, TimeUnit.MILLISECONDS, workQueue
) {
    companion object {
        fun getInstance(capacity: Int): TaskExecutor {
            return TaskExecutor(
                    capacity,
                    capacity * 2,
                    LinkedBlockingDeque<Runnable>()
            )
        }
    }
}
