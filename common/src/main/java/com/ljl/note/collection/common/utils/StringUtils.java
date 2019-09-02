package com.ljl.note.collection.common.utils;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class StringUtils {
    private static final String URL_REGEX = "((http|ftp|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?";
    private static final String HTTP_URL_REGEX = "((http|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?";
    private static final String NUMBER_DECIMAL_REGEX_NEED_FORMAT = "^(([1-9]\\d*)|(0))(\\.\\d{1,%s})?$";
    public static final String DECIMAL_FORMAT_PATTERN_TWO = "##.##";
    public static final String SEPARATOR = ",";

    public StringUtils() {
    }

    public static String collectionToStrings(Collection<? extends Object> collection) {
        StringBuilder builder = new StringBuilder();
        if(collection != null && collection.size() > 0) {
            Iterator ite = collection.iterator();

            while(ite.hasNext()) {
                builder.append(ite.next());
                builder.append(",");
            }

            builder.deleteCharAt(builder.length() - 1);
        }

        return builder.toString();
    }

    public static String makeStrCollection(List idList) {
        if(idList != null && idList.size() != 0) {
            StringBuilder builder = new StringBuilder();

            int i;
            for(i = 0; i < idList.size() - 1; ++i) {
                builder.append(idList.get(i)).append(",");
            }

            builder.append(idList.get(i));
            return builder.toString();
        } else {
            return "";
        }
    }

    public static String formateOrCondition(int num, String unit) {
        if(num <= 0) {
            return "";
        } else {
            StringBuilder builder = new StringBuilder(num * (unit.length() + 2));
            builder.append(unit);

            for(int i = 1; i < num; ++i) {
                builder.append("or");
                builder.append(unit);
            }

            return builder.toString();
        }
    }

    /*public static long transToLong(String input, String delim) {
        long result = 0L;
        if(input != null && input.length() != 0) {
            String[] delimal = input.split(delim);
            if(delimal.length > 63) {
                return result;
            } else {
                int charInt = false;

                for(int i = 0; i < delimal.length; ++i) {
                    int charInt;
                    try {
                        charInt = Integer.parseInt(delimal[i]);
                    } catch (Exception var9) {
                        charInt = 0;
                    }

                    if(charInt > 0) {
                        result += (long)(1 << i);
                    }
                }

                return result;
            }
        } else {
            return result;
        }
    }*/

    public static String getPercent(double num, int fraction) {
        NumberFormat fmt = NumberFormat.getPercentInstance();
        fmt.setMaximumFractionDigits(fraction);
        return fmt.format(num);
    }

    public static String convNumber(double number, String formate) {
        DecimalFormat df = new DecimalFormat(formate);
        return df.format(number);
    }

    public static String convNumber(double number) {
        DecimalFormat df = new DecimalFormat("##.##");
        return df.format(number);
    }

    public static boolean isTraditionalChineseCharacter(char c, boolean checkGbk) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
        if(!Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS.equals(block) && !Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS.equals(block) && !Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A.equals(block)) {
            return false;
        } else if(checkGbk) {
            try {
                String s = "" + c;
                return s.equals(new String(s.getBytes("GBK"), "GBK"));
            } catch (UnsupportedEncodingException var4) {
                return false;
            }
        } else {
            return true;
        }
    }

    public static boolean isGBK(String input) {
        try {
            return input.equals(new String(input.getBytes("GBK"), "GBK"));
        } catch (UnsupportedEncodingException var2) {
            return false;
        }
    }

    public static boolean validBeidouGbkStr(String input, boolean checkGbk, int minLength, int maxLength) {
        if(minLength > -1 && input.length() < minLength) {
            return false;
        } else if(maxLength > -1 && input.length() > maxLength) {
            return false;
        } else {
            char[] ch = input.toCharArray();
            int length = 0;

            for(int i = 0; i < ch.length; ++i) {
                char c = ch[i];
                if(isNeedAlph(c)) {
                    ++length;
                } else {
                    if(!isTraditionalChineseCharacter(c, checkGbk)) {
                        return false;
                    }

                    length += 2;
                }
            }

            if(minLength > -1 && length < minLength) {
                return false;
            } else if(maxLength > -1 && length > maxLength) {
                return false;
            } else {
                return true;
            }
        }
    }

    private static boolean isNeedAlph(char c) {
        return c >= 97 && c <= 122?true:(c >= 65 && c <= 90?true:(c >= 48 && c <= 57?true:(c == 45?true:c == 95)));
    }

    public static boolean isLatinCharacter(char c) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
        return Character.UnicodeBlock.BASIC_LATIN.equals(block);
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String i = "\"type\":\"text\",\"message\":\"ߑ\u008c\"";
        System.out.println(isGBK(i));
        String s = encodeISO(i);
        System.out.println(i + "\n " + s);
        System.out.println(parseInteger("1221000085"));
        System.out.println(parseInteger("1221000086"));
        System.out.println(parseInteger("1221000087"));
        System.out.println(parseInteger("1221000088"));
    }

    public static String tokenizeListToStringNo(List input, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {
        StringBuffer result = new StringBuffer();
        Iterator inputIt = input.iterator();

        while(true) {
            String token;
            do {
                if(!inputIt.hasNext()) {
                    result.replace(result.length() - 1, result.length(), "");
                    return result.toString();
                }

                token = inputIt.next().toString();
                if(trimTokens) {
                    token = token.trim();
                }
            } while(ignoreEmptyTokens && token.length() == 0);

            result.append(token);
            result.append(delimiters);
        }
    }

    public static String tokenizeStringToString(String[] s, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {
        StringBuffer result = new StringBuffer();
        if(delimiters == null) {
            delimiters = ",";
        }

        if(s != null && s.length > 0) {
            for(int i = 0; i < s.length; ++i) {
                result.append(s[i]);
                result.append(delimiters);
            }

            result.replace(result.length() - delimiters.length(), result.length(), "");
        }

        return result.toString();
    }

    public static List tokenizeStringToList(String s, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {
        List tokens = new ArrayList();
        if(s != null && s.length() > 0) {
            StringTokenizer st = new StringTokenizer(s, delimiters);

            while(true) {
                String token;
                do {
                    if(!st.hasMoreTokens()) {
                        return tokens;
                    }

                    token = st.nextToken();
                    if(trimTokens) {
                        token = token.trim();
                    }
                } while(ignoreEmptyTokens && token.length() == 0);

                tokens.add(token);
            }
        } else {
            return tokens;
        }
    }

    public static String getPercent(double num) {
        NumberFormat fmt = NumberFormat.getPercentInstance();
        fmt.setMaximumFractionDigits(2);
        return fmt.format(num);
    }

    public static String getFractionDigits(double num, int fraction) {
        NumberFormat fmt = NumberFormat.getInstance();
        fmt.setMaximumFractionDigits(fraction);
        return fmt.format(num);
    }

    public static String getMoney(double num) {
        NumberFormat fmt = NumberFormat.getCurrencyInstance(Locale.CHINA);
        fmt.setMaximumFractionDigits(2);
        return fmt.format(num);
    }

    public static char getCharFromEnd(String inString, int index) {
        return inString.charAt(inString.length() - index - 1);
    }

    public static String getStringFromEnd(String inString, int length) {
        return inString.substring(inString.length() - length);
    }

    public static String replace(String inString, int index, char newChar) {
        if(inString == null) {
            return null;
        } else {
            int len = inString.length();
            if(index >= 0 && index < len) {
                int pos = len - index - 1;
                StringBuffer sbuf = new StringBuffer();
                sbuf.append(inString.substring(0, pos));
                sbuf.append(newChar);
                sbuf.append(inString.substring(pos + 1));
                return sbuf.toString();
            } else {
                return inString;
            }
        }
    }

    public static String replace(String inString, String oldPattern, String newPattern) {
        if(inString == null) {
            return null;
        } else if(oldPattern != null && newPattern != null) {
            StringBuffer sbuf = new StringBuffer();
            int pos = 0;
            int index = inString.indexOf(oldPattern);

            for(int patLen = oldPattern.length(); index >= 0; index = inString.indexOf(oldPattern, pos)) {
                sbuf.append(inString.substring(pos, index));
                sbuf.append(newPattern);
                pos = index + patLen;
            }

            sbuf.append(inString.substring(pos));
            return sbuf.toString();
        } else {
            return inString;
        }
    }

    public static String getFileSuffix(String filename) {
        if(filename == null) {
            return null;
        } else if(filename.lastIndexOf(".") != -1 && filename.lastIndexOf(46) != filename.length() - 1) {
            StringTokenizer token = new StringTokenizer(filename, ".");

            String filetype;
            for(filetype = ""; token.hasMoreTokens(); filetype = token.nextToken()) {
                ;
            }

            return filetype;
        } else {
            return "";
        }
    }

    public static Boolean judgeSuffix(String suffix) {
        if(suffix == null) {
            return null;
        } else {
            Set<String> suffixSet = new HashSet();
            suffixSet.add("bat");
            suffixSet.add("cmd");
            suffixSet.add("exe");
            return suffixSet.contains(suffix)?Boolean.valueOf(true):Boolean.valueOf(false);
        }
    }

    public static final String toChinese(String strVal) {
        try {
            return strVal == null?null:new String(strVal.getBytes("ISO8859_1"), "GB2312");
        } catch (Exception var2) {
            return null;
        }
    }

    public static final String toUTF8Chinese(String strVal) {
        try {
            return strVal == null?null:new String(strVal.getBytes("ISO8859_1"), "utf-8");
        } catch (Exception var2) {
            return null;
        }
    }

    public static String iptoStr(String ip) throws Exception {
        String ip_tmp = "";
        StringTokenizer tokenizer = new StringTokenizer(ip, ".");
        String token_tmp = "";

        try {
            while(tokenizer.hasMoreTokens()) {
                token_tmp = tokenizer.nextToken();
                if(token_tmp.length() == 3) {
                    ip_tmp = ip_tmp + token_tmp;
                }

                if(token_tmp.length() == 2) {
                    ip_tmp = ip_tmp + "0" + token_tmp;
                }

                if(token_tmp.length() == 1) {
                    ip_tmp = ip_tmp + "00" + token_tmp;
                }
            }
        } catch (Exception var5) {
            System.out.println(var5);
        }

        return ip_tmp;
    }

    public static String getShortLinkTitle(String title, int shortStrLength) {
        String tail = "...";
        return title.length() < shortStrLength + tail.length()?title:title.substring(0, shortStrLength) + tail;
    }

    public static String[] tokenizeToStringArray(String s, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {
        StringTokenizer st = new StringTokenizer(s, delimiters);
        ArrayList tokens = new ArrayList();

        while(true) {
            String token;
            do {
                if(!st.hasMoreTokens()) {
                    return (String[])((String[])tokens.toArray(new String[tokens.size()]));
                }

                token = st.nextToken();
                if(trimTokens) {
                    token = token.trim();
                }
            } while(ignoreEmptyTokens && token.length() == 0);

            tokens.add(token);
        }
    }

    public static Long[] tokenizeToLongArray(String s, String delimiters) {
        if(s == null) {
            return null;
        } else {
            StringTokenizer st = new StringTokenizer(s, delimiters);
            ArrayList tokens = new ArrayList();

            while(st.hasMoreTokens()) {
                String token = st.nextToken();
                token = token.trim();
                if(isNumberStr(token)) {
                    tokens.add(Long.valueOf(token));
                }
            }

            return (Long[])tokens.toArray(new Long[tokens.size()]);
        }
    }

    public static List<Long> tokenizeToLongList(String s, String delimiters) {
        if(s == null) {
            return null;
        } else {
            StringTokenizer st = new StringTokenizer(s, delimiters);
            ArrayList tokens = new ArrayList();

            while(st.hasMoreTokens()) {
                String token = st.nextToken();
                token = token.trim();
                if(isNumberStr(token)) {
                    tokens.add(Long.valueOf(token));
                }
            }

            return tokens;
        }
    }

    public static String tokenizeListToString(List input, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {
        StringBuffer result = new StringBuffer();
        result.append("(");
        Iterator inputIt = input.iterator();

        while(true) {
            String token;
            do {
                Object o;
                do {
                    if(!inputIt.hasNext()) {
                        result.replace(result.length() - 1, result.length(), ")");
                        return result.toString();
                    }

                    o = inputIt.next();
                } while(o == null);

                token = o.toString();
                if(trimTokens) {
                    token = token.trim();
                }
            } while(ignoreEmptyTokens && token.length() == 0);

            result.append(token);
            result.append(delimiters);
        }
    }

    public static String tokenizeListToStringWithNoBracket(List input, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {
        StringBuffer result = new StringBuffer();
        result.append("");
        Iterator inputIt = input.iterator();

        while(true) {
            String token;
            do {
                if(!inputIt.hasNext()) {
                    result.replace(result.length() - 1, result.length(), "");
                    return result.toString();
                }

                token = inputIt.next().toString();
                if(trimTokens) {
                    token = token.trim();
                }
            } while(ignoreEmptyTokens && token.length() == 0);

            result.append(token);
            result.append(delimiters);
        }
    }

    public static String checkString(String inputString) {
        if(inputString == null) {
            return null;
        } else {
            String model = "[\\x01-\\x1f]";
            Pattern p = Pattern.compile(model);
            Matcher m = p.matcher(inputString);
            StringBuffer sb = new StringBuffer();
            boolean result = m.find();

            for(boolean deletedIllegalChars = false; result; result = m.find()) {
                deletedIllegalChars = true;
                m.appendReplacement(sb, "");
                System.out.println("修改类似: " + sb.toString());
            }

            m.appendTail(sb);
            return sb.toString();
        }
    }

    public static boolean isNumber(String inputString) {
        try {
            new Double(inputString);
            return true;
        } catch (Exception var2) {
            return false;
        }
    }

    public static Long parseLong(String inputString) {
        return isNumber(inputString)?Long.valueOf((new BigDecimal(inputString)).longValue()):null;
    }

    public static Long toLong(String inputString) {
        return isNumber(inputString)?Long.valueOf((new BigDecimal(inputString)).longValue()):null;
    }

    public static String longToString(Long input) {
        String base = "0000000000";
        if(input == null) {
            return base;
        } else {
            String in = input.toString();
            in = base + in;
            int len = in.length();
            return in.substring(len - 10, len);
        }
    }

    public static Integer parseInteger(String inputString) {
        return isNumber(inputString)?Integer.valueOf(Double.valueOf(inputString).intValue()):null;
    }

    public static Integer parseInteger(Number inputNum) {
        return inputNum != null?Integer.valueOf(inputNum.intValue()):null;
    }

    public static Integer toInteger(String inputString) {
        return isNumber(inputString)?Integer.valueOf(Double.valueOf(inputString).intValue()):null;
    }

    public static Integer parseInt(String inputString) {
        return parseInteger(inputString);
    }

    public static boolean isAllEqual(List objectList) {
        Iterator it = objectList.iterator();
        Object temp = it.next();

        Object ob;
        do {
            if(!it.hasNext()) {
                return true;
            }

            ob = it.next();
        } while(temp.equals(ob));

        return false;
    }

    public static long getMaxLong(List objectList) {
        Iterator it = objectList.iterator();
        long temp = ((Long)it.next()).longValue();

        while(it.hasNext()) {
            long ob = ((Long)it.next()).longValue();
            if(ob > temp) {
                temp = ob;
            }
        }

        return temp;
    }

    public static String encodeGBK(String input) throws UnsupportedEncodingException {
        String output = input;
        if(!System.getProperty("file.encoding").equals("GBK")) {
            output = new String(input.getBytes("ISO8859_1"), "GBK");
        }

        return output;
    }

    public static String encodeISO(String input) throws UnsupportedEncodingException {
        String output = input;
        if(!System.getProperty("file.encoding").equals("GBK")) {
            output = new String(input.getBytes("GBK"), "ISO8859_1");
        }

        return output;
    }

    public static boolean isLiteralAndNum(String str) {
        if(str == null) {
            return false;
        } else {
            String model = "[a-zA-Z0-9]*";
            Pattern p = Pattern.compile(model);
            Matcher m = p.matcher(str);
            return m.matches();
        }
    }

    public static String decodeSqlMatching(String matching) {
        String retStr = null;
        retStr = replace(matching, "%", "\\%");
        retStr = replace(retStr, "_", "\\_");
        return retStr;
    }

    public static List formateUrl(String url) {
        List resultList = new ArrayList();
        String flageHead1 = "http://";
        String flageHead2 = "www.";
        String centUrl = "";
        String urlWithHead1 = "";
        String urlWithHead2 = "";
        String urlWithHead12 = "";
        if(url.indexOf(flageHead1) == 0) {
            if(url.indexOf(flageHead1 + flageHead2) == 0) {
                centUrl = url.substring(flageHead1.length() + flageHead2.length());
            } else {
                centUrl = url.substring(flageHead1.length());
            }
        } else if(url.indexOf(flageHead2) == 0) {
            centUrl = url.substring(flageHead2.length());
        } else {
            centUrl = url;
        }

        urlWithHead1 = flageHead1 + centUrl;
        urlWithHead2 = flageHead2 + centUrl;
        urlWithHead12 = flageHead1 + flageHead2 + centUrl;
        resultList.add(urlWithHead12);
        resultList.add(urlWithHead1);
        resultList.add(urlWithHead2);
        resultList.add(centUrl);
        return resultList;
    }

    public static String generatRandomPassword() {
        String passwd = "";
        Random random = new Random();

        String rand;
        int i;
        for(i = 0; i < 2; ++i) {
            rand = String.valueOf(random.nextInt(9));
            passwd = passwd + rand;
        }

        char letter;
        for(i = 0; i < 2; ++i) {
            letter = (char)(random.nextInt(26) + 65);
            rand = String.valueOf(letter);
            passwd = passwd + rand;
        }

        for(i = 0; i < 2; ++i) {
            letter = (char)(random.nextInt(26) + 97);
            rand = String.valueOf(letter);
            passwd = passwd + rand;
        }

        return passwd;
    }

    public static String filterHTML(String src) {
        return src.replaceAll("<.+?>", "");
    }

   /* public static String filterHTML(String src, int maxLength) {
        if(src == null) {
            return null;
        } else if(maxLength <= 0) {
            return "";
        } else {
            src = Pattern.compile("<[^(<|>)]*>").matcher(src).replaceAll("");
            Matcher matcher = Pattern.compile("&[^(&|;)]+;").matcher(src);
            maxLength = maxLength > src.length()?src.length():maxLength;
            int endingPos = false;
            int groupLengthSum = 0;
            int groupCount = 0;

            int endingPos;
            while(true) {
                int nextComparingPos = matcher.find()?matcher.start():src.length();
                int transfferedLength = nextComparingPos - groupLengthSum + groupCount;
                if(transfferedLength >= maxLength) {
                    endingPos = nextComparingPos - (transfferedLength - maxLength);
                    break;
                }

                if(matcher.hitEnd()) {
                    endingPos = src.length();
                    break;
                }

                groupLengthSum += matcher.group().length();
                ++groupCount;
            }

            return src.substring(0, endingPos);
        }
    }*/

    public static String trimString(String src) {
        return src == null?"":src.trim();
    }

    public static Double parseDouble(String inputString) {
        return isNumber(inputString)?Double.valueOf(inputString):null;
    }

    public static Double toDouble(String inputString) {
        return isNumber(inputString)?Double.valueOf(inputString):null;
    }

    public static Float parseFloat(String inputString) {
        return isNumber(inputString)?Float.valueOf(inputString):null;
    }

    public static Float toFloat(String inputString) {
        return isNumber(inputString)?Float.valueOf(inputString):null;
    }

    public static String changeLinedText(String str) {
        String s1 = "\r\n";
        return str.replaceAll(s1, "<br>");
    }

    public static boolean checkStr(String target) {
        return target != null && !target.trim().equals("");
    }

    public static String createInStr(String str, boolean isChar, String split) {
        return createInOrNotinStr(str, "IN", isChar, split);
    }

    public static String createNotInStr(String str, boolean isChar, String split) {
        return createInOrNotinStr(str, "NOT IN", isChar, split);
    }

    private static String createInOrNotinStr(String str, String sign, boolean isChar, String split) {
        str = str.replaceAll(" ", "");
        str = str.replaceAll("\t", "");
        String s = str;
        if(isChar) {
            s = formatString(str, split);
        }

        StringBuilder sb = new StringBuilder(sign);
        sb.append("(");
        if(",".equals(split)) {
            sb.append(s);
        } else {
            String[] temp = s.split(split);
            s = objArrToStr(temp);
            sb.append(s);
        }

        sb.append(")");
        return sb.toString();
    }

    public static String formatString(String target, String split) {
        if(target != null && target.length() != 0) {
            StringBuffer sb = new StringBuffer();
            String[] temp = target.split(split);

            for(int i = 0; i < temp.length; ++i) {
                sb.append("'");
                sb.append(temp[i]);
                sb.append("'");
                sb.append(",");
            }

            trimEndSeparate(sb, ",");
            return sb.toString();
        } else {
            return null;
        }
    }

    public static String objArrToStr(Object[] arr) {
        return arr != null && arr.length > 0?objArrToStr(arr, ","):null;
    }

    public static String objArrToStr(Object[] arr, String split) {
        if(arr != null && arr.length > 0) {
            if(split == null) {
                split = ",";
            }

            String temp = Arrays.toString(arr);
            temp = temp.replaceAll(", ", split);
            return temp.substring(1, temp.length() - 1);
        } else {
            return null;
        }
    }

    public static void trimEndSeparate(StringBuffer str, String separate) {
        if(str != null && separate != null && str.length() != 0 && separate.length() != 0) {
            if(str.length() >= separate.length()) {
                str.setLength(str.length() - separate.length());
            }

        }
    }

    public static void trimEndSeparate(StringBuilder str, String separate) {
        if(str != null && separate != null && str.length() != 0 && separate.length() != 0) {
            if(str.length() >= separate.length()) {
                str.setLength(str.length() - separate.length());
            }

        }
    }

    public static boolean checkKeyWords(String keywords) {
        return !checkStr(keywords)?false:!keywords.replaceAll(",", "").trim().equals("");
    }

    public static String transferToDBC(String inStr) throws UnsupportedEncodingException {
        StringBuffer result = new StringBuffer();
        String tempStr = "";
        byte[] b = null;
        String codeType = "unicode";
        if(inStr != null && inStr.length() > 0) {
            for(int i = 0; i < inStr.length(); ++i) {
                tempStr = inStr.substring(i, i + 1);
                b = tempStr.getBytes(codeType);
                if(b != null) {
                    if(b[3] == -1) {
                        b[2] = (byte)(b[2] + 32);
                        b[3] = 0;
                        result.append(new String(b, codeType));
                    } else {
                        result.append(tempStr);
                    }
                }
            }
        }

        return result.toString();
    }

   /* public static String formatKeyWords(String keywords) throws UnsupportedEncodingException {
        String keywordstr = transferToDBC(keywords);
        Set keywordSet = new TreeSet(Arrays.asList(keywordstr.split(",")));
        keywordstr = tokenizeStringToString((String[])((String[])keywordSet.toArray(new String[0])), ",", false, false);
        return keywordstr;
    }*/

    public static String getFilename(String filepath) {
        String filename = null;
        if(filepath != null && !filepath.trim().equals("")) {
            if(filepath.lastIndexOf(File.separator) != -1) {
                filename = filepath.substring(filepath.lastIndexOf(File.separator) + 1, filepath.length());
                return filename;
            } else {
                return filename;
            }
        } else {
            return "";
        }
    }

    public static boolean isNumberStr(String str) {
        if(str != null && !str.trim().equals("")) {
            String numstr = "0123456789";
            char[] strchars = str.toCharArray();

            for(int i = 0; i < strchars.length; ++i) {
                if((i != 0 || strchars[0] != 45 && strchars[0] != 43) && numstr.indexOf(strchars[i]) == -1) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public static boolean isPositiveInteger(String str) {
        if(null == str) {
            return false;
        } else {
            String regExp = "^[0-9]*[1-9][0-9]*$";
            Pattern p1 = Pattern.compile(regExp);
            Matcher m1 = p1.matcher(str);
            return m1.matches();
        }
    }

    public static boolean equals(String str1, String delim1, String str2, String delim2) {
        if(str1 != null && str2 != null) {
            if(delim1 != null && delim2 != null) {
                boolean pass = false;
                StringTokenizer answertokenizer = new StringTokenizer(str1, delim1);
                StringTokenizer usranstokenizer = new StringTokenizer(str2, delim2);
                int anscount = answertokenizer.countTokens();
                if(anscount == usranstokenizer.countTokens()) {
                    int i = 0;

                    String[] answers;
                    for(answers = new String[anscount]; answertokenizer.hasMoreTokens(); answers[i++] = answertokenizer.nextToken()) {
                        ;
                    }

                    String token = null;

                    while(usranstokenizer.hasMoreTokens()) {
                        pass = false;
                        token = usranstokenizer.nextToken();

                        for(i = 0; i < anscount; ++i) {
                            if(token.equals(answers[i])) {
                                pass = true;
                                break;
                            }
                        }

                        if(!pass) {
                            break;
                        }
                    }
                }

                return pass;
            } else {
                throw new IllegalArgumentException("指定分隔符不可为空");
            }
        } else {
            return false;
        }
    }

    public static String[] getNonNullStringArray(String[] strs) {
        if(strs == null) {
            return null;
        } else {
            List strList = new ArrayList();
            String[] arr$ = strs;
            int len$ = strs.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String str = arr$[i$];
                if(str != null && !str.equals("")) {
                    strList.add(str);
                }
            }

            if(strList.size() == 0) {
                return null;
            } else {
                return (String[])((String[])strList.toArray(new String[0]));
            }
        }
    }

    public static boolean inStringArray(String[] arg0, String arg1) {
        if(arg0 != null && arg0.length != 0 && arg1 != null) {
            String[] arr$ = arg0;
            int len$ = arg0.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String str = arr$[i$];
                if(arg1.equals(str)) {
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    public static String escapeHTML4(String html) {
        return html == null?"": StringEscapeUtils.escapeHtml4(html);
    }

    public static String escapeHTML(Number html) {
        return html != null?html.toString():null;
    }

    public static String wipeHTML(String html) {
        return html == null?"":html.replaceAll("<[^>]*>|<[^>]*/>|</[^>]*>", "");
    }

    public static String getSetterName(String fieldName) {
        String first = fieldName.substring(0, 1);
        return "set" + first.toUpperCase() + fieldName.substring(1);
    }

    public static String getGetterName(String fieldName) {
        String first = fieldName.substring(0, 1);
        return "get" + first.toUpperCase() + fieldName.substring(1);
    }

    public static boolean isEmptyOrNull(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static boolean isDate(String str) {
        if(isEmptyOrNull(str)) {
            return false;
        } else {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            df.setLenient(true);

            try {
                df.parse(str);
                return true;
            } catch (ParseException var3) {
                return false;
            }
        }
    }

    public static Date getDate(String str) {
        if(isEmptyOrNull(str)) {
            return null;
        } else {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            df.setLenient(true);

            try {
                return df.parse(str);
            } catch (ParseException var3) {
                return null;
            }
        }
    }

    public static Date getNextDate(String str) {
        if(isEmptyOrNull(str)) {
            return null;
        } else {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            df.setLenient(true);

            try {
                df.parse(str);
                df.getCalendar().add(5, 1);
                return df.getCalendar().getTime();
            } catch (ParseException var3) {
                return null;
            }
        }
    }

    public static boolean isEmail(String email) {
        if(isEmptyOrNull(email)) {
            return false;
        } else {
            email = email.trim();
            String expression = "\\b(^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))$)\\b";
            Pattern pattern = Pattern.compile(expression);
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }
    }

    public static List getNonNullStringList(List strs) {
        if(strs == null) {
            return null;
        } else {
            List strList = new ArrayList();

            for(int i = 0; i < strs.size(); ++i) {
                String str = (String)strs.get(i);
                if(str != null && !str.equals("")) {
                    strList.add(str);
                }
            }

            if(strList.size() == 0) {
                return null;
            } else {
                return strList;
            }
        }
    }

    public static String escapeAttr(Object data) {
        return data != null && data.toString() != "null"?data.toString().replace("&", "&#38;").replace("\"", "&#34;").replace("'", "&#39;"):"";
    }

    public static String escapeJS(Object data) {
        return data != null && data.toString() != "null"?StringEscapeUtils.escapeEcmaScript(data.toString()):"";
    }

    public static String escapeHTML(Object data) {
        return data != null && data.toString() != "null"?StringEscapeUtils.escapeHtml3(data.toString()):"";
    }

    public static boolean isNegative(String str) {
        if(isNumber(str)) {
            double d = Double.parseDouble(str);
            return d < 0.0D;
        } else {
            return false;
        }
    }

    public static boolean isPositiveLong(String str) {
        if(isLong(str)) {
            Long d = Long.valueOf(Long.parseLong(str));
            return d.longValue() >= 0L;
        } else {
            return false;
        }
    }

    public static boolean isLong(String str) {
        if(isEmptyOrNull(str)) {
            return false;
        } else {
            try {
                Long.parseLong(str);
                return true;
            } catch (NumberFormatException var2) {
                return false;
            }
        }
    }

    public static String nvl(Object obj) {
        return obj == null?"":String.valueOf(obj);
    }

    public static String replace(String str) {
        if(str != null && !str.trim().equals("")) {
            if(str.startsWith("[")) {
                str = str.substring(1, str.length());
            }

            if(str.endsWith("]")) {
                str = str.substring(0, str.length() - 1);
            }

            if(str.startsWith(",")) {
                str = str.substring(1, str.length());
            }

            if(str.endsWith(",")) {
                str = str.substring(0, str.length() - 1);
            }

            return str;
        } else {
            return str;
        }
    }

    public static int getWordCount(String s) {
        int length = 0;

        for(int i = 0; i < s.length(); ++i) {
            int ascii = Character.codePointAt(s, i);
            if(ascii >= 0 && ascii <= 255) {
                ++length;
            } else {
                length += 2;
            }
        }

        return length;
    }

    public static boolean isUrl(String url) {
        if(isEmpty(url)) {
            return false;
        } else {
            Pattern pattern = Pattern.compile("((http|ftp|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?");
            Matcher matcher = pattern.matcher(url);
            return matcher.find()?matcher.group().equals(url):false;
        }
    }

    public static boolean isHttpUrl(String url) {
        if(isEmpty(url)) {
            return false;
        } else {
            Pattern pattern = Pattern.compile("((http|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?");
            Matcher matcher = pattern.matcher(url);
            return matcher.find()?matcher.group().equals(url):false;
        }
    }

    public static boolean toBoolean(Integer value) {
        return value == null?false:value.intValue() != 0;
    }

    public static String trimString(String value, String defaultStr) {
        String trimString = trimString(value);
        return isEmptyOrNull(trimString)?defaultStr:trimString;
    }

    public static boolean isStandardDecimal(String decimalStr, int decimalPlaces) {
        if(isEmptyOrNull(decimalStr)) {
            return Boolean.FALSE.booleanValue();
        } else {
            Pattern pattern = Pattern.compile(String.format("^(([1-9]\\d*)|(0))(\\.\\d{1,%s})?$", new Object[]{Integer.valueOf(decimalPlaces)}));
            Matcher matcher = pattern.matcher(decimalStr);
            return matcher.matches();
        }
    }

    public static List<Long> transferToLongList(String str) {
        return transferToLongList(str, ",");
    }

    public static List<Long> transferToLongList(String str, String separator) {
        List<Long> longList = new ArrayList();
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(str)) {
            String[] strs = str.split(separator);
            String[] arr$ = strs;
            int len$ = strs.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String string = arr$[i$];
                longList.add(Long.valueOf(string));
            }
        }

        return longList;
    }

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static String quote(String str) {
        return "\"".concat(str).concat("\"");
    }

    public static boolean startWith(String str, String... toChecks) {
        for(int i = 0; i < toChecks.length; ++i) {
            if(str.toLowerCase().startsWith(toChecks[i])) {
                return true;
            }
        }

        return false;
    }

    public static boolean endWith(String str, String... toChecks) {
        for(int i = 0; i < toChecks.length; ++i) {
            if(str.toLowerCase().endsWith(toChecks[i])) {
                return true;
            }
        }

        return false;
    }

    public static String join(Collection list, String symbol) {
        String result = "";
        if(list != null) {
            Iterator i$ = list.iterator();

            while(i$.hasNext()) {
                Object o = i$.next();
                if(o != null) {
                    String temp = o.toString();
                    if(temp.trim().length() > 0) {
                        result = result + temp + symbol;
                    }
                }
            }

            if(result.length() > 1) {
                result = result.substring(0, result.length() - 1);
            }
        }

        return result;
    }

    public static String join(char spliter, String... arrayString) {
        StringBuilder sb = new StringBuilder();
        if(arrayString != null && arrayString.length != 0) {
            for(int i = 0; i < arrayString.length; ++i) {
                if(arrayString[i] != null && arrayString[i].length() > 0) {
                    if(sb.length() > 0) {
                        sb.append(spliter);
                    }

                    sb.append(arrayString[i]);
                }
            }

            return sb.toString();
        } else {
            return null;
        }
    }

    public static boolean isEmptyExcepComma(String str) {
        if(isEmpty(str)) {
            return true;
        } else {
            String[] splits = str.split(",");
            String[] arr$ = splits;
            int len$ = splits.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String split = arr$[i$];
                if(!isEmpty(split)) {
                    return false;
                }
            }

            return true;
        }
    }
}

