import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.makzyt.github_metrics.Main;
import pl.makzyt.github_metrics.util.sonarqube.SonarQube;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Main.class})
public class SonarQubeTest {
    @Test
    public void loginLogoutSuccessTest() throws IOException {
        SonarQube sq = new SonarQube();
        sq.setHost("http://localhost:9000");
        sq.login("admin", "admin");

        Assert.assertTrue(sq.privileged());

        sq.logout();

        Assert.assertFalse(sq.privileged());
    }

    @Test
    public void loginFailureTest() throws IOException {
        SonarQube sq = new SonarQube();
        sq.setHost("http://localhost:9000");
        sq.login("admin", "abc");

        Assert.assertFalse(sq.privileged());
    }

    @Test
    public void loginCredentialsSuccessTest() throws IOException {
        SonarQube sq = new SonarQube();
        sq.setHost("http://localhost:9000");
        sq.setCredentials("admin", "admin");
        sq.login();

        Assert.assertTrue(sq.privileged());
    }

    @Test
    public void loginCredentialsFailureTest() throws IOException {
        SonarQube sq = new SonarQube();
        sq.setHost("http://localhost:9000");
        sq.setCredentials("admin", "abc");
        sq.login();

        Assert.assertFalse(sq.privileged());
    }
}
