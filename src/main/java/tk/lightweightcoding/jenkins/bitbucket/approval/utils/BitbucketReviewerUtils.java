package tk.lightweightcoding.jenkins.bitbucket.approval.utils;

import com.cloudbees.jenkins.plugins.bitbucket.api.BitbucketPullRequest;
import com.cloudbees.jenkins.plugins.bitbucket.api.BitbucketReviewer;

import java.util.Collection;

public class BitbucketReviewerUtils {
    private static boolean isApprovalAuthor(BitbucketReviewer reviewer, BitbucketPullRequest pullRequest) {
        return reviewer.getUser().getIdentifier().equalsIgnoreCase(pullRequest.getAuthorIdentifier());
    }

    public static boolean hasAuthorApproval(Collection<BitbucketReviewer> reviewers, BitbucketPullRequest pullRequest) {
        boolean hasAuthorApproval = false;

        for (BitbucketReviewer reviewer : reviewers) {
            hasAuthorApproval = hasAuthorApproval || (reviewer.getApproved() && BitbucketReviewerUtils.isApprovalAuthor(reviewer, pullRequest));
        }

        return hasAuthorApproval;
    }

    public static boolean hasNonAuthorApproval(Collection<BitbucketReviewer> reviewers, BitbucketPullRequest pullRequest) {
        boolean hasNonAuthorApproval = false;

        for (BitbucketReviewer reviewer : reviewers) {
            hasNonAuthorApproval = hasNonAuthorApproval || (reviewer.getApproved() && !BitbucketReviewerUtils.isApprovalAuthor(reviewer, pullRequest));
        }

        return hasNonAuthorApproval;
    }
}
