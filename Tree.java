import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Tree {
    static String gitRepo = Git.gitRepo;
        
    public static String createTree(String treeName, File repoName) throws IOException, NoSuchAlgorithmException {
        // get all the files in the folder
        File[] files = repoName.listFiles();
        StringBuilder references = new StringBuilder();

        if (files.length == 0) {
            return Git.createHash(repoName);
        
        }

        // list all the files
        for (File file : files) {
            // if the file is a directory and there are files in the directory
            if (file.isDirectory() && file.listFiles().length != 0) {
                // recursively look createTree()
                references.append("tree " + createTree(treeName, file) + " " + file.getPath() + "\n");

                // else if it's a file
            } else if (file.isFile()) {
                Git.createBlob(gitRepo, file);
                references.append("blob " + Git.createHash(file) + " " +  file.getPath() + "\n");

            }

        }

        // if the directory has no files in it, it's ignored and doesn't contribute to the hash

        // create the tree file
        File tree = new File(gitRepo + "/objects/" + treeName);   
        BufferedWriter writer = new BufferedWriter(new FileWriter(tree, true));

        // write the references and get the hash
        writer.write(references.toString());
    
        writer.close();
        return Git.createHash(tree);

    }

    public static String createIndexTree(File repoName) throws NoSuchAlgorithmException, IOException {
        // get all the files in the folder
        File[] files = repoName.listFiles();
        
        // everything i need
        StringBuilder treeContents = new StringBuilder();
        ArrayList<File> directories = new ArrayList<File>();
        String hash = "";

        // get all of the blobs
        for (File file : files) {
            if (file.isDirectory()) {
                hash = createIndexTree(file);
                // treeContents.append("tree " + createIndexTree(file) + " " +  file.getPath() + "\n");
            
            } else {
                Git.createBlob(gitRepo, file);
                treeContents.append("blob " + Git.createHash(file) + " " +  file.getPath() + "\n");
            
            }
        
        }

        System.out.println(treeContents.toString());


        // create the tree
        File tree = new File(gitRepo + "/objects/" + Git.createHash(repoName));
        tree.createNewFile();

        // write the contents
        BufferedWriter writer = new BufferedWriter(new FileWriter(tree));
        writer.write(treeContents.toString());
        writer.close();

        return Git.createHash(tree);

        // System.out.println(treeContents.toString());
        
    
    }
}
