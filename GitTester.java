import java.io.FileNotFoundException;

public class GitTester {
    public static void main(String args[]) throws FileNotFoundException {
        // InitializeRepo tests
        System.out.println("==initializeRepo()==");
        Git.initializeRepo("git"); // Git Repository Created
        Git.initializeRepo("git"); // Git Repository Already Exists
        Git.initializeRepo("git2"); // Git Repository Created
        System.out.println();

    }

}
