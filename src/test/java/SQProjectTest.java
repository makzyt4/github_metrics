import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.makzyt.github_metrics.Main;
import pl.makzyt.github_metrics.util.sonarqube.SQAnalysis;
import pl.makzyt.github_metrics.util.sonarqube.SQProject;
import pl.makzyt.github_metrics.util.sonarqube.SonarQube;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Main.class})
public class SQProjectTest {
    @Test
    public void sqProjectListTest() throws IOException {
        SonarQube sq = new SonarQube();
        sq.setHost("http://localhost:9000");
        sq.login("admin", "admin");

        Assert.assertTrue(sq.privileged());

        HashMap<String, SQProject> projects = sq.getProjects();

        List<String> projectKeys = new ArrayList<>(projects.keySet());

        Assert.assertTrue(projectKeys.contains("github_metrics"));
    }

    @Test
    public void sqProjectAnalysisTest() throws IOException {
        SonarQube sq = new SonarQube();
        sq.setHost("http://localhost:9000");
        sq.login("admin", "admin");

        Assert.assertTrue(sq.privileged());

        HashMap<String, SQProject> projects = sq.getProjects();

        Assert.assertNotNull(projects);

        SQProject projectGHMetrics = projects.get("github_metrics");

        SQAnalysis analysis = projectGHMetrics.getAnalysis();

        Assert.assertEquals(2.8, analysis.getClassComplexity(), 0.1);
        Assert.assertEquals(3, analysis.getBugs());
    }
}
