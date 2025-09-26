import java.io.File;

public class Git {
    public static void initializeRepo(String repoName) {
        // get all the files
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

    }

}
