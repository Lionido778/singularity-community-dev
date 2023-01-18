package cn.codeprobe.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.springframework.stereotype.Component;

/**
 * 雪花算法 生成分布式ID (全局唯一)
 *
 * @author Lionido
 */
@Component
public class IdWorker {

    private static final long WORKER_ID = 1L;
    private static final long DATACENTER_ID = 1L;
    private Snowflake snowflake = IdUtil.getSnowflake(WORKER_ID, DATACENTER_ID);


    public synchronized long nextId() {
        return snowflake.nextId();
    }

    public synchronized String nextIdStr() {
        return snowflake.nextIdStr();
    }

    public Snowflake getSnowflake() {
        return snowflake;
    }

    public void setSnowflake(Snowflake snowflake) {
        this.snowflake = snowflake;
    }
}

