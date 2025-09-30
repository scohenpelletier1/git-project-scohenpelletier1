import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Git {
    // instance variables
    static boolean compression = false; // false by default
    
    // public methods
    public static void initializeRepo(String repoName) throws IOException {
        // Create the file paths
        File git = new File(repoName);
        File obj = new File(repoName + "/objects");
        File index = new File(repoName + "/index");
        File head = new File(repoName + "/HEAD");
        
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

    public static void resetRepo(String repoName) {
        // make sure it exists
        File git = new File(repoName);
        if (!verifyRepoExists(git)) {
            System.out.println("Git Repository Does Not Exist");
            return;

        }

        // get all the files inside the directory
        File[] files = git.listFiles();

        // cycle through and delete them
        for (File file : files) {
            file.delete();
        
        }

        // delete main directory
        git.delete();

        // check to make sure that the repo was deleted
        if (verifyRepoExists(git)) {
            System.out.println("Git Repository Could Not Be Deleted");
        
        } else {
            System.out.println("Git Repository Deleted");

        }

    }

    public static String createHash(File file) throws IOException, NoSuchAlgorithmException {
        // grab the file contents by reading the file
        String fileContents = readFile(file);

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

    public static String createBlob(String repoName, File file) throws NoSuchAlgorithmException, IOException {
        // get the hash
        String hash;

        // if compression is turned on
        if (compression) {
            // compress the file
            file = compressFile(file);

        }

        // get the file's hash
        hash = createHash(file);

        // make the blob file
        File blobFile = new File(repoName + "/objects/" + hash);

        // read the old file
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String fileContents = "";
        String line;

        while ((line = reader.readLine()) != null) {
            fileContents += line;

        }

        reader.close();

        // write the contents in the new file
        BufferedWriter writer = new BufferedWriter(new FileWriter(blobFile));
        writer.write(fileContents);
        writer.close();

        // create the new file
        blobFile.createNewFile();

        // make sure it actually exists
        File[] files = new File(repoName + "/objects").listFiles();

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

    public static void updateIndex(String repoName, File file) throws NoSuchAlgorithmException, IOException {
        // get the update message
        String update = createBlob(repoName, file) + " " + file.getPath() + "\n";

        // create the writer and write the index update
        BufferedWriter writer = new BufferedWriter(new FileWriter(repoName + "/index", true));
        writer.write(update);

        writer.close();
    
    }

    // private methods
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

    private static String readFile(File file) throws IOException {
        // get the reader
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String fileContents = "";
        String line;

        // read the file then close the file
        while ((line = reader.readLine()) != null) {
            fileContents += line;
        
        }

        reader.close();

        // return the file contents
        return fileContents;
    
    }

    private static File compressFile(File file) throws IOException {
        // create compressed file
        File compressedFile = new File(file.getName());

        // read the original file
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String fileContents = "";
        String line;

        while ((line = reader.readLine()) != null) {
            fileContents += line + "\n";

        }

        reader.close();

        // encode the contents
        byte[] bytes = fileContents.getBytes("UTF-8");

        // get the buffered writer and write the compressed data
        BufferedWriter writer = new BufferedWriter(new FileWriter(compressedFile));
        writer.write(bytes.toString());
        writer.close();

        // return the compressed file
        return compressedFile;
    
    }

}
