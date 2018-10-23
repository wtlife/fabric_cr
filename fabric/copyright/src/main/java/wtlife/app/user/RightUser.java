package wtlife.app.user;

import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;

import java.util.HashSet;
import java.util.Set;

public class RightUser implements User {
    private final String userName;
    private String mspid;
    private String USERTYPE;

    public RightUser(String USERTYPE, String userName, String mspid) {
        this.userName = userName;
        this.mspid = mspid;
        this.USERTYPE = USERTYPE;

    }

    @Override
    public String getName() {
        return userName;
    }

    @Override
    public Set<String> getRoles() {
        return new HashSet<String>();
    }

    @Override
    public String getAccount() {
        return "";
    }

    @Override
    public String getAffiliation() {
        return "";
    }

    @Override
    public Enrollment getEnrollment() {
        return new RightUserEnrollment(this.USERTYPE, userName, mspid);
    }

    @Override
    public String getMspId() {
        return this.mspid;
    }
}
