import org.jahia.services.SpringContextSingleton;
import org.jahia.services.history.ContentHistoryService;

/**
 * Created by IntelliJ IDEA.
 * User: loom
 * Date: Oct 8, 2010
 * Time: 10:01:53 AM
 * To change this template use File | Settings | File Templates.
 */

if (SpringContextSingleton.getInstance() != null) {
  ContentHistoryService contentHistoryService = (ContentHistoryService) SpringContextSingleton.getInstance().getBean("ContentHistoryService");
  if (contentHistoryService != null) {
    Calendar calendar = Calendar.getInstance();
    // This removes all history older than the day before.
    //calendar.add(Calendar.YEAR, -1);
    calendar.add(Calendar.HOUR_OF_DAY, -1);
    contentHistoryService.deleteHistoryBeforeDate(calendar.getTime());
  }
}