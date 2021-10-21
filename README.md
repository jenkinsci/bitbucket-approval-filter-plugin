# Jenkins Bitbucket Pull Request Approval Plugin

This repository is a Jenkins plugin that interacts with the [Bitbucket Branch Source Plugin](https://github.com/jenkinsci/bitbucket-branch-source-plugin) and provides the extended ability of only allowing pull requests that have an approval or a non-author approval before building.

Note: There is a known issue with using the Basic Branch Build Strategies plugin with named branches not building when using this plugin.

The different approval statuses supported are: 

-   No Approval Necessary
    -   This is the same behavior as not having the plugin installed.
        Approvals have no affect on whether a pull request will be built or not.
-   Any Approval Required
    -   Before being built, a pull request must have an approval. 
        This approval could come from the author of the pull request.
-   Non-Author Approval Required
    -   Before being built a pull request must have an approval that is from a user that is not the author.

