package tk.lightweightcoding.jenkins.bitbucket.approval.filters;

import com.cloudbees.jenkins.plugins.bitbucket.BitbucketSCMSourceRequest;
import com.cloudbees.jenkins.plugins.bitbucket.PullRequestSCMHead;
import com.cloudbees.jenkins.plugins.bitbucket.api.BitbucketPullRequest;
import com.cloudbees.jenkins.plugins.bitbucket.api.BitbucketReviewer;
import edu.umd.cs.findbugs.annotations.NonNull;
import jenkins.scm.api.SCMHead;
import jenkins.scm.api.trait.SCMHeadFilter;
import jenkins.scm.api.trait.SCMSourceRequest;

import java.io.IOException;

public abstract class BaseApprovalFilter  extends SCMHeadFilter {

    @Override
    public boolean isExcluded(@NonNull SCMSourceRequest scmSourceRequest, @NonNull SCMHead scmHead) throws IOException, InterruptedException {
        if (scmHead instanceof PullRequestSCMHead) {
            BitbucketSCMSourceRequest request = (BitbucketSCMSourceRequest) scmSourceRequest;
            for (BitbucketPullRequest pull : request.getPullRequests()) {
                if (pull.getSource().getBranch().getName().equals(((PullRequestSCMHead) scmHead).getBranchName())) {
                    boolean hasApproval = false;
                    BitbucketPullRequest fullPullRequest = request.getPullRequestById(Integer.parseInt(pull.getId()));
                    for (BitbucketReviewer reviewer : fullPullRequest.getReviewers()) {
                        hasApproval = hasApproval || (reviewer.getApproved() && additionalFiltering(reviewer, pull));
                    }

                    return !hasApproval;
                }
            }
        }

        return false;
    }

    protected abstract boolean additionalFiltering(BitbucketReviewer reviewer, BitbucketPullRequest pullRequest);
}
