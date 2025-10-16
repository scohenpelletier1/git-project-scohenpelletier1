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
        Git.cleanUp(repoName);

        // remakes the core components of the repo
        Git.initializeRepo(repoName);
    
    }

    public static void testInit(GitWrapper gw) {
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
    
    }

    public static void testAddEdge(GitWrapper gw) {
        try {
            // throws error for a nonexistant path
            gw.add("myProgram/inner/world.txt");

        } catch (Exception e) {
            System.out.println("File does not exist.");

        }

        try {
            // throws error for directory path
            gw.add("myProgram/scripts");

        } catch (Exception e) {
            System.out.println("Cannot add a directory to the index file.");

        }
    
    }

    public static void testAdd(GitWrapper gw) {
        try {
            // stages a new file
            gw.add("myProgram/hello.txt");
            System.out.println("myProgram/hello.txt added to index file.");

            if (new File("git/objects/" + Git.createHash(new File("myProgram/hello.txt"))).exists()) {
                System.out.println("Blob for this file was created");
            
            } else {
                System.out.println("FAILED TO CREATE BLOB");
            
            }

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

            // no update means nothing changes
            gw.add("myProgram/hello.txt");
            System.out.println("myProgram/hello.txt unchanged and not re-added to index file.");
            
        } catch (Exception e) {
            System.out.println("add() does not work");

        }
    
    }
    
    public static void main(String args[]) throws IOException, NoSuchAlgorithmException {        
        // initializeRepo tests
        System.out.println("==initializeRepo()==");
        Git.initializeRepo("git1"); // Git Repository Created
        Git.initializeRepo("git1"); // Git Repository Already Exists
        Git.initializeRepo("git2"); // Git Repository Created
        Git.initializeRepo("git"); // Git Repository Created

        // add textFile folder and a bunch of files and directories
        File myProgram = new File("myProgram");
        myProgram.mkdir();

        File scripts = new File("myProgram/scripts");
        scripts.mkdir();

        File file1 = createFile("myProgram/file1.txt", "Hello, World!");
        File file2 = createFile("myProgram/file2.txt", "My name is Sophia Cohen-Pelletier :D");
        File file3 = createFile("myProgram/file3.txt", 
                                "Why are you still reading the test cases?");
        File sample1 = new File("myProgram/scripts/sample1.txt");
        File sample2 = new File("myProgram/scripts/sample2.txt");
        
        file1.createNewFile();
        file2.createNewFile();
        file3.createNewFile();
        sample1.createNewFile();
        sample2.createNewFile();

        System.out.println();

        // reseteRepo tests
        System.out.println("==resetRepo()==");
        Git.cleanUp("git1"); // Git Repository Deleted
        Git.cleanUp("git2"); // Git Repository Deleted
        Git.cleanUp("git4"); // Git Repository Does Not Exist
        System.out.println();

        // createHash tests
        System.out.println("==createHash()==");
        System.out.println(Git.createHash(file1)); // 60fde9c2310b0d4cad4dab8d126b04387efba289
        System.out.println(Git.createHash(file2)); // 8e51dbad52781f72f37a61360ece0d0f470fd23d
        System.out.println(Git.createHash(file3)); // fcc0c9bae1e2361a702a318bb16a8e87d23c4f2d
        System.out.println();

        // createBlob tests
        System.out.println("==createBlob()==");
        System.out.println(Git.createBlob("git", file1)); // 60fde9c2310b0d4cad4dab8d126b04387efba289
        System.out.println(Git.createBlob("git", file2)); // 8e51dbad52781f72f37a61360ece0d0f470fd23d
        System.out.println(Git.createBlob("git", file3)); // fcc0c9bae1e2361a702a318bb16a8e87d23c4f2d

        // now with files with the same content
        System.out.println(Git.createBlob("git", sample1)); // da39a3ee5e6b4b0d3255bfef95601890afd80709
        System.out.println(Git.createBlob("git", sample2)); // da39a3ee5e6b4b0d3255bfef95601890afd80709
        System.out.println();

        // updateIndex tests
        System.out.println("==updateIndex()==");
        Git.updateIndex(file1);
        Git.updateIndex(file2);
        Git.updateIndex(file3);
        Git.updateIndex(sample1);
        Git.updateIndex(sample2);
        System.out.println();

        // check for if duplicates work
        File hello = createFile("myProgram/hello.txt", "hi");
        File helloCopy = createFile("myProgram/helloCopy.txt", "hi");
        File helloCopy2 = createFile("myProgram/helloCopy.txt", "hi");

        Git.updateIndex(hello);
        Git.updateIndex(helloCopy);
        Git.updateIndex(helloCopy2);

        // files can be modified
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(hello))) {
            writer.write("HIII!!");
        }

        Git.updateIndex(hello);

        System.out.println("==createTree()==");
        System.out.println(Tree.createTree(myProgram));
        System.out.println();
        
        // reset object files
        resetObjectFiles("git");

        // works multiple times, same outcome
        System.out.println("==createIndexTree()==");
        System.out.println(Tree.createIndexTree());
        System.out.println(Tree.createIndexTree());
        System.out.println(Tree.createIndexTree());
        System.out.println();

        // delete git
        Git.cleanUp("git");

        // GP-5.0 TESTS vvvvvvvv

        // wrapper tests
        GitWrapper gw = new GitWrapper();

        // init()
        System.out.println("==init()==");
        testInit(gw);
        System.out.println();

        // add()
        System.out.println("==add()==");
        testAddEdge(gw);
        System.out.println();
        testAdd(gw);
        System.out.println();

    }

}
