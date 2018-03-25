package tk.lightweightcoding.jenkins.bitbucket.approval.filters;

import com.cloudbees.jenkins.plugins.bitbucket.api.BitbucketPullRequest;
import com.cloudbees.jenkins.plugins.bitbucket.api.BitbucketReviewer;

public class AnyApprovalSCMHeadFilter extends BaseApprovalFilter {
    @Override
    protected boolean additionalFiltering(BitbucketReviewer reviewer, BitbucketPullRequest pullRequest) {
        //no additional filtering required.
        return true;
    }
}
