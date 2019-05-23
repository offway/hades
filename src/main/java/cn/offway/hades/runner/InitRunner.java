package cn.offway.hades.runner;

import cn.offway.hades.service.PhConfigService;
import cn.offway.hades.singleton.JobHolder;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.*;

@Component
public class InitRunner implements ApplicationRunner {

    @Autowired
    private PhConfigService configService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
//        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
//        JobDetail job = JobBuilder.newJob().build();
//        scheduler.addJob(job, true);
//        scheduler.start();
        ThreadFactory factory = new ThreadFactoryBuilder()
                .setDaemon(true)
                .setNameFormat("Orders-%d")
                .build();
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        String jsonStr = configService.findContentByName("CRONJOB");
        if (jsonStr == null || "".equals(jsonStr.trim())) {
            //no nothing
            logger.info("nothing to do with CRONJOB");
        } else {
            JSONObject jsonObject = JSON.parseObject(jsonStr);
            for (String key : jsonObject.keySet()) {
                String[] args = key.split("_");
                String sTimeStr = args[0];
                String eTimeStr = args[1];
                String goodIds = args[2];
                Date sTime = DateTime.parse(sTimeStr, format).toDate();
                Date eTime = DateTime.parse(eTimeStr, format).toDate();
                Date now = new Date();
                if (sTime.compareTo(now) < 0 && eTime.compareTo(now) < 0) {
                    continue;
                }
                if ("".equals(goodIds.trim())) {
                    continue;
                }
                JSONArray taskList = jsonObject.getJSONArray(key);
                ScheduledExecutorService pool = Executors.newScheduledThreadPool(taskList.size(), factory);
                ScheduledExecutorService poolReverse = Executors.newScheduledThreadPool(taskList.size(), factory);
                for (JSONObject task : taskList.toJavaList(JSONObject.class)) {
                    //calc the delay in seconds
                    long delaySeconds = sTime.getTime() - now.getTime();
                    if (delaySeconds > 0) {
                        pool.schedule(new Callable<Object>() {
                            @Override
                            public Object call() throws Exception {
                                //TODO
                                return null;
                            }
                        }, delaySeconds, TimeUnit.MILLISECONDS);
                    }
                    //reverse job
                    //calc the delay in seconds
                    long delaySecondsReverse = eTime.getTime() - now.getTime();
                    if (delaySecondsReverse > 0) {
                        poolReverse.schedule(new Callable<Object>() {
                            @Override
                            public Object call() throws Exception {
                                //TODO
                                return null;
                            }
                        }, delaySecondsReverse, TimeUnit.MILLISECONDS);
                    }
                }
                //link to global register
                JobHolder.getHolder().put(key, pool);
                JobHolder.getHolder().put(key + "_REVERSE", poolReverse);
            }
        }
    }
}
