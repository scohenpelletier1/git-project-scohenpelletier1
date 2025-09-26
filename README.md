==Git.java==

Method Summary:
initializeRepo()
resetRepo()
verifyRepoExists()
createHash()
readFile()

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

createHash()
    Takes a file, reads its contents, then takes those contents and hashes them in SHA1 formatting

    Parameters:
        file - the file who's contents are beinging hashed
    
    Returns:
        The hash of the file's contents as a String

readFile()
    Uses a BufferedReader to read through a file, then compiles its contents into a String

    Parameters:
        file - the file who's contents are being read
    
    Returns:
        The file contents as a String


==GitTester.java==
Method Summary:
createFile()

Method Details:
createFile()
    Creates a file and writes in the file contents

    Parameters:
        fileName - The name of the file
        fileContents - The intended contents of the file, what will be written in
    
    Returns:
        The file

Test Summary:
initializeRepo tests
reseteRepo tests
createHash tests

Test Details:
initializeRepo tests
    Checks to see if the repositories "git", "git2," and "git3" were successfully initialized. Attempts to initialize "git" twice in order to test the edge case where the repo already exists.

resetRepo tests
    Checks to see if the repositories "git2" and "git3" were successfully reset. Attempts to reset "git4" in order to test the edge case where the repo does not exist.

createHash tests
    Creates test files with things written in them, and then checks to see if the SHA1 hash code generated is correct.
