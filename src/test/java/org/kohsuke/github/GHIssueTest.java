package org.kohsuke.github;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;

/**
 * Tests corresponding to the <a href="https://github.com/hub4j/github-api/issues/1218">Issue#1218</a>.
 *
 * @author <a href="adir://employees/2700805533">Jonathan Chang</a>
 */
public class GHIssueTest extends AbstractGitHubWireMockTest {

    private GHRepository repo;

    /**
     * set up the configuration and create the testing repository.
     * CS427 Issue link: https://github.com/hub4j/github-api/issues/1218
     *
     * @throws Exception
     *             thrown if the repository is failed to be created.
     */
    @Before
    public void setUp() throws Exception {
        repo = getTempRepository("Temp_GHIssueTest");
    }

    /**
     * Test whether we can get the testing repository.
     * CS427 Issue link: https://github.com/hub4j/github-api/issues/1218
     *
     * @throws Exception
     *             thrown if the testing repository cannot be queried.
     */
    @Test
    public void testGetRepository() throws Exception {
        GHRepository testRepo = gitHub.getRepositoryById(repo.getId());
        assertThat(testRepo.getName(), equalTo(repo.getName()));
    }

    /**
     * Test whether we can create a GitHub issue for the testing repository.
     * CS427 Issue link: https://github.com/hub4j/github-api/issues/1218
     *
     * @throws Exception
     *             thrown if the new issue cannot be created or queried.
     */
    @Test
    public void testCreateIssue() throws Exception {
        // start with no issues
        assertThat(repo.getIssues(GHIssueState.ALL).size(), equalTo(0));

        // create an issue
        repo.createIssue("testIssue#1").create();
        assertThat(repo.getIssues(GHIssueState.ALL).size(), equalTo(1));
        assertThat(repo.getIssues(GHIssueState.ALL).get(0).getTitle(), equalTo("testIssue#1"));
    }

    /**
     * Test whether we can create a commit for a new GitHub issue for the testing repository.
     * CS427 Issue link: https://github.com/hub4j/github-api/issues/1218
     *
     * @throws Exception
     *             thrown if the new comment for the dedicated issue cannot be created.
     */
    @Test
    public void testCreateIssueComment() throws Exception {
        // create an issue comment
        GHIssue issue = repo.createIssue("testIssue#1").create();
        issue.comment("testIssue#1Comment#1");

        assertThat(issue.getComments().size(), equalTo(1));
        assertThat(issue.getComments().get(0).getBody(), equalTo("testIssue#1Comment#1"));
    }

    /**
     * Test whether we can update a commit for a new GitHub issue for the testing repository.
     * CS427 Issue link: https://github.com/hub4j/github-api/issues/1218
     *
     * @throws Exception
     *             thrown if the new comment for the dedicated issue cannot be updated.
     */
    @Test
    public void testUpdateIssueComment() throws Exception {
        // create an issue comment
        GHIssue issue = repo.createIssue("testIssue#1").create();
        issue.comment("testIssue#1Comment#1");

        // update the issue comment
        GHIssueSearchBuilder builder = repo.getRoot()
                .searchIssues()
                .isOpen()
                .q("testIssue#1")
                .q("repo:" + repo.getFullName());
        GHIssue searchedIssue = builder.list().toList().get(0);
        List<GHIssueComment> issueCommentList = searchedIssue.getComments();
        assertThat(issueCommentList.size(), equalTo(1));
        GHIssueComment issueComment = issueCommentList.get(0);
        issueComment.update("testIssue#1Comment#2");

        // assert that issue comment is update-to-date
        assertThat(issue.getComments().size(), equalTo(1));
        assertThat(issue.getComments().get(0).getBody(), equalTo("testIssue#1Comment#2"));
    }

}
