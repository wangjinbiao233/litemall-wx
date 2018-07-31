package org.linlinjava.litemall.wx.web;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import org.linlinjava.litemall.db.util.MatrixToImageWriter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

@RestController
@RequestMapping("/wx/matrix")
public class WxMatrixController {


    /**
     * 在线生成二维码
     * <p>
     * 调用方式
     * <img src="http://localhost:8082/wx/matrix/matrixToImage?text=lingfeng&width=300&height=300&format=jpg" alt="matrix">
     *
     * @param response
     * @param text     二维码内容
     * @param width    宽度 ,默认 300
     * @param height   高度 ,默认 300
     * @param format   图片格式 ,默认jpg
     */
    @RequestMapping("matrixToImage")
    public void matrixToImage(HttpServletResponse response, String text,
                              @RequestParam(value = "width", defaultValue = "300") Integer width,
                              @RequestParam(value = "height", defaultValue = "300") Integer height,
                              @RequestParam(value = "format", defaultValue = "jpg") String format) {

        response.addHeader("Content-Disposition", "attachment;filename=" + "matrix.jpg");
        response.setContentType("application/octet-stream");

        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        /* 内容所使用字符集编码 */
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        OutputStream out = null;
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);

            /* 成二维码 */
            out = response.getOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, format, out);

        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
