package tk.lightweightcoding.jenkins.bitbucket.approval;

import com.cloudbees.jenkins.plugins.bitbucket.BitbucketSCMSourceContext;
import jenkins.scm.api.trait.SCMSourceContext;
import org.junit.Test;
import tk.lightweightcoding.jenkins.bitbucket.approval.filters.AnyApprovalSCMHeadFilter;
import tk.lightweightcoding.jenkins.bitbucket.approval.filters.NonAuthorApprovalSCMHeadFilter;

import static org.junit.Assert.assertEquals;


public class ApprovedPullRequestTraitTest {

    @Test
    public void testWhenNoAuthorFilterProvidedWhenRequested() {
        ApprovedPullRequestTrait trait = new ApprovedPullRequestTrait(2);
        SCMSourceContext fakeContext = new BitbucketSCMSourceContext(null, null);

        trait.decorateContext(fakeContext);

        assertEquals(1, fakeContext.filters().size());
        assertEquals(AnyApprovalSCMHeadFilter.class, fakeContext.filters().get(0).getClass());
    }

    @Test
    public void testWhenAuthorFilterProvidedWhenRequested() {
        ApprovedPullRequestTrait trait = new ApprovedPullRequestTrait(3);
        SCMSourceContext fakeContext = new BitbucketSCMSourceContext(null, null);

        trait.decorateContext(fakeContext);

        assertEquals(1, fakeContext.filters().size());
        assertEquals(NonAuthorApprovalSCMHeadFilter.class, fakeContext.filters().get(0).getClass());
    }

}
