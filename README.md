==Git.java==

Method Summary:
initializeRepo()
resetRepo()
verifyRepoExists

Method Details:
initializeRepo()
    Initializes the repository with a main git directory, and three subdirectories (objects, index, and HEAD)

    Parameters:
        repoName - The name of the repository that is being initialized

resetRepo()
    Deletes all the files in the repository, including the main git directory and three subdirectories (objects, index, and HEAD)

    Parameters:
        repoName - The name of the repository that is being deleted

boolean verifyRepoExists()
    Goes through each file that should be in the repository and checks to see if they exist or not

    Parameters:
        mainDir - The main directory of the repository

    Returns:
        true if all necessary files exist and false otherwise


==GitTester.java==

Test Summary:
initializeRepo tests
reseteRepo tests

Test Details:
initializeRepo tests
    Checks to see if the repositories "git", "git2," and "git3" were successfully initialized. Attempts to initialize "git" twice in order to test the edge case where the repo already exists.

resetRepo tests
    Checks to see if the repositories "git2" and "git3" were successfully reset. Attempts to reset "git4" in order to test the edge case where the repo does not exist.
