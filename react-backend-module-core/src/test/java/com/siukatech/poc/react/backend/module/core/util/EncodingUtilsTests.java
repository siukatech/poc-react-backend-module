package com.siukatech.poc.react.backend.module.core.util;

import com.github.houbb.opencc4j.util.ZhConverterUtil;
import com.siukatech.poc.react.backend.module.core.AbstractUnitTests;
import org.javatuples.Pair;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.springframework.test.util.AssertionErrors.*;

public class EncodingUtilsTests extends AbstractUnitTests {

//    @Test
    void contextLoads() {
    }

    /**
     * Reference:
     * https://stackoverflow.com/a/26358371
     * https://blog.csdn.net/seabiscuityj/article/details/80338891
     * Character.UnicodeScript.HAN
     * Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
     *
     * @throws Exception
     */
    @Test
    void test_containsChinese() throws Exception {
        String strWord = "this is a 測試";
        String strPunctuation = "this is a test ，。";
        boolean resultUnicodeScript = EncodingUtils.containsChineseHans(strWord);
        boolean resultUnicodeBlock = EncodingUtils.containsChineseUnicodeBlock(strWord);
        boolean resultWord = EncodingUtils.containsChineseWord(strWord);
        boolean resultPunctuation = EncodingUtils.containsChinesePunctuation(strPunctuation);
        log.debug("test_containsChinese - resultUnicodeScript: [{}]/[{}]", resultUnicodeScript, true);
        log.debug("test_containsChinese - resultUnicodeBlock: [{}]/[{}]", resultUnicodeBlock, true);
        log.debug("test_containsChinese - resultWord: [{}]/[{}]", resultWord, true);
        log.debug("test_containsChinese - resultPunctuation: [{}]/[{}]", resultPunctuation, true);
        assertEquals("excepted resultUnicodeScript is true", true, resultUnicodeScript);
        assertEquals("excepted resultUnicodeBlock is true", true, resultUnicodeBlock);
        assertEquals("excepted resultWord is true", true, resultWord);
        assertEquals("excepted resultPunctuation is true", true, resultPunctuation);
    }

    @Test
    void test_opencc4j() throws Exception {

        // contains simplified chinese
        List<Pair<String, Boolean>> containSimpleStrList = List.of(Pair.with("奋", true)
                , Pair.with("奋斗", true)
                , Pair.with("奋斗2023", true)
                , Pair.with("編", false)
                , Pair.with("編號", false)
                , Pair.with("奋斗", true)
        );
        for (Pair<String, Boolean> pair : containSimpleStrList) {
            log.debug(String.format("test_opencc4j - expected containsSimple [%s] is [%s]", pair.getValue0(), pair.getValue1()));
            assertEquals(String.format("expected containsSimple [%s] is [%s]", pair.getValue0(), pair.getValue1())
                    , pair.getValue1(), ZhConverterUtil.containsSimple(pair.getValue0()));
        }

        // isTraditional
        List<Pair<String, Boolean>> isTraditionalCharList = List.of(Pair.with("編", true)
                , Pair.with("编", false)
        );
        for (Pair<String, Boolean> pair : isTraditionalCharList) {
            for (char ccc : pair.getValue0().toCharArray()) {
                log.debug(String.format("test_opencc4j - expected isTraditional [%s][%c] is [%s]", pair.getValue0(), ccc, pair.getValue1()));
                assertEquals(String.format("expected isTraditional [%s][%c] is [%s]", pair.getValue0(), ccc, pair.getValue1())
                        , pair.getValue1(), ZhConverterUtil.isTraditional(ccc));
            }
        }
        List<Pair<String, Boolean>> isTraditionalStrList = List.of(Pair.with("編", true)
                , Pair.with("編號", true)
                , Pair.with("编", false)
                , Pair.with("编号", false)
                , Pair.with("编號", false)
        );
        for (Pair<String, Boolean> pair : isTraditionalStrList) {
            log.debug(String.format("test_opencc4j - expected isTraditional [%s] is [%s]", pair.getValue0(), pair.getValue1()));
            assertEquals(String.format("expected isTraditional [%s] is [%s]", pair.getValue0(), pair.getValue1())
                    , pair.getValue1(), ZhConverterUtil.isTraditional(pair.getValue0()));
        }

    }

}
