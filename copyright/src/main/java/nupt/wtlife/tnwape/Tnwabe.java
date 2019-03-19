package nupt.wtlife.tnwape;
import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.DefaultCurveParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Map;

public class Tnwabe {
    private static String curveParams = "type a\n"
            + "q 87807107996633125224377819847540498158068831994142082"
            + "1102865339926647563088022295707862517942266222142315585"
            + "8769582317459277713367317481324925129998224791\n"
            + "h 12016012264891146079388821366740534204802954401251311"
            + "822919615131047207289359704531102844802183906537786776\n"
            + "r 730750818665451621361119245571504901405976559617\n"
            + "exp2 159\n" + "exp1 107\n" + "sign1 1\n" + "sign0 1\n";

    public static void setup(TnwPub pk, TnwMsk msk){

        Element alpha, beta_inv;

        CurveParameters params = new DefaultCurveParameters()
                .load(new ByteArrayInputStream(curveParams.getBytes()));

        pk.pairingDesc = curveParams;
        pk.p = PairingFactory.getPairing(params);
        Pairing pairing = pk.p;

        pk.g    = pairing.getG1().newElement().setToRandom();
        pk.gp   = pairing.getG2().newElement().setToRandom();
        alpha    = pairing.getZr().newElement().setToRandom();
        msk.beta = pairing.getZr().newElement().setToRandom();

        msk.g_alpha=pk.gp.duplicate().powZn(alpha);
        pk.h=pk.g.duplicate().powZn(msk.beta);

        pk.g_hat_alpha = pairing.pairing(pk.g, msk.g_alpha);
    }

    public static TnwSk keyGen(TnwAttr[] attrs, TnwPub pk, TnwMsk msk) throws NoSuchAlgorithmException {
        TnwSk sk = new TnwSk();
        Element g_r,r,beta_inv;
        Pairing pairing;

        pairing = pk.p;
        sk.L = pairing.getG2().newElement();
        g_r=pairing.getG2().newElement();
        r = pairing.getZr().newElement();
        beta_inv=pairing.getZr().newElement();

        r.setToRandom();
        g_r=pk.gp.duplicate();
        g_r.powZn(r);
        sk.K = g_r;

        sk.L=msk.g_alpha.duplicate();
        sk.L.mul(g_r);

        beta_inv = msk.beta.duplicate();
        beta_inv.invert();
        sk.L.powZn(beta_inv);

        sk.attrs=attrs;

        int wa = getTotalWeight(attrs);
        sk.Dx = new Element[wa];
        sk.Dy = new Element[wa];


        int z = 0;
        int w ;
        Element ri,dxi,dyi;
        for (TnwAttr attr:attrs) {
            w=attr.weight;
            for (int i = z; i <w+z ; i++) {
                ri = pairing.getZr().newElement();
                dxi = pairing.getG2().newElement();
                dyi=pk.g.duplicate();
                ri.setToRandom();

                elementFromString(dxi,attr.name);
                dxi.powZn(ri);

                dyi.powZn(ri);

                sk.Dx[i]=dxi;
                sk.Dy[i]=dyi;
            }
            z+=w;
        }

        return sk;
    }

    public static TnwCphKey enc(TnwPub pk, TnwPolicy policy, String message) throws NoSuchAlgorithmException {
        TnwCphKey keyCph= new TnwCphKey();
        TnwCiphertext ciphertext = new TnwCiphertext();
        Element s,m;
        Pairing pairing = pk.p;

        s = pairing.getZr().newElement().setToRandom();
        m = pairing.getGT().newElement().setToRandom();

        ciphertext.C= pk.g_hat_alpha.duplicate().powZn(s);
        ciphertext.C.mul(m);
        ciphertext.C1 = pk.h.duplicate().powZn(s);

        ciphertext.policy=policy;
        fillPolicy(pk,s,ciphertext.policy);


        keyCph.key=m;
        keyCph.cph=ciphertext;

        return keyCph;
    }


    public static TnwElementBoolean dec(TnwCiphertext ciphertext, TnwSk sk, TnwPub pk){
        Pairing pairing =pk.p;
        Element t,m;
        TnwElementBoolean bool = new TnwElementBoolean();

        t=pk.p.getGT().newElement();
        m=pk.p.getGT().newElement();

        ifSatisfy(ciphertext.policy,sk);
        if(!ciphertext.policy.satisfy){
            System.err.println("Not satisfy the policy!");
            return null;
        }

        pickSatisfy(ciphertext.policy,sk);

        pre_dec(t,ciphertext.policy,sk,pk);
        t.invert();
        m=pairing.pairing(sk.L,ciphertext.C1);
        m.mul(t);

        m.invert();
        m.mul(ciphertext.C);

        bool.b=true;
        bool.e=m;
        return bool;
    }

    private static void pre_dec(Element r, TnwPolicy policy, TnwSk sk, TnwPub pk) {
        Element one;
        one = pk.p.getZr().newOneElement();
        one.setToOne();
        r.setToOne();

        decPair(r, one, policy, sk, pk);
    }

    private static void decPair(Element r, Element one, TnwPolicy policy, TnwSk sk, TnwPub pk) {
        Pairing pairing = pk.p;
        Element s,t,exp;
        s=pk.p.getGT().newElement();
        t=pk.p.getGT().newElement();
        exp=pk.p.getZr().newElement();

        exp.setToOne();
        Element _Cx,_Cy,_Dx,_Dy;
        _Cx=pk.p.getG2().newElement().setToOne();
        _Cy=pk.p.getG1().newElement().setToOne();
        _Dx=pk.p.getG2().newElement().setToOne();
        _Dy=pk.p.getG1().newElement().setToOne();

        for (int i = 0; i <sk.Dx.length ; i++) {
            lagrangeCoef(exp,policy.satl,i+1);
            _Cx.mul(policy.Cx.get(i).duplicate().powZn(exp));
            _Cy.mul(policy.Cy.get(i).duplicate().powZn(exp));
            _Dx.mul(sk.Dx[i].duplicate());
            _Dy.mul(sk.Dy[i].duplicate());
        }
        _Dx.mul(sk.K.duplicate());
        s=pairing.pairing(_Cy,_Dx);
        t=pairing.pairing(_Cx,_Dy);
        t.invert();
        s.mul(t);
        r.mul(s);
    }


    /**
     * Utils
     */
    private static void pickSatisfy(TnwPolicy policy, TnwSk sk) {
        for (int i = 0; i <policy.t ; i++) {
            policy.satl.add(new Integer(i+1));
        }
    }

    private static int getTotalWeight(TnwAttr[] attrs) {
        int sum=0;
        for (int i = 0; i <attrs.length ; i++) {
            sum+= attrs[i].getWeight();
        }
        return sum;
    }

    private static void ifSatisfy(TnwPolicy policy, TnwSk sk) {
        int w = sk.Dx.length;
        if (w>=policy.t){
            for (int i = 0; i <sk.attrs.length ; i++) {
                if (!(sk.attrs[i].weight>=policy.kx.get(sk.attrs[i].type))){
                    policy.satisfy=false;
                    return;
                }
            }
        }
        policy.satisfy=true;
    }



    public static void fillPolicy(TnwPub pk, Element s, TnwPolicy policy) throws NoSuchAlgorithmException {
        Element r, t,cxi,cyi;
        Pairing pairing = pk.p;
        r = pairing.getZr().newElement();
        t = pairing.getZr().newElement();
        policy.q = randPoly(policy.t-1,s);

        for (int i = 0; i < policy.n ; i++) {
            r.set(i+1);
            evalPoly(t,policy.q,r);
            policy.q.qx.add(t.duplicate());
        }



        int w;
        int z =0;
        for (Map.Entry<String,Integer> kxs:policy.kx.entrySet()) {
            w = kxs.getValue();
            for (int i = z; i <w+z ; i++) {
                cxi = pairing.getG2().newElement();
                elementFromString(cxi,kxs.getKey());
                policy.Cx.add(cxi.powZn(policy.q.qx.get(i)));

                cyi=pk.g.duplicate();
                policy.Cy.add(cyi.powZn(policy.q.qx.get(i)));
            }
            z+=w;
        }

    }

    private static TnwPolynomial randPoly(int deg, Element zeroVal) {
        int i;
        TnwPolynomial q = new TnwPolynomial();
        q.deg = deg;
        q.coef = new Element[deg + 1];

        for (i = 0; i < deg + 1; i++)
            q.coef[i] = zeroVal.duplicate();

        q.coef[0].set(zeroVal);

        for (i = 1; i < deg + 1; i++)
            q.coef[i].setToRandom();

        return q;
    }

    private static void lagrangeCoef(Element r, ArrayList<Integer> s, int i) {
        int j, k;
        Element t;

        t = r.duplicate();

        r.setToOne();
        for (k = 0; k < s.size(); k++) {
            j = s.get(k).intValue();
            if (j == i)
                continue;
            t.set(-j);
            r.mul(t); /* num_muls++; */
            t.set(i - j);
            t.invert();
            r.mul(t); /* num_muls++; */
        }
    }

    public static void evalPoly(Element r, TnwPolynomial q, Element x) {
        int i;
        Element s, t;

        s = r.duplicate();
        t = r.duplicate();

        r.setToZero();
        t.setToOne();

        for (i = 0; i < q.deg + 1; i++) {
            /* r += q->coef[i] * t */
            s = q.coef[i].duplicate();
            s.mul(t);
            r.add(s);

            /* t *= x */
            t.mul(x);
        }
    }

    private static void elementFromString(Element h, String s)
            throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] digest = md.digest("s".getBytes());
        md.digest(s.getBytes());
        h.setFromHash(digest, 0, digest.length);
    }
}
