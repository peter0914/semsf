package listeners;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import util.DBConnectionPool;
import dao.MysqlCourseDao;
import dao.MysqlSubjectDao;
import dao.MysqlUserDao;

/*
 * ServletContextListener
 *  - 웹 앱의 시작/종료 이벤트 처리
 *  - 구현체 작성 => web.xml 등록
 *  - <listener></listener>
 */
public class ContextLoaderListener implements ServletContextListener {
  DBConnectionPool dbConnectionPool;
  
  @Override
  public void contextDestroyed(ServletContextEvent event) {
    System.out.println("contextDestroyed...");
    dbConnectionPool.closeAll();
  }

  @Override
  public void contextInitialized(ServletContextEvent event) {
    System.out.println("contextInitialized...");
    ServletContext sc = event.getServletContext();
    dbConnectionPool = new DBConnectionPool();
    dbConnectionPool.setDriver(sc.getInitParameter("driver"));
    dbConnectionPool.setUrl(sc.getInitParameter("url"));
    dbConnectionPool.setUsername(sc.getInitParameter("username"));
    dbConnectionPool.setPassword(sc.getInitParameter("password"));
    
    MysqlSubjectDao subjectDao = new MysqlSubjectDao();
    MysqlCourseDao courseDao = new MysqlCourseDao();
    MysqlUserDao userDao = new MysqlUserDao();
    subjectDao.setDBConnectionPool(dbConnectionPool);
    courseDao.setDBConnectionPool(dbConnectionPool);
    userDao.setDBConnectionPool(dbConnectionPool);
    sc.setAttribute("subjectDao", subjectDao);
    sc.setAttribute("courseDao", courseDao);
    sc.setAttribute("userDao", userDao);
  }

}
