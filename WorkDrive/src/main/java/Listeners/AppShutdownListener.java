package Listeners;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

public class AppShutdownListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
    }

    public void contextDestroyed(ServletContextEvent sce) {
        try {
            AbandonedConnectionCleanupThread.checkedShutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}