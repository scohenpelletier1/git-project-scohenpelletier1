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
        // // initializeRepo tests
        // System.out.println("==initializeRepo()==");
        // Git.initializeRepo("git1"); // Git Repository Created
        // Git.initializeRepo("git1"); // Git Repository Already Exists
        // Git.initializeRepo("git2"); // Git Repository Created
        // Git.initializeRepo("git"); // Git Repository Created

        // // add textFile folder
        // File myProgram = new File("myProgram");

        // System.out.println();

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
        // Git.updateIndex(file1); // db1668952fdb286939fc39d573ed88c720323b69 git/file1
        // Git.updateIndex(file2); // f3db729468c3b8ff98e9d88a313d5dda633d26f7 git/file2
        // Git.updateIndex(file3); // 1a282e683577b87e845d8f197ad2a7c7bda15384 git/file3

        // // check to see if files inside of git work
        // File hello = createFile("git/hello.txt", "hi");
        // Git.updateIndex(hello);
        // System.out.println();

        // // check for duplicates
        // File helloCopy = createFile("myProgram/helloCopy.txt", "hi");
        // File helloCopy2 = createFile("myProgram/helloCopy.txt", "hi");
        // File hello2 = createFile("myProgram/hello.txt", "hi");

        // Git.updateIndex(helloCopy);
        // Git.updateIndex(helloCopy2);
        // Git.updateIndex(hello2);

        // // files can be modified
        // try (BufferedWriter writer = new BufferedWriter(new FileWriter(hello))) {
        //     writer.write("HIII!!");
        // }
        // Git.updateIndex(hello);

        // System.out.println("==resetEverything()==");
        // // resetEverything
        // resetEverything("git");
        // System.out.println();

        // System.out.println("==createTree()==");
        // System.out.println(Tree.createTree(myProgram));
        // System.out.println();
        
        // System.out.println("==resetEverything()=="); // again
        // // resetEverything
        // resetEverything("git");
        // System.out.println();

        // System.out.println("==createIndexTree()==");
        // Git.updateIndex(myProgram);
        // System.out.println(Tree.createIndexTree(myProgram));
        // System.out.println();

        // resetEverything("git");

        // wrapper tests
        GitWrapper gw = new GitWrapper();

        // init()
        System.out.println("==init()==");
        try {
            // create repo
            gw.init();

            // delete objects and see if code still works
            File obj = new File("git/objects");
            obj.delete();

            gw.init();

            // delete index and HEAD and see if code still works
            File index = new File("git/index");
            File head = new File("git/HEAD");
            index.delete();
            head.delete();

            gw.init();

            // make sure running it again doesn't cause errors
            gw.init();
            
        } catch (Exception e) {
            System.out.println("init() does not work");

        }
        System.out.println();

        System.out.println("==add()==");
        try {
            // throws error for a nonexistant path
            gw.add("myProgram/inner/world.txt");

        } catch (Exception e) {
            System.out.println("File does not exist.");

        }
        System.out.println();

        try {
            // throws error for directory path
            gw.add("myProgram/scripts");

        } catch (Exception e) {
            System.out.println("Cannot add a directory to the index file.");

        }
        System.out.println();

        try {
            // stages a new file
            gw.add("myProgram/hello.txt");
            System.out.println("myProgram/hello.txt added to index file.");

            if (new File("git/objects/" + Git.createHash(new File("myProgram/hello.txt"))).exists()) {
                System.out.println("Blob for this file was created");
            
            } else {
                System.out.println("FAILED TO CREATE BLOB");
            
            }
            System.out.println();

            // make change, stage again
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("myProgram/hello.txt"))) {
                writer.write("I have updated the file myProgram/hello.txt to say this instead of \"hi\"");
            }

            gw.add("myProgram/hello.txt");
            System.out.println("myProgram/hello.txt changed and re-added to index file.");

            if (new File("git/objects/" + Git.createHash(new File("myProgram/hello.txt"))).exists()) {
                System.out.println("Blob for this file was updated, two versions now exist in git/objects");
            
            } else {
                System.out.println("FAILED TO CREATE NEW BLOB");
            
            }
            System.out.println();

            // no update means nothing changes
            gw.add("myProgram/hello.txt");
            System.out.println("myProgram/hello.txt unchanged and not re-added to index file.");
            
        } catch (Exception e) {
            System.out.println("add() does not work");

        }
        System.out.println();

        // gw.commit("John Doe", "Initial commit");
        // gw.checkout("1234567890");


    }

}
