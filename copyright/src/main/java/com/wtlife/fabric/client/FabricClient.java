package com.wtlife.fabric.client;

import com.wtlife.fabric.bean.Right;
import com.wtlife.fabric.org.Org;
import com.wtlife.fabric.config.Config;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class FabricClient {
    private static final long PROPOSAL_WAIT_TIME = 60000 * 5;
    private static final String EXPECTED_EVENT_NAME = "event";
    private static final byte[] EXPECTED_EVENT_DATA = "!".getBytes(UTF_8);
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

    public static void regist(Channel channel, Right right) throws InvalidArgumentException, ProposalException, UnsupportedEncodingException {
        TransactionProposalRequest req = client.newTransactionProposalRequest();
        req.setChaincodeID(cid);
        req.setFcn("regist");
        req.setProposalWaitTime(PROPOSAL_WAIT_TIME);
        req.setArgs(right.toStringArray());

        Map<String, byte[]> tm = new HashMap<>();
        tm.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8));
        tm.put("method", "TransactionProposalRequest".getBytes(UTF_8));
        tm.put("result", ":)".getBytes(UTF_8));
        tm.put(EXPECTED_EVENT_NAME, EXPECTED_EVENT_DATA);
        req.setTransientMap(tm);

        Collection<ProposalResponse> resps = channel.sendTransactionProposal(req);
        for (ProposalResponse resp : resps) {
            if (resp.getStatus() == ProposalResponse.Status.SUCCESS) {
                System.out.format("Registing the work:%s \n", req.getArgs());
                System.out.format("Successful transaction proposal response Txid: %s from peer %s\n", resp.getTransactionID(), resp.getPeer());
                byte[] x = resp.getChaincodeActionResponsePayload();
                String payload = null;
                if (x != null) {
                    payload = new String(x, "UTF-8");
                }
                System.out.format("Invoke transanction prosal response Txid: %s", payload);
            } else {
                throw new RuntimeException(resp.getMessage());
            }

        }
    }

    public static void query(Channel channel, Right right) throws Exception {
        QueryByChaincodeRequest req = client.newQueryProposalRequest();
        ChaincodeID cid = ChaincodeID.newBuilder().setName(Config.CHAINCODENAME).setVersion(Config.CHAINCODEVERSION).build();
        req.setChaincodeID(cid);
        req.setFcn("queryRightByName");
        req.setArgs(new String[]{
                right.getName(),
                right.getAuthor(),
                right.getPress()
        });
        System.out.println("Querying for " + right.getName());
        Collection<ProposalResponse> resps = channel.queryByChaincode(req);
        for (ProposalResponse resp : resps) {

            byte[] x = resp.getChaincodeActionResponsePayload();
            String payload = null;
            if (x != null) {
                payload = new String(x, "UTF-8");
            }
            System.out.format("response:%s \n", payload);
        }
    }
}