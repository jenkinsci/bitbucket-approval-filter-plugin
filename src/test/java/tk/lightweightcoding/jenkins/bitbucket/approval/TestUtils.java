package tk.lightweightcoding.jenkins.bitbucket.approval;

import com.cloudbees.jenkins.plugins.bitbucket.BitbucketSCMSourceRequest;
import com.cloudbees.jenkins.plugins.bitbucket.PullRequestSCMHead;
import com.cloudbees.jenkins.plugins.bitbucket.api.BitbucketBranch;
import com.cloudbees.jenkins.plugins.bitbucket.api.BitbucketPullRequest;
import com.cloudbees.jenkins.plugins.bitbucket.api.BitbucketPullRequestSource;
import com.cloudbees.jenkins.plugins.bitbucket.api.BitbucketReviewer;
import com.cloudbees.jenkins.plugins.bitbucket.client.branch.BitbucketCloudBranch;
import jenkins.scm.api.SCMHead;
import org.powermock.api.mockito.PowerMockito;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class TestUtils {
    public static BitbucketSCMSourceRequest getMockRequest() throws Exception {
        return getMockRequest(Collections.emptyList());
    }

    public static BitbucketSCMSourceRequest getMockRequest(Collection<BitbucketReviewer> reviewers) throws Exception {

        BitbucketSCMSourceRequest request = PowerMockito.mock(BitbucketSCMSourceRequest.class);
        List<BitbucketPullRequest> prs = new ArrayList<>();
        BitbucketPullRequest pr = mock(BitbucketPullRequest.class);
        prs.add(pr);
        doReturn(prs).when(request).getPullRequests();

        BitbucketPullRequestSource source = mock(BitbucketPullRequestSource.class);
        doReturn(source).when(pr).getSource();
        doReturn("1").when(pr).getId();
        doReturn("Author").when(pr).getAuthorIdentifier();

        BitbucketBranch branch = new BitbucketCloudBranch("Test","123", 1L);
        doReturn(branch).when(source).getBranch();

        BitbucketPullRequest full = mock(BitbucketPullRequest.class);
        doReturn(full).when(request).getPullRequestById(any(Integer.class));
        doReturn(reviewers).when(full).getReviewers();

        return request;
    }

    public static SCMHead getMockSCMHead() {
        PullRequestSCMHead prHead = mock(PullRequestSCMHead.class);
        doReturn("Test").when(prHead).getBranchName();

        return prHead;
    }
}
