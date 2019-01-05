import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.makzyt.github_metrics.Main;
import pl.makzyt.github_metrics.util.SonarProjectLoader;

import java.io.IOException;
import java.security.InvalidKeyException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Main.class})
public class ProjectLoaderTest {
    @Test
    public void ProjectLoaderSuccess() {
        SonarProjectLoader loader = new SonarProjectLoader();

        try {
            loader.loadProject("github_metrics");
        } catch (InvalidKeyException | IOException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(loader.loaded());
    }

    @Test
    public void ProjectLoaderFailureTest() {
        SonarProjectLoader loader = new SonarProjectLoader();

        try {
            loader.loadProject("gfsddsfsdfsdf");
        } catch (InvalidKeyException | IOException e) {
            e.printStackTrace();
        }

        Assert.assertFalse(loader.loaded());
    }
}
