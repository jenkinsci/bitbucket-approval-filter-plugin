package tk.lightweightcoding.jenkins.bitbucket.approval.filters;

import com.cloudbees.jenkins.plugins.bitbucket.BitbucketSCMSourceRequest;
import com.cloudbees.jenkins.plugins.bitbucket.api.BitbucketReviewer;
import jenkins.scm.api.SCMHead;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.Collections;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static tk.lightweightcoding.jenkins.bitbucket.approval.TestUtils.getMockRequest;
import static tk.lightweightcoding.jenkins.bitbucket.approval.TestUtils.getMockSCMHead;

@RunWith(PowerMockRunner.class)
@PrepareForTest(value = BitbucketSCMSourceRequest.class)
public class AuthorAndNonAuthorApprovalSCMHeadFilterTest {

    @Test
    public void testFilteredWhenNoApprovals() throws Exception {
        BitbucketSCMSourceRequest mockRequest = getMockRequest(Collections.emptyList());
        SCMHead mockHead = getMockSCMHead();

        AuthorAndNonAuthorApprovalSCMHeadFilter filter = new AuthorAndNonAuthorApprovalSCMHeadFilter();

        boolean result = filter.isExcluded(mockRequest, mockHead);

        assertTrue(result);
    }

    @Test
    public void testFilteredWhenOnlyAuthor() throws Exception {
        BitbucketReviewer reviewer = mock(BitbucketReviewer.class);
        BitbucketReviewer.User mockUser = mock(BitbucketReviewer.User.class);
        doReturn(mockUser).when(reviewer).getUser();
        doReturn("Author").when(mockUser).getIdentifier();
        doReturn(true).when(reviewer).getApproved();

        BitbucketSCMSourceRequest mockRequest = getMockRequest(Collections.singletonList(reviewer));
        SCMHead mockHead = getMockSCMHead();

        AuthorAndNonAuthorApprovalSCMHeadFilter filter = new AuthorAndNonAuthorApprovalSCMHeadFilter();

        boolean result = filter.isExcluded(mockRequest, mockHead);

        assertTrue(result);
    }

    @Test
    public void testFilteredWhenOnlyNonAuthor() throws Exception {
        BitbucketReviewer reviewer = mock(BitbucketReviewer.class);
        BitbucketReviewer.User mockUser = mock(BitbucketReviewer.User.class);
        doReturn(mockUser).when(reviewer).getUser();
        doReturn("NonAuthor").when(mockUser).getIdentifier();
        doReturn(true).when(reviewer).getApproved();

        BitbucketSCMSourceRequest mockRequest = getMockRequest(Collections.singletonList(reviewer));
        SCMHead mockHead = getMockSCMHead();

        AuthorAndNonAuthorApprovalSCMHeadFilter filter = new AuthorAndNonAuthorApprovalSCMHeadFilter();

        boolean result = filter.isExcluded(mockRequest, mockHead);

        assertTrue(result);
    }

    @Test
    public void testNotFilteredWhenAuthorAndNonAuthor() throws Exception {
        BitbucketReviewer authorReviewer = mock(BitbucketReviewer.class);
        BitbucketReviewer.User authorUser = mock(BitbucketReviewer.User.class);
        doReturn(authorUser).when(authorReviewer).getUser();
        doReturn("Author").when(authorUser).getIdentifier();
        doReturn(true).when(authorReviewer).getApproved();

        BitbucketReviewer nonAuthorReviewer = mock(BitbucketReviewer.class);
        BitbucketReviewer.User nonAuthorUser = mock(BitbucketReviewer.User.class);
        doReturn(nonAuthorUser).when(nonAuthorReviewer).getUser();
        doReturn("NonAuthor").when(nonAuthorUser).getIdentifier();
        doReturn(true).when(nonAuthorReviewer).getApproved();

        BitbucketSCMSourceRequest mockRequest = getMockRequest(Arrays.asList(authorReviewer, nonAuthorReviewer));
        SCMHead mockHead = getMockSCMHead();

        AuthorAndNonAuthorApprovalSCMHeadFilter filter = new AuthorAndNonAuthorApprovalSCMHeadFilter();

        boolean result = filter.isExcluded(mockRequest, mockHead);

        assertFalse(result);
    }
}
