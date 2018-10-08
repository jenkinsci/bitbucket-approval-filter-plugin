package tk.lightweightcoding.jenkins.bitbucket.approval.filters;

import com.cloudbees.jenkins.plugins.bitbucket.api.BitbucketPullRequest;
import com.cloudbees.jenkins.plugins.bitbucket.api.BitbucketReviewer;

public class NonAuthorApprovalSCMHeadFilter extends BaseApprovalFilter {
    @Override
    protected boolean additionalFiltering(BitbucketReviewer reviewer, BitbucketPullRequest pullRequest) {
        return !reviewer.getUser().getUsername().equalsIgnoreCase(pullRequest.getAuthorLogin());
    }
}
