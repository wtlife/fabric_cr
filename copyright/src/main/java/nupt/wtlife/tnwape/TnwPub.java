package nupt.wtlife.tnwape;


import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

public class TnwPub {
    public Pairing p;
    /**
     * G1
     */
    public Element g;
    public Element h;

    /**
     * G2
     */
    public Element gp;

    /**
     * GT
     */
    public Element g_hat_alpha;
    public String pairingDesc;
}
