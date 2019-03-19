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

public class MTnwabe {
    private static String curveParams = "type a\n"
            + "q 87807107996633125224377819847540498158068831994142082"
            + "1102865339926647563088022295707862517942266222142315585"
            + "8769582317459277713367317481324925129998224791\n"
            + "h 12016012264891146079388821366740534204802954401251311"
            + "822919615131047207289359704531102844802183906537786776\n"
            + "r 730750818665451621361119245571504901405976559617\n"
            + "exp2 159\n" + "exp1 107\n" + "sign1 1\n" + "sign0 1\n";

        public static void setup(TnwPub pk, TnwMsk msk){
            Element alpha ;

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

        public static MTnwISk inkeyGen(TnwAttr[] attrs, TnwPub pk) throws NoSuchAlgorithmException {
            MTnwISk isk = new MTnwISk();
            Element r,kid,g_kid;
            Pairing pairing= pk.p;
            kid = pairing.getZr().newElement().setToRandom();
            g_kid=pk.gp.duplicate();
            g_kid.powZn(kid);

            r = pairing.getZr().newElement().setToRandom();

            int wa = getTotalWeight(attrs);
            isk.Dix = new Element[wa];
            isk.Diy = new Element[wa];
            isk.attrs=attrs;

            int z = 0;
            int w ;
            Element ri,_dxi,_dyi;
            for (TnwAttr attr:attrs) {
                w=attr.weight;
                for (int i = z; i <w+z ; i++) {
                    ri = pairing.getZr().newElement();
                    _dxi = pairing.getG2().newElement();
                    _dyi=pk.g.duplicate();
                    ri.setToRandom();

                    elementFromString(_dxi,attr.name);
                    _dxi.powZn(ri);
                    _dxi.powZn(kid);

                    _dyi.powZn(ri);
                    _dyi.powZn(kid);

                    isk.Dix[i]=_dxi;
                    isk.Diy[i]=_dyi;
                }
                z+=w;
            }

            isk.g_kid=g_kid.duplicate();
            isk.r=r.duplicate();
            return isk;
        }

        public static TnwSk keyGen(TnwPub pk,TnwMsk msk,MTnwISk isk) throws NoSuchAlgorithmException {
            TnwSk sk = new TnwSk();
            Element gk_r,r,beta_inv;
            Pairing pairing;
            pairing = pk.p;
            sk.attrs=isk.attrs;

            gk_r=isk.g_kid.duplicate();
            r = isk.r.duplicate();
            sk.K=gk_r.powZn(r);

            beta_inv = msk.beta.duplicate();
            beta_inv.invert();

            sk.L = pairing.getG2().newElement();
            sk.L=msk.g_alpha.duplicate();
            sk.L.mul(sk.K.duplicate());
            sk.L.powZn(beta_inv);

            int wa = isk.Dix.length;
            sk.Dx = new Element[wa];
            sk.Dy = new Element[wa];

            Element dxi,dyi;
                for (int i = 0; i <wa ; i++) {
                    dxi = isk.Dix[i].duplicate();
                    dyi = isk.Diy[i].duplicate();

                    sk.Dx[i]=dxi;
                    sk.Dy[i]=dyi;
                }
            return sk;
        }

        public static TnwCphKey enc(TnwPub pk, TnwPolicy policy) throws NoSuchAlgorithmException {
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

        public static int getTotalWeight(TnwAttr[] attrs) {
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
