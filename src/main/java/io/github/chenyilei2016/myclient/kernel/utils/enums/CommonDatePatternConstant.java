package io.github.chenyilei2016.myclient.kernel.utils.enums;

/**
 * @author chenyilei
 * @date 2023/02/22 15:28
 */
public interface CommonDatePatternConstant {

    /**
     * yyyyMMdd
     * 20120312
     */
    String yyyyMMdd = "^\\d{4}\\d{2}\\d{2}$";

    /**
     * 2023-01-02
     */
    String yyyy_mm_dd = "^\\d{4}-\\d{2}-\\d{2}$";

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    String yyyy_MM_dd_HH_mm_ss = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$";
}
