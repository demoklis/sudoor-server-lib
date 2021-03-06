package net.gplatform.sudoor.server.test.it;

import javax.mail.MessagingException;

import net.gplatform.sudoor.server.Application;
import net.gplatform.sudoor.server.mail.MailClient;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MailClientTest {

	@Autowired
	MailClient mailClient;

	@Test
	public void test() throws MessagingException {
		try {
			mailClient.send("xufucheng@vcredit.com", "我");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
