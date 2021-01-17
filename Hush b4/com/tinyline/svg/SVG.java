// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.svg;

import com.tinyline.tiny2d.TinyPoint;
import com.tinyline.tiny2d.TinyVector;
import com.tinyline.tiny2d.TinyTransform;
import com.tinyline.tiny2d.TinyPath;
import com.tinyline.tiny2d.TinyNumber;
import com.tinyline.tiny2d.TinyColor;
import com.tinyline.tiny2d.TinyString;

public class SVG
{
    public static final int DATATYPE_UNKNOWN = 0;
    public static final int DATATYPE_COLOR = 1;
    public static final int DATATYPE_DASHARRAY = 2;
    public static final int DATATYPE_ENUM = 3;
    public static final int DATATYPE_NUMBER = 4;
    public static final int DATATYPE_NUMBERLIST = 5;
    public static final int DATATYPE_PATH = 6;
    public static final int DATATYPE_PATHLIST = 7;
    public static final int DATATYPE_POINTLIST = 8;
    public static final int DATATYPE_STRING = 9;
    public static final int DATATYPE_STRINGLIST = 10;
    public static final int DATATYPE_TRANSFORM = 11;
    public static final int DATATYPE_TIME = 12;
    public static final int DATATYPE_VIEWBOX = 14;
    public static final int ERR_OK = 0;
    public static final int ERR_NUMBER_FORMAT = 1;
    public static final int ERR_INVALID_ARG = 2;
    public static final int ERR_NOT_FOUND = 4;
    public static final int ERR_NOT_SUPPORTED = 8;
    public static final int ERR_HIERARCHY_REQUEST = 16;
    public static char[][] ELEMENTS;
    public static final int ELEM_A = 0;
    public static final int ELEM_ANIMATE = 1;
    public static final int ELEM_ANIMATECOLOR = 2;
    public static final int ELEM_ANIMATEMOTION = 3;
    public static final int ELEM_ANIMATETRANSFORM = 4;
    public static final int ELEM_CIRCLE = 5;
    public static final int ELEM_DEFS = 6;
    public static final int ELEM_DESC = 7;
    public static final int ELEM_ELLIPSE = 8;
    public static final int ELEM_FONT = 9;
    public static final int ELEM_FONT_FACE = 10;
    public static final int ELEM_FONT_FACE_NAME = 11;
    public static final int ELEM_FONT_FACE_SRC = 12;
    public static final int ELEM_FOREIGNOBJECT = 13;
    public static final int ELEM_G = 14;
    public static final int ELEM_GLYPH = 15;
    public static final int ELEM_HKERN = 16;
    public static final int ELEM_IMAGE = 17;
    public static final int ELEM_LINE = 18;
    public static final int ELEM_LINEARGRADIENT = 19;
    public static final int ELEM_METADATA = 20;
    public static final int ELEM_MISSING_GLYPH = 21;
    public static final int ELEM_MPATH = 22;
    public static final int ELEM_PATH = 23;
    public static final int ELEM_POLYGON = 24;
    public static final int ELEM_POLYLINE = 25;
    public static final int ELEM_RADIALGRADIENT = 26;
    public static final int ELEM_RECT = 27;
    public static final int ELEM_SET = 28;
    public static final int ELEM_STOP = 29;
    public static final int ELEM_SVG = 30;
    public static final int ELEM_SWITCH = 31;
    public static final int ELEM_TEXT = 32;
    public static final int ELEM_TITLE = 33;
    public static final int ELEM_USE = 34;
    public static final int ELEM_UNKNOWN = 35;
    public static final int ELEM_DOCUMENT = 300;
    public static char[][] ATTRIBUTES;
    public static final int ATT_ACCENT_HEIGHT = 0;
    public static final int ATT_ACCUMULATE = 1;
    public static final int ATT_ADDITIVE = 2;
    public static final int ATT_ALPHABETIC = 3;
    public static final int ATT_ARABIC_FORM = 4;
    public static final int ATT_ASCENT = 5;
    public static final int ATT_ATTRIBUTENAME = 6;
    public static final int ATT_ATTRIBUTETYPE = 7;
    public static final int ATT_BASEPROFILE = 8;
    public static final int ATT_BASELINE = 9;
    public static final int ATT_BBOX = 10;
    public static final int ATT_BEGIN = 11;
    public static final int ATT_BY = 12;
    public static final int ATT_CALCMODE = 13;
    public static final int ATT_CAP_HEIGHT = 14;
    public static final int ATT_COLOR = 15;
    public static final int ATT_COLOR_RENDERING = 16;
    public static final int ATT_CONTENT = 17;
    public static final int ATT_CX = 18;
    public static final int ATT_CY = 19;
    public static final int ATT_D = 20;
    public static final int ATT_DESCENT = 21;
    public static final int ATT_DISPLAY = 22;
    public static final int ATT_DUR = 23;
    public static final int ATT_END = 24;
    public static final int ATT_FILL = 25;
    public static final int ATT_FILL_OPACITY = 26;
    public static final int ATT_FILL_RULE = 27;
    public static final int ATT_FONT_FAMILY = 28;
    public static final int ATT_FONT_SIZE = 29;
    public static final int ATT_FONT_STRETCH = 30;
    public static final int ATT_FONT_STYLE = 31;
    public static final int ATT_FONT_VARIANT = 32;
    public static final int ATT_FONT_WEIGHT = 33;
    public static final int ATT_FROM = 34;
    public static final int ATT_G1 = 35;
    public static final int ATT_G2 = 36;
    public static final int ATT_GLYPH_NAME = 37;
    public static final int ATT_GRADIENTTRANSFORM = 38;
    public static final int ATT_GRADIENTUNITS = 39;
    public static final int ATT_HANGING = 40;
    public static final int ATT_HEIGHT = 41;
    public static final int ATT_HORIZ_ADV_X = 42;
    public static final int ATT_HORIZ_ORIGIN_X = 43;
    public static final int ATT_ID = 44;
    public static final int ATT_IDEOGRAPHIC = 45;
    public static final int ATT_K = 46;
    public static final int ATT_KEYPOINTS = 47;
    public static final int ATT_KEYSPLINES = 48;
    public static final int ATT_KEYTIMES = 49;
    public static final int ATT_LANG = 50;
    public static final int ATT_MATHEMATICAL = 51;
    public static final int ATT_MAX = 52;
    public static final int ATT_MIN = 53;
    public static final int ATT_NAME = 54;
    public static final int ATT_OFFSET = 55;
    public static final int ATT_OPACITY = 56;
    public static final int ATT_ORIGIN = 57;
    public static final int ATT_OVERLINE_POSITION = 58;
    public static final int ATT_OVERLINE_THICKNESS = 59;
    public static final int ATT_PANOSE_1 = 60;
    public static final int ATT_PATH = 61;
    public static final int ATT_PATHLENGTH = 62;
    public static final int ATT_POINTS = 63;
    public static final int ATT_PRESERVEASPECTRATIO = 64;
    public static final int ATT_R = 65;
    public static final int ATT_REPEATCOUNT = 66;
    public static final int ATT_REPEATDUR = 67;
    public static final int ATT_REQUIREDEXTENSIONS = 68;
    public static final int ATT_REQUIREDFEATURES = 69;
    public static final int ATT_RESTART = 70;
    public static final int ATT_ROTATE = 71;
    public static final int ATT_RX = 72;
    public static final int ATT_RY = 73;
    public static final int ATT_SLOPE = 74;
    public static final int ATT_SPREADMETHOD = 75;
    public static final int ATT_STEMH = 76;
    public static final int ATT_STEMV = 77;
    public static final int ATT_STOP_COLOR = 78;
    public static final int ATT_STOP_OPACITY = 79;
    public static final int ATT_STRIKETHROUGH_POSITION = 80;
    public static final int ATT_STRIKETHROUGH_THICKNESS = 81;
    public static final int ATT_STROKE = 82;
    public static final int ATT_STROKE_DASHARRAY = 83;
    public static final int ATT_STROKE_DASHOFFSET = 84;
    public static final int ATT_STROKE_LINECAP = 85;
    public static final int ATT_STROKE_LINEJOIN = 86;
    public static final int ATT_STROKE_MITERLIMIT = 87;
    public static final int ATT_STROKE_OPACITY = 88;
    public static final int ATT_STROKE_WIDTH = 89;
    public static final int ATT_SYSTEMLANGUAGE = 90;
    public static final int ATT_TARGET = 91;
    public static final int ATT_TEXT_ANCHOR = 92;
    public static final int ATT_TO = 93;
    public static final int ATT_TRANSFORM = 94;
    public static final int ATT_TYPE = 95;
    public static final int ATT_U1 = 96;
    public static final int ATT_U2 = 97;
    public static final int ATT_UNDERLINE_POSITION = 98;
    public static final int ATT_UNDERLINE_THICKNESS = 99;
    public static final int ATT_UNICODE = 100;
    public static final int ATT_UNICODE_RANGE = 101;
    public static final int ATT_UNITS_PER_EM = 102;
    public static final int ATT_VALUES = 103;
    public static final int ATT_VERSION = 104;
    public static final int ATT_VIEWBOX = 105;
    public static final int ATT_VISIBILITY = 106;
    public static final int ATT_WIDTH = 107;
    public static final int ATT_WIDTHS = 108;
    public static final int ATT_X = 109;
    public static final int ATT_X_HEIGHT = 110;
    public static final int ATT_X1 = 111;
    public static final int ATT_X2 = 112;
    public static final int ATT_XLINK_ACTUATE = 113;
    public static final int ATT_XLINK_ARCROLE = 114;
    public static final int ATT_XLINK_HREF = 115;
    public static final int ATT_XLINK_ROLE = 116;
    public static final int ATT_XLINK_SHOW = 117;
    public static final int ATT_XLINK_TITLE = 118;
    public static final int ATT_XLINK_TYPE = 119;
    public static final int ATT_XML_BASE = 120;
    public static final int ATT_XML_LANG = 121;
    public static final int ATT_XML_SPACE = 122;
    public static final int ATT_Y = 123;
    public static final int ATT_Y1 = 124;
    public static final int ATT_Y2 = 125;
    public static final int ATT_ZOOMANDPAN = 126;
    public static final int ATT_UNKNOWN = 127;
    public static char[][] VALUES;
    public static final int[] VAL_STROKEDASHARRAYNONE;
    public static final int[] VAL_STROKEDASHARRAYINHERIT;
    public static final TinyString VAL_DEFAULT_FONTFAMILY;
    static final int a = 3072;
    public static final int VAL_100 = 0;
    public static final int VAL_200 = 1;
    public static final int VAL_300 = 2;
    public static final int VAL_400 = 3;
    public static final int VAL_500 = 4;
    public static final int VAL_600 = 5;
    public static final int VAL_700 = 6;
    public static final int VAL_800 = 7;
    public static final int VAL_900 = 8;
    public static final int VAL_ALWAYS = 9;
    public static final int VAL_AUTO = 10;
    public static final int VAL_AUTO_REVERSE = 11;
    public static final int VAL_BEVEL = 12;
    public static final int VAL_BOLD = 13;
    public static final int VAL_BOLDER = 14;
    public static final int VAL_BUTT = 15;
    public static final int VAL_COLLAPSE = 16;
    public static final int VAL_CURRENTCOLOR = 17;
    public static final int VAL_DEFAULT = 18;
    public static final int VAL_DISABLE = 19;
    public static final int VAL_DISCRETE = 20;
    public static final int VAL_END = 21;
    public static final int VAL_EVENODD = 22;
    public static final int VAL_FREEZE = 23;
    public static final int VAL_HIDDEN = 24;
    public static final int VAL_INDEFINITE = 25;
    public static final int VAL_INHERIT = 26;
    public static final int VAL_INLINE = 27;
    public static final int VAL_ITALIC = 28;
    public static final int VAL_LIGHTER = 29;
    public static final int VAL_LINEAR = 30;
    public static final int VAL_MAGNIFY = 31;
    public static final int VAL_MIDDLE = 32;
    public static final int VAL_MITER = 33;
    public static final int VAL_NEVER = 34;
    public static final int VAL_NONE = 35;
    public static final int VAL_NONZERO = 36;
    public static final int VAL_NORMAL = 37;
    public static final int VAL_OBJECTBOUNDINGBOX = 38;
    public static final int VAL_OBLIQUE = 39;
    public static final int VAL_PACED = 40;
    public static final int VAL_PAD = 41;
    public static final int VAL_PRESERVE = 42;
    public static final int VAL_REFLECT = 43;
    public static final int VAL_REMOVE = 44;
    public static final int VAL_REPEAT = 45;
    public static final int VAL_REPLACE = 46;
    public static final int VAL_ROTATE = 47;
    public static final int VAL_ROUND = 48;
    public static final int VAL_SCALE = 49;
    public static final int VAL_SKEWX = 50;
    public static final int VAL_SKEWY = 51;
    public static final int VAL_SPLINE = 52;
    public static final int VAL_SQUARE = 53;
    public static final int VAL_START = 54;
    public static final int VAL_SUM = 55;
    public static final int VAL_TRANSLATE = 56;
    public static final int VAL_USERSPACEONUSE = 57;
    public static final int VAL_VISIBLE = 58;
    public static final int VAL_WHENNOTACTIVE = 59;
    public static final int VAL_XMIDYMID_MEET = 60;
    public static final int VAL_UNKNOWN = 61;
    
    public static int elementName(final char[] array, final int n, final int n2) {
        return TinyString.getIndex(SVG.ELEMENTS, array, n, n2);
    }
    
    public static int attributeName(final char[] array, final int n, final int n2) {
        return TinyString.getIndex(SVG.ATTRIBUTES, array, n, n2);
    }
    
    public static int attributeValue(final char[] array, final int n, final int n2) {
        return TinyString.getIndex(SVG.VALUES, array, n, n2);
    }
    
    public static boolean isElementAnimatable(final int n) {
        switch (n) {
            case 0:
            case 5:
            case 6:
            case 8:
            case 14:
            case 17:
            case 18:
            case 19:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 29:
            case 30:
            case 31:
            case 32:
            case 34: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public static int attributeDataType(final int n, final int n2) {
        int n3 = 0;
        switch (n2) {
            case 0:
            case 3:
            case 5:
            case 9:
            case 14:
            case 18:
            case 19:
            case 21:
            case 26:
            case 29:
            case 40:
            case 41:
            case 42:
            case 43:
            case 45:
            case 46:
            case 51:
            case 55:
            case 56:
            case 58:
            case 59:
            case 60:
            case 62:
            case 65:
            case 66:
            case 71:
            case 72:
            case 73:
            case 79:
            case 87:
            case 88:
            case 89:
            case 98:
            case 99:
            case 100:
            case 102:
            case 107:
            case 109:
            case 110:
            case 111:
            case 112:
            case 123:
            case 124:
            case 125: {
                n3 = 4;
                break;
            }
            case 1:
            case 2:
            case 13:
            case 16:
            case 22:
            case 27:
            case 30:
            case 31:
            case 32:
            case 33:
            case 39:
            case 64:
            case 70:
            case 74:
            case 75:
            case 76:
            case 77:
            case 80:
            case 81:
            case 84:
            case 85:
            case 86:
            case 92:
            case 95:
            case 106:
            case 122:
            case 126: {
                n3 = 3;
                break;
            }
            case 4:
            case 6:
            case 7:
            case 8:
            case 10:
            case 12:
            case 17:
            case 28:
            case 34:
            case 35:
            case 36:
            case 37:
            case 44:
            case 50:
            case 54:
            case 57:
            case 68:
            case 69:
            case 90:
            case 91:
            case 93:
            case 96:
            case 97:
            case 101:
            case 104:
            case 108:
            case 113:
            case 114:
            case 115:
            case 116:
            case 117:
            case 118:
            case 119:
            case 120:
            case 121: {
                n3 = 9;
                break;
            }
            case 11:
            case 23:
            case 24:
            case 52:
            case 53:
            case 67: {
                n3 = 12;
                break;
            }
            case 15:
            case 78:
            case 82: {
                n3 = 1;
                break;
            }
            case 20:
            case 61: {
                n3 = 6;
                break;
            }
            case 25: {
                switch (n) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 28: {
                        n3 = 3;
                        break;
                    }
                    default: {
                        n3 = 1;
                        break;
                    }
                }
                break;
            }
            case 47:
            case 49: {
                n3 = 5;
                break;
            }
            case 48: {
                n3 = 7;
                break;
            }
            case 63: {
                n3 = 8;
                break;
            }
            case 83: {
                n3 = 2;
                break;
            }
            case 38:
            case 94: {
                n3 = 11;
                break;
            }
            case 103: {
                n3 = 10;
                break;
            }
            case 105: {
                n3 = 14;
                break;
            }
        }
        return n3;
    }
    
    public static Object copyAttributeValue(final Object o, final int n) {
        Object o2 = null;
        if (o == null) {
            return o2;
        }
        switch (n) {
            case 1: {
                final TinyColor tinyColor = (TinyColor)o;
                if (tinyColor != null) {
                    o2 = new TinyColor(tinyColor);
                }
                break;
            }
            case 2: {
                final int[] array = (int[])o;
                if (array != null) {
                    final int[] array2 = new int[array.length];
                    System.arraycopy(array, 0, array2, 0, array2.length);
                    o2 = array2;
                }
                break;
            }
            case 3:
            case 4: {
                final TinyNumber tinyNumber = (TinyNumber)o;
                if (tinyNumber != null) {
                    o2 = new TinyNumber(tinyNumber.val);
                }
                break;
            }
            case 6: {
                final TinyPath tinyPath = (TinyPath)o;
                if (tinyPath != null) {
                    o2 = new TinyPath(tinyPath);
                }
                break;
            }
            case 9: {
                final TinyString tinyString = (TinyString)o;
                if (tinyString != null) {
                    o2 = new TinyString(tinyString.data);
                }
                break;
            }
            case 11: {
                final TinyTransform tinyTransform = (TinyTransform)o;
                if (tinyTransform != null) {
                    o2 = new TinyTransform(tinyTransform);
                }
                break;
            }
            case 12: {
                final SMILTime smilTime = (SMILTime)o;
                if (smilTime != null) {
                    o2 = new SMILTime();
                    smilTime.copyTo((SMILTime)o2);
                }
                break;
            }
            case 14: {
                final SVGRect svgRect = (SVGRect)o;
                if (svgRect != null) {
                    o2 = new SVGRect(svgRect);
                }
                break;
            }
            case 5:
            case 7:
            case 8:
            case 10: {
                final TinyVector tinyVector = (TinyVector)o;
                if (tinyVector != null) {
                    final TinyVector tinyVector2 = new TinyVector(4);
                    for (int count = tinyVector.count, i = 0; i < count; ++i) {
                        switch (n) {
                            case 5: {
                                final TinyNumber tinyNumber2 = (TinyNumber)tinyVector.data[i];
                                if (tinyNumber2 != null) {
                                    tinyVector2.addElement(new TinyNumber(tinyNumber2.val));
                                }
                                break;
                            }
                            case 7: {
                                final TinyPath tinyPath2 = (TinyPath)tinyVector.data[i];
                                if (tinyPath2 != null) {
                                    tinyVector2.addElement(new TinyPath(tinyPath2));
                                }
                                break;
                            }
                            case 8: {
                                final TinyPoint tinyPoint = (TinyPoint)tinyVector.data[i];
                                if (tinyPoint != null) {
                                    tinyVector2.addElement(new TinyPoint(tinyPoint.x, tinyPoint.y));
                                }
                                break;
                            }
                            case 10: {
                                final TinyString tinyString2 = (TinyString)tinyVector.data[i];
                                if (tinyString2 != null) {
                                    tinyVector2.addElement(new TinyString(tinyString2.data));
                                    break;
                                }
                                break;
                            }
                        }
                    }
                    o2 = tinyVector2;
                    break;
                }
                break;
            }
        }
        return o2;
    }
    
    static {
        SVG.ELEMENTS = new char[][] { "a".toCharArray(), "animate".toCharArray(), "animateColor".toCharArray(), "animateMotion".toCharArray(), "animateTransform".toCharArray(), "circle".toCharArray(), "defs".toCharArray(), "desc".toCharArray(), "ellipse".toCharArray(), "font".toCharArray(), "font-face".toCharArray(), "font-face-name".toCharArray(), "font-face-src".toCharArray(), "foreignObject".toCharArray(), "g".toCharArray(), "glyph".toCharArray(), "hkern".toCharArray(), "image".toCharArray(), "line".toCharArray(), "linearGradient".toCharArray(), "metadata".toCharArray(), "missing-glyph".toCharArray(), "mpath".toCharArray(), "path".toCharArray(), "polygon".toCharArray(), "polyline".toCharArray(), "radialGradient".toCharArray(), "rect".toCharArray(), "set".toCharArray(), "stop".toCharArray(), "svg".toCharArray(), "switch".toCharArray(), "text".toCharArray(), "title".toCharArray(), "use".toCharArray() };
        SVG.ATTRIBUTES = new char[][] { "accent-height".toCharArray(), "accumulate".toCharArray(), "additive".toCharArray(), "alphabetic".toCharArray(), "arabic-form".toCharArray(), "ascent".toCharArray(), "attributeName".toCharArray(), "attributeType".toCharArray(), "baseProfile".toCharArray(), "baseline".toCharArray(), "bbox".toCharArray(), "begin".toCharArray(), "by".toCharArray(), "calcMode".toCharArray(), "cap-height".toCharArray(), "color".toCharArray(), "color-rendering".toCharArray(), "content".toCharArray(), "cx".toCharArray(), "cy".toCharArray(), "d".toCharArray(), "descent".toCharArray(), "display".toCharArray(), "dur".toCharArray(), "end".toCharArray(), "fill".toCharArray(), "fill-opacity".toCharArray(), "fill-rule".toCharArray(), "font-family".toCharArray(), "font-size".toCharArray(), "font-stretch".toCharArray(), "font-style".toCharArray(), "font-variant".toCharArray(), "font-weight".toCharArray(), "from".toCharArray(), "g1".toCharArray(), "g2".toCharArray(), "glyph-name".toCharArray(), "gradientTransform".toCharArray(), "gradientUnits".toCharArray(), "hanging".toCharArray(), "height".toCharArray(), "horiz-adv-x".toCharArray(), "horiz-origin-x".toCharArray(), "id".toCharArray(), "ideographic".toCharArray(), "k".toCharArray(), "keyPoints".toCharArray(), "keySplines".toCharArray(), "keyTimes".toCharArray(), "lang".toCharArray(), "mathematical".toCharArray(), "max".toCharArray(), "min".toCharArray(), "name".toCharArray(), "offset".toCharArray(), "opacity".toCharArray(), "origin".toCharArray(), "overline-position".toCharArray(), "overline-thickness".toCharArray(), "panose-1".toCharArray(), "path".toCharArray(), "pathLength".toCharArray(), "points".toCharArray(), "preserveAspectRatio".toCharArray(), "r".toCharArray(), "repeatCount".toCharArray(), "repeatDur".toCharArray(), "requiredExtensions".toCharArray(), "requiredFeatures".toCharArray(), "restart".toCharArray(), "rotate".toCharArray(), "rx".toCharArray(), "ry".toCharArray(), "slope".toCharArray(), "spreadMethod".toCharArray(), "stemh".toCharArray(), "stemv".toCharArray(), "stop-color".toCharArray(), "stop-opacity".toCharArray(), "strikethrough-position".toCharArray(), "strikethrough-thickness".toCharArray(), "stroke".toCharArray(), "stroke-dasharray".toCharArray(), "stroke-dashoffset".toCharArray(), "stroke-linecap".toCharArray(), "stroke-linejoin".toCharArray(), "stroke-miterlimit".toCharArray(), "stroke-opacity".toCharArray(), "stroke-width".toCharArray(), "systemLanguage".toCharArray(), "target".toCharArray(), "text-anchor".toCharArray(), "to".toCharArray(), "transform".toCharArray(), "type".toCharArray(), "u1".toCharArray(), "u2".toCharArray(), "underline-position".toCharArray(), "underline-thickness".toCharArray(), "unicode".toCharArray(), "unicode-range".toCharArray(), "units-per-em".toCharArray(), "values".toCharArray(), "version".toCharArray(), "viewBox".toCharArray(), "visibility".toCharArray(), "width".toCharArray(), "widths".toCharArray(), "x".toCharArray(), "x-height".toCharArray(), "x1".toCharArray(), "x2".toCharArray(), "xlink:actuate".toCharArray(), "xlink:arcrole".toCharArray(), "xlink:href".toCharArray(), "xlink:role".toCharArray(), "xlink:show".toCharArray(), "xlink:title".toCharArray(), "xlink:type".toCharArray(), "xml:base".toCharArray(), "xml:lang".toCharArray(), "xml:space".toCharArray(), "y".toCharArray(), "y1".toCharArray(), "y2".toCharArray(), "zoomAndPan".toCharArray() };
        SVG.VALUES = new char[][] { "100".toCharArray(), "200".toCharArray(), "300".toCharArray(), "400".toCharArray(), "500".toCharArray(), "600".toCharArray(), "700".toCharArray(), "800".toCharArray(), "900".toCharArray(), "always".toCharArray(), "auto".toCharArray(), "auto-reverse".toCharArray(), "bevel".toCharArray(), "bold".toCharArray(), "bolder".toCharArray(), "butt".toCharArray(), "collapse".toCharArray(), "currentColor".toCharArray(), "default".toCharArray(), "disable".toCharArray(), "discrete".toCharArray(), "end".toCharArray(), "evenodd".toCharArray(), "freeze".toCharArray(), "hidden".toCharArray(), "indefinite".toCharArray(), "inherit".toCharArray(), "inline".toCharArray(), "italic".toCharArray(), "lighter".toCharArray(), "linear".toCharArray(), "magnify".toCharArray(), "middle".toCharArray(), "miter".toCharArray(), "never".toCharArray(), "none".toCharArray(), "nonzero".toCharArray(), "normal".toCharArray(), "objectBoundingBox".toCharArray(), "oblique".toCharArray(), "paced".toCharArray(), "pad".toCharArray(), "preserve".toCharArray(), "reflect".toCharArray(), "remove".toCharArray(), "repeat".toCharArray(), "replace".toCharArray(), "rotate".toCharArray(), "round".toCharArray(), "scale".toCharArray(), "skewX".toCharArray(), "skewY".toCharArray(), "spline".toCharArray(), "square".toCharArray(), "start".toCharArray(), "sum".toCharArray(), "translate".toCharArray(), "userSpaceOnUse".toCharArray(), "visible".toCharArray(), "whenNotActive".toCharArray(), "xMidYMid meet".toCharArray() };
        VAL_STROKEDASHARRAYNONE = new int[0];
        VAL_STROKEDASHARRAYINHERIT = new int[0];
        VAL_DEFAULT_FONTFAMILY = new TinyString("Helvetica".toCharArray());
    }
}
