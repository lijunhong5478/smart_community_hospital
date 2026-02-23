import com.tyut.Application;
import com.tyut.service.ScheduleTaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Application.class)
public class TestSchedule {
    @Autowired
    private ScheduleTaskService scheduleTaskService;
    @Test
    public void testResetWeeklyScheduleNumbers(){
        scheduleTaskService.resetWeeklyScheduleNumbers();
    }
    @Test
    public void testResetStatus(){
        scheduleTaskService.resetScheduleStatus();
    }
}
