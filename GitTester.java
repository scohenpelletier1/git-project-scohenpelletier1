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

    public static void resetEverything(String repoName) throws IOException {
        // deletes all files
        resetObjectFiles(repoName);
        Git.resetRepo(repoName);

        // remakes the core components of the repo
        Git.initializeRepo(repoName);
    
    }

    public static void main(String args[]) throws IOException, NoSuchAlgorithmException {
        // initializeRepo tests
        System.out.println("==initializeRepo()==");
        // Git.initializeRepo("git1"); // Git Repository Created
        // Git.initializeRepo("git1"); // Git Repository Already Exists
        // Git.initializeRepo("git2"); // Git Repository Created
        Git.initializeRepo("git"); // Git Repository Created

        // add textFile folder
        File myProgram = new File("myProgram");

        System.out.println();

        // // reseteRepo tests
        // System.out.println("==resetRepo()==");
        // Git.resetRepo("git1"); // Git Repository Deleted
        // Git.resetRepo("git2"); // Git Repository Deleted
        // Git.resetRepo("git4"); // Git Repository Does Not Exist
        // System.out.println();

        // // createHash tests
        // System.out.println("==createHash()==");
        // File file1 = createFile("myProgram/file1.txt", "Hello, World!");
        // System.out.println(Git.createHash(file1)); // 0a0a9f2a6772942557ab5355d76af442f8f65e01
        // File file2 = createFile("myProgram/file2.txt", "My name is Sophia Cohen-Pelletier :D");
        // System.out.println(Git.createHash(file2)); // 82a593ef07d35285dd53c050a5cc564709b07dab
        // File file3 = createFile("myProgram/file3.txt", "Why are you still reading the test cases?");
        // System.out.println(Git.createHash(file3)); // 2020d57460bdc7624d7e0e746b746cfa81414be5
        // System.out.println();

        // // createBlob tests
        // System.out.println("==createBlob()=="); // everything should return true

        // // compression on
        // Git.compression = true;
        // System.out.println(Git.createBlob("git", file1)); // creates file 320ff6a9534a245d34a1f15b7affa021fb53301a
        // System.out.println(Git.createBlob("git", file2)); // creates file 1192dca5eb1608edb89cee23186f5fd2bf793af1
        // System.out.println(Git.createBlob("git", file3)); // creates file cf9eef560135abb820422435cf2be5b4089adba5
        // System.out.println();

        // resetObjectFiles("git");

        // // compression off
        // Git.compression = false;
        // System.out.println(Git.createBlob("git", file1)); // creates file 0a0a9f2a6772942557ab5355d76af442f8f65e01
        // System.out.println(Git.createBlob("git", file2)); // creates file 82a593ef07d35285dd53c050a5cc564709b07dab
        // System.out.println(Git.createBlob("git", file3)); // creates file 2020d57460bdc7624d7e0e746b746cfa81414be5

        // // updateIndex tests
        // System.out.println("==updateIndex()==");
        // Git.updateIndex("git", file1); // db1668952fdb286939fc39d573ed88c720323b69 git/file1
        // Git.updateIndex("git", file2); // f3db729468c3b8ff98e9d88a313d5dda633d26f7 git/file2
        // Git.updateIndex("git", file3); // 1a282e683577b87e845d8f197ad2a7c7bda15384 git/file3

        // // check to see if files inside of git work
        // File hello = createFile("git/hello.txt", "hi");
        // Git.updateIndex("git", hello);
        // System.out.println();

        // // check for duplicates
        // File helloCopy = createFile("myProgram/helloCopy.txt", "hi");
        // File helloCopy2 = createFile("myProgram/helloCopy.txt", "hi");
        // File hello2 = createFile("myProgram/hello.txt", "hi");

        // Git.updateIndex("git", helloCopy);
        // Git.updateIndex("git", helloCopy2);
        // Git.updateIndex("git", hello2);

        // // files can be modified
        // BufferedWriter writer = new BufferedWriter(new FileWriter(hello));
        // writer.write("HIII!!");
        // writer.close();
        // Git.updateIndex("git", hello);

        System.out.println("==resetEverything()==");
        // resetEverything
        resetEverything("git");
        System.out.println();

        System.out.println("==createTree()==");
        System.out.println(Tree.createTree(myProgram));
        System.out.println();
        
        System.out.println("==resetEverything()=="); // again
        // resetEverything
        resetEverything("git");
        System.out.println();

        System.out.println("==createIndexTree()==");
        Git.updateIndex(myProgram);
        // Tree.createIndexTree(myProgram);
        System.out.println();

    }

}
