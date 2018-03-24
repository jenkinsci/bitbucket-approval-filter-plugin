package tk.lightweightcoding.jenkins.bitbucket.approval;

import com.cloudbees.jenkins.plugins.bitbucket.BitbucketSCMSourceContext;
import com.cloudbees.jenkins.plugins.bitbucket.BranchSCMHead;
import jenkins.scm.api.trait.SCMSourceContext;
import jenkins.scm.api.trait.SCMSourceRequest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;


public class ApprovedPullRequestTraitTest {

    @Test
    public void testWhenNoAuthorFilterProvidedWhenRequested() {
        ApprovedPullRequestTrait trait = new ApprovedPullRequestTrait(2);
        SCMSourceContext fakeContext = new BitbucketSCMSourceContext(null, null);

        trait.decorateContext(fakeContext);

        assertEquals(1, fakeContext.filters().size());
        ApprovedPullRequestTrait.AnyApprovalSCMHeadFilter filter = (ApprovedPullRequestTrait.AnyApprovalSCMHeadFilter)fakeContext.filters().get(0);
        assertEquals(false, filter.isRequireNonAuthor());
    }

    @Test
    public void testWhenAuthorFilterProvidedWhenRequested() {
        ApprovedPullRequestTrait trait = new ApprovedPullRequestTrait(3);
        SCMSourceContext fakeContext = new BitbucketSCMSourceContext(null, null);

        trait.decorateContext(fakeContext);

        assertEquals(1, fakeContext.filters().size());
        ApprovedPullRequestTrait.AnyApprovalSCMHeadFilter filter = (ApprovedPullRequestTrait.AnyApprovalSCMHeadFilter)fakeContext.filters().get(0);
        assertEquals(true, filter.isRequireNonAuthor());
    }

    @Test
    public void testNonPullRequestsReturnFalse() throws Exception {
        ApprovedPullRequestTrait.AnyApprovalSCMHeadFilter filter = new ApprovedPullRequestTrait.AnyApprovalSCMHeadFilter(false);

        boolean result = filter.isExcluded(mock(SCMSourceRequest.class), mock(BranchSCMHead.class));

        assertFalse(result);
    }

}
