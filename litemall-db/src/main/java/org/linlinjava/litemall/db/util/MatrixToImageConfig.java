package org.linlinjava.litemall.db.util;

import java.awt.image.BufferedImage;

/**
 * 二维码配置类
 *
 *
 */
public class MatrixToImageConfig {
    public static final int BLACK = 0xFF000000;//16进制颜色代码
    public static final int WHITE = 0xFFFFFFFF;//16进制颜色代码

    private final int onColor; //图案的颜色
    private final int offColor;//背景颜色

    /**
     * 创建一个默认的配置
     * black-on-white barcodes.
     */
    public MatrixToImageConfig() {
        this(BLACK, WHITE);
    }

    /**
     * @param onColor 图案颜色代码, 指定一个 ARGB 值
     * @param offColor 背景颜色代码, 指定一个 ARGB 值
     */
    public MatrixToImageConfig(int onColor, int offColor) {
        this.onColor = onColor;
        this.offColor = offColor;
    }

    public int getPixelOnColor() {
        return onColor;
    }

    public int getPixelOffColor() {
        return offColor;
    }

    int getBufferedImageColorModel() {
        if (onColor == BLACK && offColor == WHITE) {
            // Use faster BINARY if colors match default
            return BufferedImage.TYPE_BYTE_BINARY;
        }
        if (hasTransparency(onColor) || hasTransparency(offColor)) {
            // Use ARGB representation if colors specify non-opaque alpha
            return BufferedImage.TYPE_INT_ARGB;
        }
        // Default otherwise to RGB representation with ignored alpha channel
        return BufferedImage.TYPE_INT_RGB;
    }

    private static boolean hasTransparency(int argb) {
        return (argb & 0xFF000000) != 0xFF000000;
    }

}
