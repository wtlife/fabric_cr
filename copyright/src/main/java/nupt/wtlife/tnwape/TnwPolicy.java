package nupt.wtlife.tnwape;

import it.unisa.dia.gas.jpbc.Element;

import java.util.ArrayList;
import java.util.TreeMap;

public class TnwPolicy {
    /**
     * threshold
     */
    public int t;

    /**
     * kR secret shares
     */
    public int n;

    /**
     * kx TypeName -> threshold
     */
    public TreeMap<String,Integer> kx = new TreeMap<String, Integer>() ;
    public TnwPolynomial q;

    /**
     * Only used for enc
     */
    public ArrayList<Element> Cx=new ArrayList<Element>();
    public ArrayList<Element> Cy=new ArrayList<Element>();

    public Boolean satisfy;
    ArrayList<Integer> satl = new ArrayList<Integer>();
}
