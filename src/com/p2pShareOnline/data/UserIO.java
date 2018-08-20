package com.p2pShareOnline.data;

import com.p2pShareOnline.business.User;

import java.io.*;
import java.util.StringTokenizer;

public class UserIO{

    public static boolean add(User user, String path) {
        try {
            File file = new File(path);
            PrintWriter writer = new PrintWriter(new FileWriter(file,true));
            writer.println(user.getEmail() + "|" + user.getFirstName() + "|" + user.getLastName());
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static User getUser(String email, String path) {
        File file = new File(path);
        User user = null;
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line = in.readLine();
            while (line != null){
                StringTokenizer t = new StringTokenizer(line,"|");
                if (t.countTokens() < 3) {
                    return new User("","","");
                }
                user = new User();
                String token = t.nextToken();
                if (token.equalsIgnoreCase(email)){
                    user.setEmail(email);
                    user.setFirstName(t.nextToken());
                    user.setLastName(t.nextToken());
                }
                line = in.readLine();
            }
            in.close();
            return user;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
