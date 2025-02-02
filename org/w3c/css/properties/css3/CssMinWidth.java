//
// Author: Yves Lafon <ylafon@w3.org>
//
// (c) COPYRIGHT MIT, ERCIM, Keio, Beihang, 2016.
// Please first read the full copyright statement in file COPYRIGHT.html
package org.w3c.css.properties.css3;

import org.w3c.css.properties.css.CssProperty;
import org.w3c.css.util.ApplContext;
import org.w3c.css.util.InvalidParamException;
import org.w3c.css.values.CssCheckableValue;
import org.w3c.css.values.CssExpression;
import org.w3c.css.values.CssFunction;
import org.w3c.css.values.CssIdent;
import org.w3c.css.values.CssTypes;
import org.w3c.css.values.CssValue;

/**
 * @spec https://www.w3.org/TR/2021/WD-css-sizing-3-20210317/#propdef-min-width
 * @spec https://www.w3.org/TR/2021/WD-css-sizing-4-20210520/#sizing-values
 */
public class CssMinWidth extends org.w3c.css.properties.css.CssMinWidth {

    public static final CssIdent[] allowed_values;
    public static final String fit_content_func = "fit-content";

    static {
        String[] _allowed_values = {"auto", "max-content", "min-content",
                // following from sizing-4
                "stretch", "fit-content", "contain"};
        allowed_values = new CssIdent[_allowed_values.length];
        int i = 0;
        for (String s : _allowed_values) {
            allowed_values[i++] = CssIdent.getIdent(s);
        }
    }

    public static CssIdent getAllowedIdent(CssIdent ident) {
        for (CssIdent id : allowed_values) {
            if (id.equals(ident)) {
                return id;
            }
        }
        return null;
    }

    /**
     * Create a new CssMinWidth
     */
    public CssMinWidth() {
        value = initial;
    }

    /**
     * Creates a new CssMinWidth
     *
     * @param expression The expression for this property
     * @throws org.w3c.css.util.InvalidParamException Expressions are incorrect
     */
    public CssMinWidth(ApplContext ac, CssExpression expression, boolean check)
            throws InvalidParamException {
        if (check && expression.getCount() > 1) {
            throw new InvalidParamException("unrecognize", ac);
        }
        setByUser();
        value = parseMinWidth(ac, expression, this);
    }

    public CssMinWidth(ApplContext ac, CssExpression expression)
            throws InvalidParamException {
        this(ac, expression, false);
    }

    public static CssValue parseMinWidth(ApplContext ac, CssExpression expression,
                                         CssProperty caller)
            throws InvalidParamException {
        CssValue val = expression.getValue();
        CssValue v = null;

        switch (val.getType()) {
            case CssTypes.CSS_NUMBER:
                val.getCheckableValue().checkEqualsZero(ac, caller);
                v = val;
                break;
            case CssTypes.CSS_LENGTH:
            case CssTypes.CSS_PERCENTAGE:
                CssCheckableValue l = val.getCheckableValue();
                l.checkPositiveness(ac, caller);
                v = val;
                break;
            case CssTypes.CSS_FUNCTION:
                v = parseFunctionValue(ac, val, caller);
                break;
            case CssTypes.CSS_IDENT:
                if (inherit.equals(val)) {
                    v = inherit;
                } else {
                    CssIdent id = getAllowedIdent((CssIdent) val);
                    if (id != null) {
                        v = id;
                    } else {
                        throw new InvalidParamException("unrecognize", ac);
                    }
                }
                break;
            default:
                throw new InvalidParamException("value", expression.getValue(),
                        caller.getPropertyName(), ac);
        }
        expression.next();
        return v;
    }

    protected static CssValue parseFunctionValue(ApplContext ac, CssValue value,
                                                 CssProperty caller)
            throws InvalidParamException {
        CssFunction function = (CssFunction) value;
        if (!fit_content_func.equalsIgnoreCase(function.getName())) {
            throw new InvalidParamException("value", value.toString(),
                    caller.getPropertyName(), ac);
        }
        CssExpression expression = function.getParameters();
        if (expression.getCount() > 1) {
            throw new InvalidParamException("unrecognize", ac);
        }
        CssValue val = expression.getValue();
        switch (val.getType()) {
            case CssTypes.CSS_NUMBER:
                val.getCheckableValue().checkEqualsZero(ac, caller);
                break;
            case CssTypes.CSS_LENGTH:
            case CssTypes.CSS_PERCENTAGE:
                CssCheckableValue l = val.getCheckableValue();
                l.checkPositiveness(ac, caller);
                break;
            default:
                throw new InvalidParamException("value", expression.getValue(),
                        caller.getPropertyName(), ac);
        }
        return value;
    }
}

