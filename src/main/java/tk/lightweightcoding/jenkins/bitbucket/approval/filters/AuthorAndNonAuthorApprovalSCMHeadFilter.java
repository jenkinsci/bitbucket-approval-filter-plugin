package tk.lightweightcoding.jenkins.bitbucket.approval.filters;

import com.cloudbees.jenkins.plugins.bitbucket.api.BitbucketPullRequest;
import com.cloudbees.jenkins.plugins.bitbucket.api.BitbucketReviewer;
import tk.lightweightcoding.jenkins.bitbucket.approval.utils.BitbucketReviewerUtils;

import java.util.Collection;

public class AuthorAndNonAuthorApprovalSCMHeadFilter extends BaseApprovalFilter {

    @Override
    protected boolean isPullRequestProperlyApproved(BitbucketPullRequest pull, Collection<BitbucketReviewer> reviewers) {
        return BitbucketReviewerUtils.hasAuthorApproval(reviewers, pull) && BitbucketReviewerUtils.hasNonAuthorApproval(reviewers, pull);
    }
}
