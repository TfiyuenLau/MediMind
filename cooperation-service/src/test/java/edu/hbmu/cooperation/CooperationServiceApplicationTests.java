package edu.hbmu.cooperation;

import edu.hbmu.cooperation.domain.entity.MsgInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CooperationServiceApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testLocalDateTime() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println(LocalDateTime.parse(LocalDateTime.now().format(timeFormatter), timeFormatter));

        // 封装对象
        MsgInfo msgInfo = new MsgInfo();
        msgInfo.setSendTime(LocalDateTime.parse(LocalDateTime.now().format(timeFormatter), timeFormatter));
        System.out.println(msgInfo.getSendTime());
    }

}
