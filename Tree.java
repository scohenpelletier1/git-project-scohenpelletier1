import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Tree {
    static String gitRepo = Git.gitRepo;
        
    public static String createTree(File directoryPath) throws IOException, NoSuchAlgorithmException {
        String directoryHash = createTreeHelper(directoryPath);

        // root file!!!!
        Path tempName = Paths.get("rootTemp");

        File rootTemp = new File("rootTemp");
        BufferedWriter rootWriter = new BufferedWriter(new FileWriter(rootTemp));
        rootWriter.write("tree " + directoryHash + " " + directoryPath);
        rootWriter.close();

        // change it's name
        Path rootFinal = Paths.get(gitRepo + "/objects/" + Git.createHash(rootTemp));
        Files.move(tempName, rootFinal, StandardCopyOption.REPLACE_EXISTING);

        return Git.createHash(new File(rootFinal.toString()));

    }

    public static String createTreeHelper(File directoryPath) throws NoSuchAlgorithmException, IOException {
        // get all the files in the folder
        File[] files = directoryPath.listFiles();
        StringBuilder content = new StringBuilder();

        if (files.length == 0) {
            return Git.createHash(directoryPath);
        
        }

        // list all the files
        for (File file : files) {
            // if the file is a directory and there are files in the directory
            if (file.isDirectory() && file.listFiles().length != 0) {
                // recursively look createTree()
                content.append("tree " + createTreeHelper(file) + " " + file.getName() + "\n");

                // else if it's a file
            } else if (file.isFile()) {
                Git.createBlob(gitRepo, file);
                content.append("blob " + Git.createHash(file) + " " +  file.getName() + "\n");

            }

        }

        // if the directory has no files in it, it's ignored and doesn't contribute to the hash

        // create the tree file
        Path tempName = Paths.get("temp");

        File tempFile = new File("temp");
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
        writer.write(content.toString());
        writer.close();

        // change it's name
        Path finalName = Paths.get(gitRepo + "/objects/" + Git.createHash(tempFile));
        Files.move(tempName, finalName, StandardCopyOption.REPLACE_EXISTING);
        
        return Git.createHash(new File(finalName.toString()));

    }

    public static String createIndexTree() throws NoSuchAlgorithmException, IOException {
        // create a temp file to copy over the contents from the index file
        File tempFile = new File("workingList");
        List<String> workingList = new ArrayList<>();
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        // create lists for organization of trees later
        List<String> filePaths = new ArrayList<>();

        // read the index file
        BufferedReader reader = new BufferedReader(new FileReader(gitRepo + "/index"));
        StringBuilder indexContents = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            // add to the lists and temp file
            filePaths.add(line.substring(41));
            workingList.add("blob " + line);
            indexContents.append("blob " + line + "\n");

        }
        
        reader.close();

        // write the contents in the temp folder
        writer.write(indexContents.toString());
        writer.close();

        // recursively build the tree by calling indexTreeHelper()
        indexTreeHelper(workingList, filePaths);

        // collapse the temp file
        BufferedWriter writer2 = new BufferedWriter(new FileWriter(tempFile));
        writer2.write(workingList.get(0));
        writer2.close();

        Git.createBlob(gitRepo, tempFile);

        // now just make the root
        File rootFile = new File("(root)");

        // write in the final tree
        BufferedWriter rootWriter = new BufferedWriter(new FileWriter(rootFile));
        rootWriter.write("tree " + Git.createHash(tempFile) + " (root)");
        rootWriter.close();

        // create the tree blob
        String root = Git.createBlob(gitRepo, rootFile);

        // delete temp files
        tempFile.delete();
        rootFile.delete();

        // return root
        return root;
        
    }

    private static void indexTreeHelper(List<String> workingList, List<String> filePaths) throws IOException, NoSuchAlgorithmException {
        String leafHeavyDirectory = greatestLeaf(filePaths);

        // make the temporary tree
        File tempTree = new File("tempTree");

        // no more directories
        if (leafHeavyDirectory.length() == 0) {
            tempTree.delete();
            return;
        
        }

        // tree writers
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempTree));
        StringBuilder treeContents = new StringBuilder();

        // all files will be blobs because we found the deepest directory
        int index = 0;

        while (index < filePaths.size()) {
            // when the file path is found
            if (filePaths.get(index).startsWith(leafHeavyDirectory + "/")) {
                // add everything in the directory to the tree contents
                int lastSlash = workingList.get(index).lastIndexOf("/");
                treeContents.append(workingList.get(index).substring(0, 46) + workingList.get(index).substring(lastSlash + 1) + "\n");
            
                // delete its entries in the lists
                filePaths.remove(index);
                workingList.remove(index);

            } else {
                // move index up if nothing changes
                index++;
            
            }
            
        }

        // write out the contents
        writer.write(treeContents.toString());
        writer.close();

        // collapse into tree
        workingList.add(0, "tree " + Git.createHash(tempTree) + " " + leafHeavyDirectory);
        Git.createBlob(gitRepo, tempTree);

        filePaths.add(leafHeavyDirectory);

        // recurse
        indexTreeHelper(workingList, filePaths);
    
    }

    private static String greatestLeaf(List<String> filePaths) {
        // deepest directory
        String deepestDir = "";
        int deepest = 0;

        for (String path : filePaths) {
            String tempPath = path;

            // get the index of the last slash and the depth
            int lastSlash = path.lastIndexOf('/');
            int depth = 0;

            // while there are still slashes
            while (tempPath.indexOf('/') != -1) {
                // add to depth, and shift past the slash
                depth++;
                tempPath = tempPath.substring(tempPath.indexOf('/') + 1);
            
            }
        
            // add the directory to the list of directories
            if (depth > deepest) {
                deepest = depth;
                deepestDir = path.substring(0, lastSlash);

            }

        }

        // return the deepest directory
        return deepestDir;
    
    }
}
