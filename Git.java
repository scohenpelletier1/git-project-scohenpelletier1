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
import java.util.ArrayList;

public class Git {
    // instance variables
    static boolean compression = false; // false by default
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

    public static void resetRepo(String gitRepo) {
        // make sure it exists
        File git = new File(gitRepo);
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

    public static String createBlob(String gitRepo, File file) throws NoSuchAlgorithmException, IOException {
        // get the hash
        String hash;

        // if compression is turned on
        if (compression) {
            // compress the file and get hash
            hash = createHash(compressFile(file));

            // get rid of the compressed file
            compressFile(file).delete();

        } else {
             // get the file's hash
            hash = createHash(file);
        
        }

        // make the blob file
        File blobFile = new File(gitRepo + "/objects/" + hash);

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

    public static void updateIndex(String gitRepo, File file) throws NoSuchAlgorithmException, IOException {
        // get the update message
        String update;

        if (compression) {
            update = createHash(compressFile(file)) + " " + file.getPath();

            // get rid of the compressed file
            compressFile(file).delete();
        
        } else {
            update = createHash(file) + " " + file.getPath();

        }

        // check for file updates
        String index = readFile(new File(gitRepo + "/index"));

        // this will also take care of duplicates
        if (index.contains(" " + file.getPath())) {
            // create the writer and write the index update
            BufferedReader reader = new BufferedReader(new FileReader(gitRepo + "/index"));
            String line;
            String newUpdate = "";

            // replace entry
            while ((line = reader.readLine()) != null) {
                if (line.contains(" " + file.getPath())) {
                    newUpdate += update + "\n";
                
                } else {
                    // keep reading
                    newUpdate += line + "\n";
                
                }
            
            }

            reader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter(gitRepo + "/index"));
            writer.write(newUpdate);
            writer.close();

        } else {
            // create the writer and write the index update
            BufferedWriter writer = new BufferedWriter(new FileWriter(gitRepo + "/index", true));
            writer.write(update + "\n");

            writer.close();
        
        }

        createBlob(gitRepo, file);
    
    }

    // private methods
    private static File compressFile(File file) throws IOException {
        // read the original file
        String fileContents = readFile(file);

        // encode the contents
        byte[] bytes = fileContents.getBytes("UTF-8");

        // make a new compressed file
        File compressedFile = new File("Compressed" + file.getName());

        // get the buffered writer and override the file with the compressed data
        BufferedWriter writer = new BufferedWriter(new FileWriter(compressedFile));
        writer.write(bytes.toString());
        writer.close();

        // return the now compressed file
        return compressedFile;
    
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

