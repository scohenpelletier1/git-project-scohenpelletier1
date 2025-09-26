import java.io.FileNotFoundException;

public class GitTester {
    public static void main(String args[]) throws FileNotFoundException {
        // initializeRepo tests
        System.out.println("==initializeRepo()==");
        Git.initializeRepo("git"); // Git Repository Created
        Git.initializeRepo("git"); // Git Repository Already Exists
        Git.initializeRepo("git2"); // Git Repository Created
        Git.initializeRepo("git3"); // Git Repository Created
        System.out.println();

        // reseteRepo tests
        System.out.println("==resetRepo()==");
        Git.resetRepo("git2"); // Git Repository Deleted
        Git.resetRepo("git3"); // Git Repository Deleted
        Git.resetRepo("git4"); // Git Repository Does Not Exist
        System.out.println();

    }

}
