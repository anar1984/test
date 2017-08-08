/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import job.CompanyJob;
import org.quartz.CronScheduleBuilder;
import static org.quartz.JobBuilder.newJob;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import static org.quartz.TriggerBuilder.newTrigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author nikli
 */
@WebListener
public class JobListener implements ServletContextListener {

    Scheduler scheduler = null;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Context Initialized");

        /*try {
            // Setup the Job class and the Job group
            JobDetail job = newJob(CompanyJob.class).withIdentity(
                    "CompanyCreatorJob", "APDVoice").build();

            // Create a Trigger that fires every 5 minutes.
            Trigger trigger = newTrigger()
                    .withIdentity("TriggerName", "Group")
                    .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
                    .build();

            // Setup the Job and Trigger with Scheduler & schedule jobs
            scheduler = new StdSchedulerFactory().getScheduler();//"quartz.properties"
            scheduler.start();
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Context Destroyed");
        /*try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }*/
    }

}
