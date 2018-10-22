package wtlife.app.client;

import wtlife.app.bean.Right;
import wtlife.app.org.Org;
import wtlife.app.config.Config;
import org.apache.log4j.Logger;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class FabricClient {

    private static Logger logger = Logger.getLogger(FabricClient.class);
    public static HFClient client = null;
    public static CryptoSuite cs;

    static {
        try {
            cs = CryptoSuite.Factory.getCryptoSuite();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, Org> orgHashMap = null;
    public static ChaincodeID cid = ChaincodeID.newBuilder().setName(Config.CHAINCODENAME).setVersion(Config.CHAINCODEVERSION).build();
    public static User peer0org1 = null;

    /**
     * return instance of client
     */
    public HFClient getInstance() {
        return client;
    }

    /**
     * Init
     */
    public static void init() throws CryptoException, InvalidArgumentException, MalformedURLException {
        client = HFClient.createNewInstance();
        client.setCryptoSuite(cs);
        orgHashMap = Config.getConfigure();
        peer0org1 = orgHashMap.get("org1").getAdmin();
        client.setUserContext(peer0org1);
    }

    public static void regist(Channel channel, Right right) throws InvalidArgumentException, ProposalException {
        TransactionProposalRequest req = client.newTransactionProposalRequest();
        req.setChaincodeID(cid);
        req.setFcn("regist");
//        req.setArgs(right.toStringArray());
        req.setArgs("work1", "wutao", "org1", "1000", "0xhash", "sigsigsig");

        Map<String, byte[]> tm = new HashMap<>();
        tm.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8));
        tm.put("method", "TransactionProposalRequest".getBytes(UTF_8));
        tm.put("result", ":)".getBytes(UTF_8));
        req.setTransientMap(tm);
        Collection<ProposalResponse> resps = channel.sendTransactionProposal(req);
        for (ProposalResponse resp : resps) {
            String payload = new String(resp.getChaincodeActionResponsePayload());
            logger.debug("response: " + payload);
        }
        channel.sendTransaction(resps);
    }

    public static void query(Channel channel, Right right) throws Exception {
        QueryByChaincodeRequest req = client.newQueryProposalRequest();
        ChaincodeID cid = ChaincodeID.newBuilder().setName(Config.CHAINCODENAME).setVersion(Config.CHAINCODEVERSION).build();
        req.setChaincodeID(cid);
        req.setFcn("query");
        req.setArgs(new String[]{
                right.getName(),
                right.getAuthor(),
                right.getPress()
        });
        System.out.println("Querying for " + right.getName());
        Collection<ProposalResponse> resps = channel.queryByChaincode(req);
        for (ProposalResponse resp : resps) {
            String payload = new String(resp.getChaincodeActionResponsePayload());
            logger.debug("response: " + payload);
        }
    }

}
