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

import static org.w3c.css.values.CssOperator.SPACE;

/**
 * @spec http://www.w3.org/TR/2012/CR-css3-background-20120417/#border
 */
public class CssBorder extends org.w3c.css.properties.css.CssBorder {

    /**
     * Create a new CssBorder
     */
    public CssBorder() {
        value = initial;
    }

    /**
     * Set the value of the property<br/>
     * Does not check the number of values
     *
     * @param expression The expression for this property
     * @throws org.w3c.css.util.InvalidParamException
     *          The expression is incorrect
     */
    public CssBorder(ApplContext ac, CssExpression expression)
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
    public CssBorder(ApplContext ac, CssExpression expression,
                     boolean check) throws InvalidParamException {
        // great, it's the same thing as one side!
        CssValueList valueList = new CssValueList();

        SideValues values = parseBorderSide(ac, expression, check, this);
        shorthand = true;
        if (values.color != null) {
            valueList.add(values.color);
            borderColor = new CssBorderColor();
            borderColor.bottom = new CssBorderBottomColor();
            borderColor.bottom.value = values.color;
            borderColor.top = new CssBorderTopColor();
            borderColor.top.value = values.color;
            borderColor.left = new CssBorderLeftColor();
            borderColor.left.value = values.color;
            borderColor.right = new CssBorderRightColor();
            borderColor.right.value = values.color;
        }
        if (values.style != null) {
            valueList.add(values.style);
            borderStyle = new CssBorderStyle();
            borderStyle.bottom = new CssBorderBottomStyle();
            borderStyle.bottom.value = values.style;
            borderStyle.top = new CssBorderTopStyle();
            borderStyle.top.value = values.style;
            borderStyle.left = new CssBorderLeftStyle();
            borderStyle.left.value = values.style;
            borderStyle.right = new CssBorderRightStyle();
            borderStyle.right.value = values.style;
        }
        if (values.width != null) {
            valueList.add(values.width);
            borderWidth = new CssBorderWidth();
            borderWidth.bottom = new CssBorderBottomWidth();
            borderWidth.bottom.value = values.width;
            borderWidth.top = new CssBorderTopWidth();
            borderWidth.top.value = values.width;
            borderWidth.left = new CssBorderLeftWidth();
            borderWidth.left.value = values.width;
            borderWidth.right = new CssBorderRightWidth();
            borderWidth.right.value = values.width;
        }
        value = valueList;
    }

    /**
     * Check the border-* and returns a value.
     * It makes sense to do it only once for all the sides, so by having the code here.
     */
    protected static SideValues parseBorderSide(ApplContext ac, CssExpression expression, boolean check, CssProperty caller) throws InvalidParamException {
        if (check && expression.getCount() > 3) {
            throw new InvalidParamException("unrecognize", ac);
        }
        CssValue _width = null;
        CssValue _style = null;
        CssValue _color = null;
        CssExpression nex;

        CssValue val;
        char op;

        while (!expression.end()) {
            val = expression.getValue();
            op = expression.getOperator();

            switch (val.getType()) {
                case CssTypes.CSS_NUMBER:
                    CssCheckableValue number = val.getCheckableValue();
                    number.checkEqualsZero(ac, caller);
                    _width = val;
                    break;
                case CssTypes.CSS_LENGTH:
                    CssCheckableValue length = val.getCheckableValue();
                    length.checkPositiveness(ac, caller);
                    _width = val;
                    break;
                case CssTypes.CSS_HASH_IDENT:
                    org.w3c.css.values.CssColor c = new org.w3c.css.values.CssColor();
                    c.setShortRGBColor(ac, val.toString());
                    _color = c;
                    break;
                case CssTypes.CSS_COLOR:
                    _color = val;
                    break;
                case CssTypes.CSS_IDENT:
                    CssIdent id = (CssIdent) val;
                    if (transparent.equals(id)) {
                        _color = transparent;
                        break;
                    }
                    if (inherit.equals(id)) {
                        if (expression.getCount() > 1) {
                            throw new InvalidParamException("unrecognize", ac);
                        }
                        _width = inherit;
                        _style = inherit;
                        _color = inherit;
                        break;
                    }
                    CssIdent match = CssBorderWidth.getMatchingIdent(id);
                    if (match != null) {
                        _width = match;
                    } else {
                        match = CssBorderStyle.getMatchingIdent(id);
                        if (match != null) {
                            _style = match;
                        } else {
                            // if not a width or a style, fail if it's not a proper color
                            nex = new CssExpression();
                            nex.addValue(val);
                            CssColor cssColor = new CssColor(ac, nex, false);
                            _color = cssColor.color;
                        }
                    }
                    break;
                case CssTypes.CSS_FUNCTION:
                    nex = new CssExpression();
                    nex.addValue(val);
                    CssColor cssColor = new CssColor(ac, nex, false);
                    _color = cssColor.color;
                    break;
                default:
                    throw new InvalidParamException("value", val.toString(),
                            caller.getPropertyName(), ac);
            }
            expression.next();
            if (op != SPACE) {
                throw new InvalidParamException("operator",
                        Character.toString(op),
                        ac);
            }
        }
        return new SideValues(_width, _style, _color);
    }

    // small wrapper to return values...
    protected static class SideValues {
        CssValue width;
        CssValue style;
        CssValue color;

        SideValues(CssValue width, CssValue style, CssValue color) {
            this.width = width;
            this.style = style;
            this.color = color;

        }
    }
}
