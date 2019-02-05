package tk.lightweightcoding.jenkins.bitbucket.approval.filters;

import com.cloudbees.jenkins.plugins.bitbucket.BitbucketSCMSourceRequest;
import com.cloudbees.jenkins.plugins.bitbucket.BranchSCMHead;
import com.cloudbees.jenkins.plugins.bitbucket.api.BitbucketReviewer;
import jenkins.scm.api.SCMHead;
import jenkins.scm.api.trait.SCMSourceRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collections;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static tk.lightweightcoding.jenkins.bitbucket.approval.TestUtils.getMockRequest;
import static tk.lightweightcoding.jenkins.bitbucket.approval.TestUtils.getMockSCMHead;

@RunWith(PowerMockRunner.class)
@PrepareForTest(value = BitbucketSCMSourceRequest.class)
public class AnyApprovalSCMHeadFilterTest {

    @Test
    public void testNonPullRequestsReturnFalse() throws Exception {
        AnyApprovalSCMHeadFilter filter = new AnyApprovalSCMHeadFilter();

        boolean result = filter.isExcluded(mock(SCMSourceRequest.class), mock(BranchSCMHead.class));

        assertFalse(result);
    }

    @Test
    public void testFilteredWhenNoApproval() throws Exception {
        BitbucketSCMSourceRequest mockRequest = getMockRequest();
        SCMHead mockHead = getMockSCMHead();

        AnyApprovalSCMHeadFilter filter = new AnyApprovalSCMHeadFilter();

        boolean result = filter.isExcluded(mockRequest, mockHead);

        assertTrue(result);
    }

    @Test
    public void testFilteredWhenReviewerNotApproved() throws Exception {
        BitbucketReviewer reviewer = mock(BitbucketReviewer.class);
        doReturn(false).when(reviewer).getApproved();

        BitbucketSCMSourceRequest mockRequest = getMockRequest(Collections.singletonList(reviewer));
        SCMHead mockHead = getMockSCMHead();


        AnyApprovalSCMHeadFilter filter = new AnyApprovalSCMHeadFilter();

        boolean result = filter.isExcluded(mockRequest, mockHead);

        assertTrue(result);
    }

    @Test
    public void testNotFilteredWhenReviewerIsApproved() throws Exception {
        BitbucketReviewer reviewer = mock(BitbucketReviewer.class);
        BitbucketReviewer.User mockUser = mock(BitbucketReviewer.User.class);
        doReturn(mockUser).when(reviewer).getUser();
        doReturn("Author").when(mockUser).getIdentifier();
        doReturn(true).when(reviewer).getApproved();

        BitbucketSCMSourceRequest mockRequest = getMockRequest(Collections.singletonList(reviewer));
        SCMHead mockHead = getMockSCMHead();


        AnyApprovalSCMHeadFilter filter = new AnyApprovalSCMHeadFilter();

        boolean result = filter.isExcluded(mockRequest, mockHead);

        assertFalse(result);
    }
}
