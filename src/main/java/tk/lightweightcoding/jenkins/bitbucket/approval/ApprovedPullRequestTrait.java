package tk.lightweightcoding.jenkins.bitbucket.approval;

import com.cloudbees.jenkins.plugins.bitbucket.BitbucketSCMSourceRequest;
import com.cloudbees.jenkins.plugins.bitbucket.PullRequestSCMHead;
import com.cloudbees.jenkins.plugins.bitbucket.api.*;
import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.util.ListBoxModel;
import jenkins.scm.api.SCMHead;
import jenkins.scm.api.SCMHeadCategory;
import jenkins.scm.api.trait.*;
import jenkins.scm.impl.trait.Discovery;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;

public class ApprovedPullRequestTrait extends SCMSourceTrait {

    private int strategyId;

    /**
     * Constructor.
     */
    @DataBoundConstructor
    public ApprovedPullRequestTrait(String strategyId) {
        this.strategyId = Integer.parseInt(strategyId);
    }

    @SuppressWarnings("unused") // used by Jelly EL
    public int getStrategyId() {
        return this.strategyId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void decorateContext(SCMSourceContext<?, ?> context) {
        if(strategyId == 2) {
            context.withFilter(new ApprovedPullRequestTrait.AnyApprovalSCMHeadFilter(false));
        } else if(strategyId == 3) {
            context.withFilter(new ApprovedPullRequestTrait.AnyApprovalSCMHeadFilter(true));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean includeCategory(@NonNull SCMHeadCategory category) {
        return category.isUncategorized();
    }

    @Extension
    @Discovery
    public static class DescriptorImpl extends SCMSourceTraitDescriptor {

        /**
         * {@inheritDoc}
         */
        @Override
        public String getDisplayName() {
            return "Require approvals on pull request";
        }

        /**
         * Populates the strategy options.
         *
         * @return the strategy options.
         */
        @NonNull
        @Restricted(NoExternalUse.class)
        @SuppressWarnings("unused") // stapler
        public ListBoxModel doFillStrategyIdItems() {
            ListBoxModel result = new ListBoxModel();
            result.add("No approval necessary.", "1");
            result.add("An approval required.", "2");
            result.add("Non-author approval required.", "3");
            return result;
        }
    }

    public static class AnyApprovalSCMHeadFilter extends SCMHeadFilter {

        private final boolean requireNonAuthor;

        AnyApprovalSCMHeadFilter(boolean requireNonAuthor) {
            this.requireNonAuthor = requireNonAuthor;
        }

        @Override
        public boolean isExcluded(@NonNull SCMSourceRequest scmSourceRequest, @NonNull SCMHead scmHead) throws IOException, InterruptedException {
            if (scmHead instanceof PullRequestSCMHead) {
                BitbucketApi api = ((BitbucketSCMSourceRequest) scmSourceRequest).getApi();
                for (BitbucketPullRequest pull : ((BitbucketSCMSourceRequest) scmSourceRequest).getPullRequests()) {
                    if (pull.getSource().getBranch().getName().equals(((PullRequestSCMHead) scmHead).getBranchName())) {
                        boolean hasApproval = false;
                        BitbucketPullRequestFull fullPullRequest = api.getPullRequestById(Integer.parseInt(pull.getId()));
                        for (BitbucketReviewer reviewer : fullPullRequest.getReviewers()) {

                            if(requireNonAuthor && reviewer.getReviewerLogin().equalsIgnoreCase(pull.getAuthorLogin())) {
                                continue;
                            }

                            hasApproval = hasApproval || reviewer.getApproved();
                        }
                        System.out.println("Skip this pull request: "+!hasApproval);
                        return !hasApproval;
                    }
                }
            }

            return false;
        }
    }
}