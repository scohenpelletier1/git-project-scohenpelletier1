import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Git {
// instance variables
static String gitRepo;

// public methods
    public static void initializeRepo(String gitRepo) throws IOException {
        Git.gitRepo = gitRepo;

        // Create the file paths
        File git = new File(gitRepo);
        File obj = new File(gitRepo + "/objects");
        File index = new File(gitRepo + "/index");
        File head = new File(gitRepo + "/HEAD");

        // check to see if the repo exists
        if (git.exists() && obj.exists() && index.exists() && head.exists()) {
            System.out.println("Git Repository Already Exists");
            return;

        }

        // if it doesn't, then make the directory
        if (!git.exists()) {
            git.mkdir();

        }

        //  make the objects directory
        if (!obj.exists()) {
            obj.mkdir();

        }

        // make the index file
        if (!index.exists()) {
            index.createNewFile();

        }

        // make the head file
        if (!head.exists()) {
            head.createNewFile();

        }

        // check to make sure that the repo exists
        if (!verifyRepoExists(git)) {
            System.out.println("Git Repository Could Not Be Created");

        } else {
            System.out.println("Git Repository Created");

        }

    }

    public static void cleanUp(String filePath) {
        // make sure it exists
        File file = new File(filePath);

        // check if the file is a directory
        if (file.isDirectory()) {
            // get all the files inside the directory
            File[] files = file.listFiles();

            // cycle through and delete them
            for (File newFile : files) {
                if (newFile.isDirectory()) {
                    cleanUp(newFile.getPath());
                
                }

                newFile.delete();

            }
        
        }

        // delete current file
        file.delete();

        // check to make sure that the repo was deleted
        if (file.exists()) {
            System.out.println("Git Repository Could Not Be Deleted");

        }

    }

    public static String createHash(File file) throws IOException, NoSuchAlgorithmException {
        // grab the file contents by reading the file
        String fileContents = readFile(file.getPath());

        // ceate sha1 digest
        MessageDigest digest = MessageDigest.getInstance("SHA1");

        // get the hash by counting bytes
        byte[] hashBytes = digest.digest(fileContents.getBytes(StandardCharsets.UTF_8));

        // convert hash to hex string
        String hexCode = "";

        for (int i = 0; i < hashBytes.length; i++) {
            hexCode += String.format("%02x", hashBytes[i]);

        }

        return hexCode;

    }

    public static String createBlob(String gitRepo, File file) throws NoSuchAlgorithmException, IOException {
        // get the hash
        String hash;

        // get the file's hash
        hash = createHash(file);

        // make the blob file
        File blobFile = new File(gitRepo + "/objects/" + hash);

        // write the contents in the new file
        BufferedWriter writer = new BufferedWriter(new FileWriter(blobFile));
        writer.write(readFile(file.getPath()));
        writer.close();

        // create the new file
        blobFile.createNewFile();

        // make sure it actually exists
        File[] files = new File(gitRepo + "/objects").listFiles();

        for (File objectFile : files) {
            // if the file is there, return true
            if (objectFile.getName().equals(hash)) {
                return blobFile.getName();

            }

        }

        // Error if the blob file couldn't be created
        System.out.println("BLOB File Could Not Be Created");
        return "";

    }

    public static void updateIndex(File filePath) throws NoSuchAlgorithmException, IOException {
        // edge cases
        if (!filePath.exists()) {
            throw new IllegalArgumentException("File does not exist.");

        } else if (filePath.isDirectory()) {
            throw new IllegalArgumentException("Cannot add a directory to the index file.");

        }

        // create string builder to make the content that will be added to the index file
        StringBuilder indexContent = new StringBuilder();

        // grab everything from the index file, stuff might be replaced
        indexContent.append(readFile(gitRepo + "/index"));

        // create blob, then add that to the index file
        Git.createBlob(gitRepo, filePath);
        int pathIndex = indexContent.indexOf(filePath.getPath());

        // if this file already exists in the index
        if (pathIndex != -1) {
            // replace it since it's been updated
            indexContent.replace(pathIndex - 41, pathIndex + filePath.getPath().length(), Git.createHash(filePath) + " " +  filePath.getPath());

        } else {
            // otherwise just add it normally
            indexContent.append(Git.createHash(filePath) + " " +  filePath.getPath());
            indexContent.append("\n");

        }

        // get the file and buffered writer
        File index = new File(gitRepo + "/index");
        BufferedWriter writer1 = new BufferedWriter(new FileWriter(index));

        // write the index content
        writer1.write(indexContent.toString());
        writer1.close();

    }

    // private methods
    public static String readFile(String path) throws IOException {
        // get the file
        File file = new File(path);

        // get the reader
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String fileContents = "";
        String line;

        // read the file then close the file
        while ((line = reader.readLine()) != null) {
            fileContents += line + "\n";

        }

        reader.close();

        // return the file contents
        return fileContents;

    }

    private static boolean verifyRepoExists(File mainDir) {
        // make sure the main directory (git) exists
        if (!mainDir.exists()) {
            return false;

        }

        // cycle through all of it's files to make sure that objects, index, and HEAD are there
        File[] files = mainDir.listFiles();

        for (File file : files) {
            // if it equals any of the files, return true
            if ((file.getPath().equals(mainDir.getPath() + "/objects")) ||
            (file.getPath().equals(mainDir.getPath() + "/index")) ||
            (file.getPath().equals(mainDir.getPath() + "/HEAD"))) {
                return true;

            }

        }

        // if none of the files exist, return false
        return false;

    }

}
