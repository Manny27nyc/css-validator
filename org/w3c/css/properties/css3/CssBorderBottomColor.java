// $Id$
// Author: Yves Lafon <ylafon@w3.org>
//
// (c) COPYRIGHT MIT, ERCIM and Keio University, 2012.
// Please first read the full copyright statement in file COPYRIGHT.html
package org.w3c.css.properties.css3;

import org.w3c.css.util.ApplContext;
import org.w3c.css.util.InvalidParamException;
import org.w3c.css.values.CssExpression;

/**
 * @spec http://www.w3.org/TR/2012/CR-css3-background-20120417/#border-bottom-color
 * @see CssBorderColor
 */
public class CssBorderBottomColor extends org.w3c.css.properties.css.CssBorderBottomColor {

    /**
     * Create a new CssBorderBottomColor
     */
    public CssBorderBottomColor() {
        value = initial;
    }

    /**
     * Creates a new CssBorderBottomColor
     *
     * @param expression The expression for this property
     * @throws org.w3c.css.util.InvalidParamException
     *          Expressions are incorrect
     */
    public CssBorderBottomColor(ApplContext ac, CssExpression expression, boolean check)
            throws InvalidParamException {
        value = CssBorderColor.parseBorderSideColor(ac, expression, check, this);
    }

    public CssBorderBottomColor(ApplContext ac, CssExpression expression)
            throws InvalidParamException {
        this(ac, expression, false);
    }
}

