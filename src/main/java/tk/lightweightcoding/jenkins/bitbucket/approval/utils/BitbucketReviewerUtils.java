package tk.lightweightcoding.jenkins.bitbucket.approval.utils;

import com.cloudbees.jenkins.plugins.bitbucket.api.BitbucketPullRequest;
import com.cloudbees.jenkins.plugins.bitbucket.api.BitbucketReviewer;

import java.util.Collection;

public class BitbucketReviewerUtils {
    private static boolean isApprovalAuthor(BitbucketReviewer reviewer, BitbucketPullRequest pullRequest) {
        return reviewer.getUser().getIdentifier().equalsIgnoreCase(pullRequest.getAuthorIdentifier());
    }

    public static boolean hasAuthorApproval(Collection<BitbucketReviewer> reviewers, BitbucketPullRequest pullRequest) {
        return reviewers.stream()
                .filter(BitbucketReviewer::getApproved)
                .anyMatch(bitbucketReviewer -> BitbucketReviewerUtils.isApprovalAuthor(bitbucketReviewer, pullRequest));
    }

    public static boolean hasNonAuthorApproval(Collection<BitbucketReviewer> reviewers, BitbucketPullRequest pullRequest) {
        return reviewers.stream()
                .filter(BitbucketReviewer::getApproved)
                .anyMatch(bitbucketReviewer -> !BitbucketReviewerUtils.isApprovalAuthor(bitbucketReviewer, pullRequest));
    }
}
