package zixiaowangfall2020.webapp;

import org.junit.jupiter.api.Test;
import zixiaowangfall2020.webapp.util.RegexHelper;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/

public class RegexTest {

    @Test
    public void betterThanNullTest(){
        String password = "fjw";
        assert ((Integer)RegexHelper.checkPassordStrength(password).values().toArray()[0])==-1;
    }

    @Test
    public void lowercaseTest(){
        String password = "fjwwzxqweasd";
        assert ((Integer)RegexHelper.checkPassordStrength(password).values().toArray()[0])==1;
    }

    @Test
    public void uppercaseTest(){
        String password = "WZXQWEASDZXC";
        assert ((Integer)RegexHelper.checkPassordStrength(password).values().toArray()[0])==1;
    }

    @Test
    public void lowerAndUppercaseTest(){
        String password = "fjwwzxqwSDFeasd";
        assert ((Integer)RegexHelper.checkPassordStrength(password).values().toArray()[0])==2;
    }

    @Test
    public void numberTest(){
        String password = "123456789";
        assert ((Integer)RegexHelper.checkPassordStrength(password).values().toArray()[0])==1;
    }

    @Test
    public void complexTest(){
        String password = "fjwWZX970814";
        assert ((Integer)RegexHelper.checkPassordStrength(password).values().toArray()[0])==3;
    }
}
