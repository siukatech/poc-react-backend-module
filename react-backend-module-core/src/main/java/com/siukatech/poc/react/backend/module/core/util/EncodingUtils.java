package com.siukatech.poc.react.backend.module.core.util;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Reference:
 * https://stackoverflow.com/a/26358371
 * https://blog.csdn.net/seabiscuityj/article/details/80338891
 * Character.UnicodeScript.HAN
 * Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
 *
 */
@Slf4j
@NoArgsConstructor
public class EncodingUtils {

    public static List<Character.UnicodeBlock> UNICODE_BLOCK_CJKS = List.of(
            Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
            , Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
            , Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
            , Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C
            , Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D
            , Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_E
            , Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_F
            , Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_G
//            , Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_I
            , Character.UnicodeBlock.CJK_COMPATIBILITY
            , Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS
            , Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
            , Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT
            , Character.UnicodeBlock.CJK_STROKES
            , Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
            , Character.UnicodeBlock.ENCLOSED_CJK_LETTERS_AND_MONTHS
            , Character.UnicodeBlock.ENCLOSED_IDEOGRAPHIC_SUPPLEMENT
            , Character.UnicodeBlock.IDEOGRAPHIC_DESCRIPTION_CHARACTERS
    );
    public static List<Character.UnicodeBlock> UNICODE_BLOCK_CJK_WORDS = List.of(
            Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
            , Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
            , Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
            , Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C
            , Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D
            , Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_E
            , Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_F
            , Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_G
//            , Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_I
            , Character.UnicodeBlock.CJK_COMPATIBILITY
            , Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS
            , Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
            , Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT
    );
    public static List<Character.UnicodeBlock> UNICODE_BLOCK_CJK_PUNCTUATIONS = List.of(
            Character.UnicodeBlock.GENERAL_PUNCTUATION
            , Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
            , Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
            , Character.UnicodeBlock.CJK_COMPATIBILITY
            , Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS
            , Character.UnicodeBlock.VERTICAL_FORMS
    );

    public static boolean containsChineseHans(String str) {
        boolean result = str.codePoints()
                .anyMatch(codePoint -> Character.UnicodeScript.of(codePoint) == Character.UnicodeScript.HAN)
                ;
        return result;
    }

    public static boolean containsChineseUnicodeBlock(String str) {
        boolean result = str.codePoints()
                .anyMatch(codePoint -> {
                    boolean r = false;
                    Character.UnicodeBlock ub = Character.UnicodeBlock.of(codePoint);
                    if (UNICODE_BLOCK_CJKS.contains(ub)) {
                        r = true;
                    }
                    return r;
                })
                ;
        return result;
    }

    public static boolean containsChineseWord(String str) {
        boolean result = str.codePoints()
                .anyMatch(codePoint -> {
                    boolean r = false;
                    Character.UnicodeBlock ub = Character.UnicodeBlock.of(codePoint);
                    if (UNICODE_BLOCK_CJK_WORDS.contains(ub)) {
                        r = true;
                    }
                    return r;
                })
                ;
        return result;
    }

    public static boolean containsChinesePunctuation(String str) {
        boolean result = str.codePoints()
                .anyMatch(codePoint -> {
                    boolean r = false;
                    Character.UnicodeBlock ub = Character.UnicodeBlock.of(codePoint);
                    if (UNICODE_BLOCK_CJK_PUNCTUATIONS.contains(ub)) {
                        r = true;
                    }
                    return r;
                })
                ;
        return result;
    }

}
