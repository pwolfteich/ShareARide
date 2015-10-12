package cs4720.cs.virginia.edu.sharearide;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by McNulty-PC on 10/12/2015.
 */
public class FriendStorageHelper {
    public ArrayList<String> friends;
    public String filename;
    public File file;
    public Context context;
    public FriendStorageHelper(String filename, Context context)
    {
        this.filename = filename;
        this.context = context;
        friends = new ArrayList<>();
        file = new File(context.getFilesDir(), filename);
    }
    public void addFriend(String friend)
    {
        friends.add(friend);
        writeFriends();
    }
    public void removeFriend(String friend)
    {
        try {
            file.delete();
            file.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }
        friends.remove(friend);
        writeFriends();
    }
    public void writeFriends()
    {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            for (String friend: friends) {
                fos.write(friend.getBytes());
                fos.write('\n');
            }
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public ArrayList<String> readFriends()
    {
        int length = (int) file.length();

        byte[] bytes = new byte[length];

        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            in.read(bytes);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String allFriends = new String(bytes);
        friends.clear();
        friends.addAll(Arrays.asList(allFriends.split("\n")));
        return friends;
    }
    public void deleteFile()
    {
        //writeFriends();
        file.delete();
    }
}
