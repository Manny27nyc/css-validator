// $Id$
// @author Yves Lafon <ylafon@w3.org>

// (c) COPYRIGHT MIT, ERCIM and Keio University, 2012.
// Please first read the full copyright statement in file COPYRIGHT.html
package org.w3c.css.properties.css3;

import org.w3c.css.properties.css.CssProperty;
import org.w3c.css.util.ApplContext;
import org.w3c.css.util.InvalidParamException;
import org.w3c.css.values.CssCheckableValue;
import org.w3c.css.values.CssExpression;
import org.w3c.css.values.CssIdent;
import org.w3c.css.values.CssTypes;
import org.w3c.css.values.CssValue;
import org.w3c.css.values.CssValueList;

import java.util.ArrayList;

import static org.w3c.css.values.CssOperator.SPACE;

/**
 * @spec http://www.w3.org/TR/2012/CR-css3-background-20120417/#border-width
 */
public class CssBorderWidth extends org.w3c.css.properties.css.CssBorderWidth {

    public static CssIdent allowed_values[];

    static {
        allowed_values = new CssIdent[3];
        allowed_values[0] = CssIdent.getIdent("thin");
        allowed_values[1] = CssIdent.getIdent("medium");
        allowed_values[2] = CssIdent.getIdent("thick");
    }

    /*
     * Get the cached ident if it matches null otherwise
     */
    static CssIdent getMatchingIdent(CssIdent ident) {
        for (CssIdent id : allowed_values) {
            if (id.equals(ident)) {
                return id;
            }
        }
        return null;
    }

    /**
     * Create a new CssBorderWidth
     */
    public CssBorderWidth() {
        value = initial;
        top = new CssBorderTopWidth();
        right = new CssBorderRightWidth();
        bottom = new CssBorderBottomWidth();
        left = new CssBorderLeftWidth();
    }

    /**
     * Set the value of the property<br/>
     * Does not check the number of values
     *
     * @param expression The expression for this property
     * @throws org.w3c.css.util.InvalidParamException
     *          The expression is incorrect
     */
    public CssBorderWidth(ApplContext ac, CssExpression expression)
            throws InvalidParamException {
        this(ac, expression, false);
    }

    /**
     * Set the value of the property
     *
     * @param expression The expression for this property
     * @param check      set it to true to check the number of values
     * @throws org.w3c.css.util.InvalidParamException
     *          The expression is incorrect
     */
    public CssBorderWidth(ApplContext ac, CssExpression expression,
                          boolean check) throws InvalidParamException {
        if (check && expression.getCount() > 4) {
            throw new InvalidParamException("unrecognize", ac);
        }
        setByUser();
        CssValue val;
        char op;

        ArrayList<CssValue> res = new ArrayList<CssValue>();
        while (res.size() < 4 && !expression.end()) {
            val = expression.getValue();
            op = expression.getOperator();

            switch (val.getType()) {
                case CssTypes.CSS_NUMBER:
                    val.getCheckableValue().checkEqualsZero(ac, this);
                    res.add(val);
                    break;
                case CssTypes.CSS_LENGTH:
                    CssCheckableValue length = val.getCheckableValue();
                    length.checkPositiveness(ac, this);
                    res.add(val);
                    break;
                case CssTypes.CSS_IDENT:
                    if (inherit.equals(val)) {
                        res.add(inherit);
                        break;
                    }
                    CssIdent match = getMatchingIdent((CssIdent) val);
                    if (match == null) {
                        throw new InvalidParamException("value", expression.getValue(),
                                getPropertyName(), ac);
                    }
                    res.add(match);
                    break;
                default:
                    throw new InvalidParamException("unrecognize", ac);
            }
            expression.next();
            if (op != SPACE) {
                throw new InvalidParamException("operator",
                        Character.toString(op),
                        ac);
            }
        }
        // check that inherit is alone
        if (res.size() > 1 && res.contains(inherit)) {
            throw new InvalidParamException("unrecognize", ac);
        }
        value = (res.size() == 1) ? res.get(0) : new CssValueList(res);

        // now assign the computed values...
        top = new CssBorderTopWidth();
        right = new CssBorderRightWidth();
        bottom = new CssBorderBottomWidth();
        left = new CssBorderLeftWidth();

        switch (res.size()) {
            case 1:
                top.value = left.value = right.value = bottom.value = res.get(0);
                break;
            case 2:
                top.value = bottom.value = res.get(0);
                right.value = left.value = res.get(1);
                break;
            case 3:
                top.value = res.get(0);
                right.value = left.value = res.get(1);
                bottom.value = res.get(2);
                break;
            case 4:
                top.value = res.get(0);
                right.value = res.get(1);
                bottom.value = res.get(2);
                left.value = res.get(3);
                break;
            default:
                // can't happen
                throw new InvalidParamException("unrecognize", ac);
        }
        shorthand = true;
    }


    /**
     * Check the border-*-width and returns a value.
     * It makes sense to do it only once for all the sides, so by having the code here.
     */
    protected static CssValue parseBorderSideWidth(ApplContext ac, CssExpression expression,
                                                   boolean check, CssProperty caller)
            throws InvalidParamException {
        if (check && expression.getCount() > 1) {
            throw new InvalidParamException("unrecognize", ac);
        }
        CssValue retval = null;
        CssValue val = expression.getValue();
        switch (val.getType()) {
            case CssTypes.CSS_NUMBER:
                val.getCheckableValue().checkEqualsZero(ac, caller);
                retval = val;
                break;
            case CssTypes.CSS_LENGTH:
                CssCheckableValue length = val.getCheckableValue();
                length.checkPositiveness(ac, caller);
                retval = val;
                break;
            case CssTypes.CSS_IDENT:
                if (inherit.equals(val)) {
                    retval = inherit;
                } else {
                    retval = getMatchingIdent((CssIdent) val);
                }
                if (retval == null) {
                    throw new InvalidParamException("value", expression.getValue(),
                            caller.getPropertyName(), ac);
                }
                break;
            default:
                throw new InvalidParamException("unrecognize", ac);
        }
        expression.next();
        return retval;
    }
}
