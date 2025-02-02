// $Id$
//
// (c) COPYRIGHT 1995-2012  World Wide Web Consortium (MIT, ERCIM, Keio University)
// Please first read the full copyright statement at
// http://www.w3.org/Consortium/Legal/copyright-software-19980720
package org.w3c.css.properties.css3;

import org.w3c.css.util.ApplContext;
import org.w3c.css.util.InvalidParamException;
import org.w3c.css.values.CssExpression;
import org.w3c.css.values.CssTypes;
import org.w3c.css.values.CssValueList;

/**
 * @spec http://www.w3.org/TR/2012/CR-css3-background-20120417/#border-top-left-radius
 * @see CssBorderRadius
 */
public class CssBorderTopLeftRadius extends org.w3c.css.properties.css.CssBorderTopLeftRadius {

    /**
     * Create new CssBorderTopLeftRadius
     */
    public CssBorderTopLeftRadius() {
        value = initial;
    }

    /**
     * Create new CssBorderTopLeftRadius
     *
     * @param expression The expression for this property
     * @throws InvalidParamException Values are incorrect
     */
    public CssBorderTopLeftRadius(ApplContext ac, CssExpression expression,
                                  boolean check) throws InvalidParamException {
        setByUser();
        value = CssBorderRadius.parseBorderCornerRadius(ac, expression, check, this);
        if (value.getType() == CssTypes.CSS_VALUE_LIST) {
            CssValueList vl = (CssValueList) value;
            h_radius = vl.get(0);
            v_radius = vl.get(1);
        } else {
            h_radius = v_radius = value;
        }
    }

}
