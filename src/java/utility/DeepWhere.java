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
public class DeepWhere {

    private String field = "";
    private boolean matches = false;
    private String arg = "";

    private final String SPACE = " ";
    private final String SPEC_CHAR = "\"";

    ArrayList<String> wpart = new ArrayList<>();

    public DeepWhere(String field, String arg) {
        setField(field);
        setArg(arg);
    }

    public boolean isMatched() {
        if (this.arg.trim().length() == 0) {
            return true;
        }
        seperateWpart();
        if (getMainOrStatement()) {
            return matches;
        } else {
            return getAllAndStatement();
        }
    }

    private boolean getMainOrStatement() {
        String splitPart[] = this.arg.split(SPACE);
        for (String p : splitPart) {
            if (p.trim().length() == 0) {
                continue;
            }
            if (getGT(p)) {
                break;
            }
            if (getGE(p)) {
                break;
            }

            if (getLT(p)) {
                break;
            }

            if (getLE(p)) {
                break;
            }
            if (getBetween(p)) {
                break;
            }
            if (setAsLike(p)) {
                break;
            }
        }
        return matches;
    }

    private boolean getAllAndStatement() {
        for (String st : wpart) {
            if (st.trim().length() == 0) {
                continue;
            }
            boolean r = getSingleAndStatement(st);
            if (r) {
                matches = true;
                return matches;
            }
        }
        matches = false;
        return matches;
    }

    private boolean getSingleAndStatement(String val) {
        String splitPart[] = val.split(SPACE);
        this.matches = true;
        for (String p : splitPart) {
            if (p.trim().length() == 0) {
                continue;
            }
            if (!getGT(p)) {
                this.matches = false;
            } else if (!getGE(p)) {
                this.matches = false;
            } else if (!getLT(p)) {
                this.matches = false;
            } else if (!getLE(p)) {
                this.matches = false;
            } else if (!getBetween(p)) {
                this.matches = false;
            } else if (!setAsLike(p)) {
                this.matches = false;
            }
        }
        return matches;
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

    private boolean getGT(String val) {
        // >5 or 2000>
        if (val.startsWith(">")) {
            String pval = val.substring(1, val.length());
            if (isNumber(pval) && isNumber(getField())) {
                matches = Double.parseDouble(getField()) > Double.parseDouble(pval);
            }
        } else if (val.endsWith(">")) {
            String pval = val.substring(0, val.length() - 1);
            if (isNumber(pval) && isNumber(getField())) {
                matches = Double.parseDouble(getField()) < Double.parseDouble(pval);
            }
        }
        return matches;
    }

    private boolean getGE(String val) {
        // >=5 or =>5 or  2000>= or 2000=>
        if (val.startsWith(">=") || val.startsWith("=>")) {
            String pval = val.substring(2, val.length());
            if (isNumber(pval) && isNumber(getField())) {
                matches = Double.parseDouble(getField()) >= Double.parseDouble(pval);
            }

        } else if (val.endsWith(">=") || val.endsWith("=>")) {
            String pval = val.substring(0, val.length() - 2);
            if (isNumber(pval) && isNumber(getField())) {
                matches = Double.parseDouble(getField()) <= Double.parseDouble(pval);
            }
        }
        return matches;
    }

    private boolean getLT(String val) {
        // <5 or 2000<
        if (val.startsWith("<")) {
            String pval = val.substring(1, val.length());
            if (isNumber(pval) && isNumber(getField())) {
                matches = Double.parseDouble(getField()) < Double.parseDouble(pval);
            }

        } else if (val.endsWith("<")) {
            String pval = val.substring(0, val.length() - 1);
            if (isNumber(pval) && isNumber(getField())) {
                matches = Double.parseDouble(getField()) > Double.parseDouble(pval);
            }

        }
        return matches;
    }

    private boolean getLE(String val) {
        // <=5 or =<5 or 2000=< or 2000<=
        if (val.startsWith("=<") || val.startsWith("<=")) {
            String pval = val.substring(2, val.length());
            if (isNumber(pval) && isNumber(getField())) {
                matches = Double.parseDouble(getField()) <= Double.parseDouble(pval);
            }
        } else if (val.endsWith("=<") || val.endsWith("<=")) {
            String pval = val.substring(0, val.length() - 2);
            if (isNumber(pval) && isNumber(getField())) {
                matches = Double.parseDouble(getField()) >= Double.parseDouble(pval);
            }
        }
        return matches;
    }

    private boolean getBetween(String val) {
        String[] pval = val.trim().split("-");
        if (pval.length == 2 && pval[0].trim().length() > 0 && pval[1].trim().length() > 0
                && isNumber(pval[0]) && isNumber(pval[1]) && isNumber(getField())) {
            matches = Double.parseDouble(getField()) >= Double.parseDouble(pval[0])
                    && Double.parseDouble(getField()) <= Double.parseDouble(pval[1]);
        }
        return matches;
    }

    private boolean setAsLike(String val) {
        if (val.trim().length() > 0) {
            matches = getField().toLowerCase().contains(val.toLowerCase());
        }
        return matches;
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
            wpart.add(newLn.replaceAll("\"", ""));
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
