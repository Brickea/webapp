package zixiaowangfall2020.webapp.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/

public class RegexHelper {
    // Number
    public static final String REG_NUMBER = ".*\\d+.*";
    // Lower case
    public static final String REG_UPPERCASE = ".*[A-Z]+.*";
    // Upper case
    public static final String REG_LOWERCASE = ".*[a-z]+.*";
    // Special symbol (~!@#$%^&*()_+|<>,.?/:;'[]{}\)
    public static final String REG_SYMBOL = ".*[~!@#$%^&*()_+|<>,.?/:;'\\[\\]{}\"]+.*";

    /**
     * @author: Zixiao Wang
     * @date: 10/1/20
     * @param: password
     * @return: Map<String, Integer>
     * @description:
     * Return the strength description of password and the level of it.
     **/
    public static Map<String, Integer> checkPassordStrength(String password) {
        Map<String,Integer> res = new HashMap<>();
        if (password == null || password.length() < 8 || password.length() > 30) {
            res.put("Better than null",-1);
            return res;
        }

        int i = 0;

        if (password.matches(RegexHelper.REG_NUMBER)) i++;
        if (password.matches(RegexHelper.REG_LOWERCASE)) i++;
        if (password.matches(RegexHelper.REG_UPPERCASE)) i++;
        if (password.matches(RegexHelper.REG_SYMBOL)) i++;


        switch (i){
            case 0:{
                res.clear();
                res.put("So week",0);
                break;
            }
            case 1:{
                res.clear();
                res.put("Numbers",1);
                break;
            }
            case 2:{
                res.clear();
                res.put("Numbers, lowercase",2);
                break;
            }
            case 3:{
                res.clear();
                res.put("Numbers, lowercase, uppercase",3);
                break;
            }
            case 4:{
                res.clear();
                res.put("Numbers, lowercase, uppercase,special symbol",4);
                break;
            }
        }

        return res;
    }
}
