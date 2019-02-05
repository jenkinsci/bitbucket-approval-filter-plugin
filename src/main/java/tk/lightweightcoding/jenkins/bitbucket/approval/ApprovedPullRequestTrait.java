package tk.lightweightcoding.jenkins.bitbucket.approval;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.util.ListBoxModel;
import jenkins.scm.api.SCMHeadCategory;
import jenkins.scm.api.trait.*;
import jenkins.scm.impl.trait.Discovery;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;
import org.kohsuke.stapler.DataBoundConstructor;
import tk.lightweightcoding.jenkins.bitbucket.approval.filters.AnyApprovalSCMHeadFilter;
import tk.lightweightcoding.jenkins.bitbucket.approval.filters.AuthorAndNonAuthorApprovalSCMHeadFilter;
import tk.lightweightcoding.jenkins.bitbucket.approval.filters.NonAuthorApprovalSCMHeadFilter;

public class ApprovedPullRequestTrait extends SCMSourceTrait {

    private int strategyId;

    /**
     * Constructor.
     */
    @DataBoundConstructor
    public ApprovedPullRequestTrait(int strategyId) {
        this.strategyId = strategyId;
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
            context.withFilter(new AnyApprovalSCMHeadFilter());
        } else if(strategyId == 3) {
            context.withFilter(new NonAuthorApprovalSCMHeadFilter());
        } else if(strategyId == 4) {
            context.withFilter(new AuthorAndNonAuthorApprovalSCMHeadFilter());
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
            result.add("Any approval required.", "2");
            result.add("Non-author approval required.", "3");
            result.add("Author and Non-author approval required.", "4");
            return result;
        }
    }

}
