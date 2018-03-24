package tk.lightweightcoding.jenkins.bitbucket.approval;

import com.cloudbees.jenkins.plugins.bitbucket.BitbucketSCMSourceRequest;
import com.cloudbees.jenkins.plugins.bitbucket.PullRequestSCMHead;
import com.cloudbees.jenkins.plugins.bitbucket.api.BitbucketBranch;
import com.cloudbees.jenkins.plugins.bitbucket.api.BitbucketPullRequest;
import com.cloudbees.jenkins.plugins.bitbucket.api.BitbucketPullRequestFull;
import com.cloudbees.jenkins.plugins.bitbucket.api.BitbucketPullRequestSource;
import com.cloudbees.jenkins.plugins.bitbucket.api.BitbucketReviewer;
import com.cloudbees.jenkins.plugins.bitbucket.client.branch.BitbucketCloudBranch;
import jenkins.scm.api.SCMHead;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest(value = BitbucketSCMSourceRequest.class)
public class ApprovedPullRequestTraitTestPowerMock {

    @Test
    public void testFilteredWhenNoApproval() throws Exception {
        BitbucketSCMSourceRequest mockRequest = getMockRequest();
        SCMHead mockHead = getMockSCMHead();

        ApprovedPullRequestTrait.AnyApprovalSCMHeadFilter filter = new ApprovedPullRequestTrait.AnyApprovalSCMHeadFilter(false);

        boolean result = filter.isExcluded(mockRequest, mockHead);

        assertTrue(result);
    }

    @Test
    public void testFilteredWhenReviewerNotApproved() throws Exception {
        BitbucketReviewer reviewer = mock(BitbucketReviewer.class);
        doReturn(false).when(reviewer).getApproved();

        BitbucketSCMSourceRequest mockRequest = getMockRequest(Collections.singletonList(reviewer));
        SCMHead mockHead = getMockSCMHead();


        ApprovedPullRequestTrait.AnyApprovalSCMHeadFilter filter = new ApprovedPullRequestTrait.AnyApprovalSCMHeadFilter(false);

        boolean result = filter.isExcluded(mockRequest, mockHead);

        assertTrue(result);
    }

    @Test
    public void testNotFilteredWhenReviewerIsApproved() throws Exception {
        BitbucketReviewer reviewer = mock(BitbucketReviewer.class);
        doReturn(true).when(reviewer).getApproved();

        BitbucketSCMSourceRequest mockRequest = getMockRequest(Collections.singletonList(reviewer));
        SCMHead mockHead = getMockSCMHead();


        ApprovedPullRequestTrait.AnyApprovalSCMHeadFilter filter = new ApprovedPullRequestTrait.AnyApprovalSCMHeadFilter(false);

        boolean result = filter.isExcluded(mockRequest, mockHead);

        assertFalse(result);
    }

    @Test
    public void testFilteredWhenApprovalIsAuthor() throws Exception {
        BitbucketReviewer reviewer = mock(BitbucketReviewer.class);
        doReturn("Author").when(reviewer).getReviewerLogin();
        doReturn(true).when(reviewer).getApproved();

        BitbucketSCMSourceRequest mockRequest = getMockRequest(Collections.singletonList(reviewer));
        SCMHead mockHead = getMockSCMHead();


        ApprovedPullRequestTrait.AnyApprovalSCMHeadFilter filter = new ApprovedPullRequestTrait.AnyApprovalSCMHeadFilter(true);

        boolean result = filter.isExcluded(mockRequest, mockHead);

        assertTrue(result);
    }

    @Test
    public void testNotFilteredWhenApprovalIsNotAuthor() throws Exception {
        BitbucketReviewer reviewer = mock(BitbucketReviewer.class);
        doReturn("NotAuthor").when(reviewer).getReviewerLogin();
        doReturn(true).when(reviewer).getApproved();

        BitbucketSCMSourceRequest mockRequest = getMockRequest(Collections.singletonList(reviewer));
        SCMHead mockHead = getMockSCMHead();


        ApprovedPullRequestTrait.AnyApprovalSCMHeadFilter filter = new ApprovedPullRequestTrait.AnyApprovalSCMHeadFilter(true);

        boolean result = filter.isExcluded(mockRequest, mockHead);

        assertFalse(result);
    }

    private BitbucketSCMSourceRequest getMockRequest() throws Exception {
        return getMockRequest(Collections.<BitbucketReviewer>emptyList());
    }

    private BitbucketSCMSourceRequest getMockRequest(Collection<BitbucketReviewer> reviewers) throws Exception {

        BitbucketSCMSourceRequest request = PowerMockito.mock(BitbucketSCMSourceRequest.class);
        List<BitbucketPullRequest> prs = new ArrayList<>();
        BitbucketPullRequest pr = mock(BitbucketPullRequest.class);
        prs.add(pr);
        doReturn(prs).when(request).getPullRequests();

        BitbucketPullRequestSource source = mock(BitbucketPullRequestSource.class);
        doReturn(source).when(pr).getSource();
        doReturn("1").when(pr).getId();
        doReturn("Author").when(pr).getAuthorLogin();

        BitbucketBranch branch = new BitbucketCloudBranch("Test","123", 1L);
        doReturn(branch).when(source).getBranch();

        BitbucketPullRequestFull full = mock(BitbucketPullRequestFull.class);
        doReturn(full).when(request).getPullRequestById(any(Integer.class));
        doReturn(reviewers).when(full).getReviewers();

        return request;
    }

    private SCMHead getMockSCMHead() {
        PullRequestSCMHead prHead = mock(PullRequestSCMHead.class);
        doReturn("Test").when(prHead).getBranchName();

        return prHead;
    }

}
