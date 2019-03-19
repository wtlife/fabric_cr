package nupt.wtlife.tnwape;

import it.unisa.dia.gas.jpbc.Element;

import java.util.ArrayList;
public class TnwPolynomial {
    int deg;

    /**
     * coefficients from [0] x^0 to [deg] x^deg G_T (of length deg + 1)
    */
    Element[] coef;
    ArrayList<Element> qx=new ArrayList<Element>();
}
