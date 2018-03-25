package tk.lightweightcoding.jenkins.bitbucket.approval.filters;

import com.cloudbees.jenkins.plugins.bitbucket.BitbucketSCMSourceRequest;
import com.cloudbees.jenkins.plugins.bitbucket.api.BitbucketReviewer;
import jenkins.scm.api.SCMHead;
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
public class NonAuthorApprovalSCMHeadFilterTest {

    @Test
    public void testFilteredWhenApprovalIsAuthor() throws Exception {
        BitbucketReviewer reviewer = mock(BitbucketReviewer.class);
        doReturn("Author").when(reviewer).getReviewerLogin();
        doReturn(true).when(reviewer).getApproved();

        BitbucketSCMSourceRequest mockRequest = getMockRequest(Collections.singletonList(reviewer));
        SCMHead mockHead = getMockSCMHead();

        NonAuthorApprovalSCMHeadFilter filter = new NonAuthorApprovalSCMHeadFilter();

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

        NonAuthorApprovalSCMHeadFilter filter = new NonAuthorApprovalSCMHeadFilter();

        boolean result = filter.isExcluded(mockRequest, mockHead);

        assertFalse(result);
    }
}
