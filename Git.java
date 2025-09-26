import java.io.File;

public class Git {
    public static void initializeRepo(String repoName) {
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

        // make the index directory
        if (!index.exists()) {
            index.mkdir();

        }


        // make the head directory
        if (!head.exists()) {
            head.mkdir();

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
