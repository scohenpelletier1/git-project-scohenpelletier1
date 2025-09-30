import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class GitTester {
    // methods for testing purposes
    public static File createFile(String fileName, String fileContents) throws IOException {
        File file = new File(fileName);

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(fileContents);
        writer.close();

        return file;

    }

    public static void resetObjectFiles(String repoName) {
        // get the files in the objects folder
        File[] files = new File(repoName + "/objects").listFiles();

        // delete the files
        for (File file : files) {
            file.delete();
        
        }
    
    }

    public static void main(String args[]) throws IOException, NoSuchAlgorithmException {
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

        // createHash tests
        System.out.println("==createHash()==");
        File file1 = createFile("file1", "Hello, World!");
        System.out.println(Git.createHash(file1)); // 0a0a9f2a6772942557ab5355d76af442f8f65e01
        File file2 = createFile("file2", "My name is Sophia Cohen-Pelletier :D");
        System.out.println(Git.createHash(file2)); // 82a593ef07d35285dd53c050a5cc564709b07dab
        File file3 = createFile("file3", "Why are you still reading the test cases?");
        System.out.println(Git.createHash(file3)); // 2020d57460bdc7624d7e0e746b746cfa81414be5
        System.out.println();

        // createBlob tests
        System.out.println("==createBlob()==");
        Git.createBlob("git", file1); // creates file 0a0a9f2a6772942557ab5355d76af442f8f65e01
        Git.createBlob("git", file2); // creates file 82a593ef07d35285dd53c050a5cc564709b07dab
        Git.createBlob("git", file3); // creates file 2020d57460bdc7624d7e0e746b746cfa81414be5

        resetObjectFiles("git");

        System.out.println(Git.createBlob("git", file1)); // true
        System.out.println(Git.createBlob("git", file2)); // true
        System.out.println(Git.createBlob("git", file3)); // true
        System.out.println();

    }

}
