/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.util.ArrayList;

/**
 *
 * @author Lenovo
 */
public class WhereSingle {

    private String field = "";
    private String arg = "";
    private boolean withParanteth = true;
    private ArrayList valueArr;

    private final String COMMA = ",";
    private final String QST = " ? ";
    private final String LEFT_PARANTETH = "(";
    private final String RIGHT_PRANTETH = ")";
    private final String AND = " AND ";
    private final String OR = " OR ";
    private final String SPACE = " ";
    private final String SPEC_CHAR = "\"";

    ArrayList<String> wpart = new ArrayList<>();

    public WhereSingle(String field, String arg, ArrayList valueArr) {
        setField(field);
        setArg(arg);
        this.valueArr = valueArr;
    }

    public String exec() {      
        seperateWpart();
        String mainPart = getMainOrStatement();
        String andPart = getAllAndStatement();
        String ln = "";//withParanteth ? LEFT_PARANTETH : "";
        ln += mainPart;
        ln += andPart.trim().length() > 3 && ln.length() > 3 ? OR : "";
        ln += andPart.trim().length() > 3 ? andPart : "";
        if (ln.length()>0){
            if (withParanteth){
                ln = LEFT_PARANTETH+ln+RIGHT_PRANTETH ;
            }
        }
        return ln;
    }

    private String getMainOrStatement() {
        String splitPart[] = this.arg.split(SPACE);

        String ln = "";
        for (String p : splitPart) {
            if (p.trim().length() == 0) {
                continue;
            }
            String res;
            res = getGT(p);
            if (res.length() > 0) {
                ln += SPACE + res + OR;
                continue;
            }
            res = getGE(p);
            if (res.length() > 0) {
                ln += SPACE + res + OR;
                continue;
            }
            res = getLT(p);
            if (res.length() > 0) {
                ln += SPACE + res + OR;
                continue;
            }
            res = getLE(p);
            if (res.length() > 0) {
                ln += SPACE + res + OR;
                continue;
            }
            res = getBetween(p);
            if (res.length() > 0) {
                ln += SPACE + res + OR;
                continue;
            }
            //ELSE
            ln += SPACE + setAsLike(p) + OR;
        }
        ln = ln.length() > 3 ? ln.substring(0, ln.length() - OR.length()) : ln;
        ln = ln.length() > 3 ? LEFT_PARANTETH + ln + RIGHT_PRANTETH : ln;
        return ln;
    }

    private String getAllAndStatement() {
        String ln = "";
        for (String st : wpart) {
            if (st.trim().length() == 0) {
                continue;
            }
            String r = getSingleAndStatement(st);
            ln += r + OR;
        }
        ln = ln.length() > 3 ? ln.substring(0, ln.length() - OR.length()) : ln;
        return ln;
    }

    private String getSingleAndStatement(String val) {
        String splitPart[] = val.split(SPACE);

        String ln = "";
        for (String p : splitPart) {
            if (p.trim().length() == 0) {
                continue;
            }
            String res;
            res = getGT(p);
            if (res.length() > 0) {
                ln += SPACE + res + AND;
                continue;
            }
            res = getGE(p);
            if (res.length() > 0) {
                ln += SPACE + res + AND;
                continue;
            }
            res = getLT(p);
            if (res.length() > 0) {
                ln += SPACE + res + AND;
                continue;
            }
            res = getLE(p);
            if (res.length() > 0) {
                ln += SPACE + res + AND;
                continue;
            }
            res = getBetween(p);
            if (res.length() > 0) {
                ln += SPACE + res + AND;
                continue;
            }
            //ELSE
            ln += SPACE + setAsLike(p) + AND;
        }
        ln = ln.length() > 3 ? ln.substring(0, ln.length() - AND.length()) : ln;
        ln = ln.length() > 3 ? LEFT_PARANTETH + ln + RIGHT_PRANTETH : ln;
        return ln;
    }

    public WhereSingle setWithParenteth(boolean arg) {
        this.withParanteth = arg;
        return this;
    }

    private String getField() {
        return this.field;
    }

    private void setField(String field) {
        this.field = field;
    }

    private void setArg(String arg) {
        this.arg = arg;
    }

    private String getArg() {
        return this.arg;
    }

    private String[] splitArg() {
        return getArg().split(" ");
    }

    private String getGT(String val) {
        // >5 or 2000>
        String res = "";
        if (val.startsWith(">")) {
            String pval = val.substring(1, val.length());
            if (isNumber(pval)) {
                res = getField() + ">" + QST;
                this.valueArr.add(pval);
            }
        } else if (val.endsWith(">")) {
            String pval = val.substring(0, val.length() - 1);
            if (isNumber(pval)) {
                res = getField() + "<" + QST;
                this.valueArr.add(pval);
            }
        }
        return res;
    }

    private String getGE(String val) {
        // >=5 or =>5 or  2000>= or 2000=>
        String res = "";
        if (val.startsWith(">=") || val.startsWith("=>")) {
            String pval = val.substring(2, val.length());
            if (isNumber(pval)) {
                res = getField() + ">=" + QST;
                this.valueArr.add(pval);
            }
        } else if (val.endsWith(">=") || val.endsWith("=>")) {
            String pval = val.substring(0, val.length() - 2);
            if (isNumber(pval)) {
                res = getField() + "<=" + QST;
                this.valueArr.add(pval);
            }
        }
        return res;
    }

    private String getLT(String val) {
        // <5 or 2000<
        String res = "";
        if (val.startsWith("<")) {
            String pval = val.substring(1, val.length());
            if (isNumber(pval)) {
                res = getField() + "<" + QST;
                this.valueArr.add(pval);
            }
        } else if (val.endsWith("<")) {
            String pval = val.substring(0, val.length() - 1);
            if (isNumber(pval)) {
                res = getField() + ">" + QST;
                this.valueArr.add(pval);
            }
        }
        return res;
    }

    private String getLE(String val) {
        // <=5 or =<5 or 2000=< or 2000<=
        String res = "";
        if (val.startsWith("=<") || val.startsWith("<=")) {
            String pval = val.substring(2, val.length());
            if (isNumber(pval)) {
                res = getField() + "<=" + QST;
                this.valueArr.add(pval);
            }
        } else if (val.endsWith("=<") || val.endsWith("<=")) {
            String pval = val.substring(0, val.length() - 2);
            if (isNumber(pval)) {
                res = getField() + ">=" + QST;
                this.valueArr.add(pval);
            }
        }
        return res;
    }

    private String getBetween(String val) {
        String res = "";
        String[] pval = val.trim().split("-");
        if (pval.length == 2 && pval[0].trim().length() > 0 && pval[1].trim().length() > 0
                && isNumber(pval[0]) && isNumber(pval[1])) {
            res = getField() + " BETWEEN " + QST + " AND " + QST;
            this.valueArr.add(pval[0]);
            this.valueArr.add(pval[1]);
        }
        return res;
    }

    private String setAsLike(String val) {
        String res = "";
        if (val.trim().length() > 0) {
            res = getField() + " LIKE " + SPACE + QST;
            this.valueArr.add("%" + val + "%");
        }
        return res;
    }

    private void seperateWpart() {
        ArrayList<Integer> idx = new ArrayList<>();
        int ind = -1;
        do {
            ind = this.arg.indexOf(SPEC_CHAR, ind + 1);
            if (ind > -1) {
                idx.add(ind);
            }
        } while (ind > -1);

        int s = idx.size() > 0 && idx.size() % 2 == 0
                ? idx.size() : idx.size() - 1;
        for (int i = 0; i < s; i += 2) {
            int sind = idx.get(i);
            int eind = idx.get(i + 1);
            String newLn = this.arg.substring(sind + 1, eind);
            wpart.add(newLn.replaceAll("\"",""));
        }

        //replace wpart
        wpart.stream().forEach((wpart1) -> {
            this.arg = this.arg.replaceFirst(SPEC_CHAR + wpart1 + SPEC_CHAR, "");
        });
    }

    private boolean isNumber(String arg) {
        boolean f = true;
        try {
            double a = Double.parseDouble(arg);
        } catch (Exception e) {
            f = false;
        }
        return f;
    }

}
