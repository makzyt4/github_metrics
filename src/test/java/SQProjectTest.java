import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.makzyt.github_metrics.Main;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Main.class})
public class SQProjectTest {
    @Test
    public void sqProjectListTest() {
        SonarQube sq = new SonarQube();
        sq.setHost("http://localhost:9000");
        sq.login("admin", "admin");

        Assert.assertTrue(sq.authenticated());

        HashMap<String, SQProject> projects = sq.getProjects();

        List<String> projectKeys = new ArrayList<>();

        for (String key : projects.keySet()) {
            projectKeys.add(key);
        }

        Assert.assertTrue(projectKeys.contains("github_metrics"));
    }

    @Test
    public void sqProjectAnalysisTest() {
        SonarQube sq = new SonarQube();
        sq.setHost("http://localhost:9000");
        sq.login("admin", "admin");

        Assert.assertTrue(sq.authenticated());

        HashMap<String, SQProject> projects = sq.getProjects();
        SQProject projectGitHubMetrics = projects.get("github_metrics");

        // May take some time
        SQAnalysis analysis = projectGitHubMetrics.analyse();

        // TODO Some analysis checks
        Assert.assertEquals(100.0, analysis.getCoverage());
        Assert.assertEquals(3, analysis.getNumberOfBugs());
    }
}
