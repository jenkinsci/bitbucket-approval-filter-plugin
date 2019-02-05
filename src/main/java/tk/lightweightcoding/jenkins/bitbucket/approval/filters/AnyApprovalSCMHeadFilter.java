package tk.lightweightcoding.jenkins.bitbucket.approval.filters;

import com.cloudbees.jenkins.plugins.bitbucket.api.BitbucketPullRequest;
import com.cloudbees.jenkins.plugins.bitbucket.api.BitbucketReviewer;
import tk.lightweightcoding.jenkins.bitbucket.approval.utils.BitbucketReviewerUtils;

import java.util.Collection;

public class AnyApprovalSCMHeadFilter extends BaseApprovalFilter {

    @Override
    protected boolean isPullRequestProperlyApproved(BitbucketPullRequest pullRequest, Collection<BitbucketReviewer> reviewers) {
        return BitbucketReviewerUtils.hasNonAuthorApproval(reviewers, pullRequest) || BitbucketReviewerUtils.hasAuthorApproval(reviewers, pullRequest);
    }
}
