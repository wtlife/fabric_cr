package wtlife.app.user;

import wtlife.app.bean.Right;

import java.io.File;

public class test {
    public static void main(String[] args) {
//        File file = new File(new File(System.getProperty("user.dir")).getParent());
//        System.out.println(file.getPath());
//        String path = file.getPath()+"/workspaces/right/crypto-config/peerOrganizations/org1.right.com/users/";
//        path = path+ "Admin@"+"org1.right.com"+"/msp"+"/signcerts";
//        File key = new File(path);
//        System.out.println(key.getPath()+key.listFiles()[0].getName());
        Right right = new Right("work1", "wutao", "org1", 1000, "0xhash", "sigsigsig");
        System.out.println(right.toStringArray());

    }
}
