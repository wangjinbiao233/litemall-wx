package org.linlinjava.litemall.db.util.runner;


import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.linlinjava.litemall.db.util.FFmpegUtils;
import org.linlinjava.litemall.db.util.FileUtils;
import org.linlinjava.litemall.db.util.entity.HLS;
import org.linlinjava.litemall.db.util.entity.VideoFile;
import org.linlinjava.litemall.db.util.entity.VideoInfo;
import org.linlinjava.litemall.db.util.handler.DefaultCallbackHandler;
import org.linlinjava.litemall.db.util.handler.ProcessCallbackHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by tonydeng on 15/4/16.
 */
public class FFmpegCommandRunner {
    private static final Logger log = LoggerFactory.getLogger(FFmpegCommandRunner.class);
    private static ProcessBuilder pb = null;
    private static Process process = null;

    /**
     * 获取视频信息
     *
     * @param input
     * @return
     */
    public static VideoInfo getVideoInfo(File input) {
        VideoInfo vi = new VideoInfo();
        if (input != null && input.exists()) {
            List<String> commands = Lists.newArrayList(BaseCommandOption.getFFprobeBinary());
            commands.add(input.getAbsolutePath());
            vi.setSize(FileUtils.getFineSize(input));
            if (vi.getSize() > 0) {
                return FFmpegUtils.regInfo(runProcess(commands), vi);
            }
        } else {
            if (log.isErrorEnabled())
                log.error("video '{}' is not fount! ", input.getAbsolutePath());
        }

        return vi;
    }

    /**
     * 视频截图
     *
     * @param input
     * @param shotSecond,0 for random capture screen shot
     * @return
     */
    public static File screenshot(File input, int shotSecond) {
        File output = FileUtils.getSrceentshotOutputByInput(input);
        if (output != null) {
            VideoInfo vi = getVideoInfo(input);
            List<String> commands = Lists.newArrayList(BaseCommandOption.getFFmpegBinary());
            commands.addAll(BaseCommandOption.toScreenshotCmdArrays(input.getAbsolutePath(), output.getAbsolutePath(), shotSecond, vi));
            if (StringUtils.isNotEmpty(runProcess(commands))) {
                return output;
            }
        } else {
            if (log.isErrorEnabled())
                log.error("video '{}' screentshot '{}' create error!", input.getAbsolutePath(), output.getAbsolutePath());
        }
        return null;
    }

    /**
     * 生成HLS
     *
     * @param input
     * @param cutSecond
     * @param tsBaseUrl
     * @return
     */
    public static HLS generationHls(File input, int cutSecond, String tsBaseUrl) {
        File output = FileUtils.getM3U8OutputByInput(input);
        if (output != null) {
            VideoInfo vi = getVideoInfo(input);
            List<String> commands = Lists.newArrayList(BaseCommandOption.getFFmpegBinary());
            commands.addAll(BaseCommandOption.toHLSCmdArrays(input.getAbsolutePath(), output.getAbsolutePath(), cutSecond, tsBaseUrl, vi));
            if (StringUtils.isNotEmpty(runProcess(commands))) {
                HLS hls = new HLS();
                hls.setM3u8(output);
                hls.setTs(FileUtils.findTS(output));
                return hls;
            }

        } else {
            if (log.isErrorEnabled())
                log.error("vidoe '{}' m3u8 '{}' create error!", input.getAbsolutePath(), output.getAbsolutePath());
        }

        return null;
    }
    /**
     * 转换视频格式为MP4
     *
     * @param input
     * @return
     */
    public static VideoFile coverToMp4_GeneralCopy(File input, String output_filename) {
        VideoFile vf = new VideoFile(input, FileUtils.getMp4OutputByFilename(input,output_filename));
        if (vf.getTarget() != null && !vf.getTarget().exists()) {
            vf.setInputInfo(getVideoInfo(input));
            if (vf.getInputInfo().getSize() > 0
                   /** && !BaseCommandOption.H264.equals(vf.getInputInfo().getFormat()) **/) {
                List<String> commands = Lists.newArrayList(BaseCommandOption.getFFmpegBinary());

                commands.addAll(BaseCommandOption.toMP4GerneralCopyCmdArrays(
                        input.getAbsolutePath(),
                        vf.getTarget().getAbsolutePath(),
                        vf.getInputInfo()
                ));

                if (StringUtils.isNotEmpty(runProcess(commands))) {
                    vf.setTargetInfo(getVideoInfo(vf.getTarget()));
                    vf.setSuccess(true);
                    return vf;
                }
            }

        }
        return vf;
    }
    /**
     * 转换视频格式为MP4
     *
     * @param input
     * @return
     */
    public static VideoFile coverToMp4(File input) {
        VideoFile vf = new VideoFile(input, FileUtils.getMp4OutputByInput(input));
        if (vf.getTarget() != null && !vf.getTarget().exists()) {
            vf.setInputInfo(getVideoInfo(input));
            if (vf.getInputInfo().getSize() > 0
                   /** && !BaseCommandOption.H264.equals(vf.getInputInfo().getFormat()) **/) {
                List<String> commands = Lists.newArrayList(BaseCommandOption.getFFmpegBinary());

                commands.addAll(BaseCommandOption.toMP4CmdArrays(
                        input.getAbsolutePath(),
                        vf.getTarget().getAbsolutePath(),
                        vf.getInputInfo()
                ));

                if (StringUtils.isNotEmpty(runProcess(commands))) {
                    vf.setTargetInfo(getVideoInfo(vf.getTarget()));
                    vf.setSuccess(true);
                    return vf;
                }
            }

        }
        return vf;
    }

    /**
     * 执行命令
     *
     * @param commands
     * @return
     */
    public static String runProcess(List<String> commands) {
        try {
            return runProcess(commands, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 执行命令
     *
     * @param commands
     * @param handler
     * @return
     * @throws Exception
     */
    public static String runProcess(List<String> commands, ProcessCallbackHandler handler) {
        if (log.isDebugEnabled())
            log.debug("start to run ffmpeg process... cmd : '{}'", FFmpegUtils.ffmpegCmdLine(commands));
        Stopwatch stopwatch = Stopwatch.createStarted();
        pb = new ProcessBuilder(commands);

        pb.redirectErrorStream(true);


        if (null == handler) {
            handler = new DefaultCallbackHandler();
        }

        String result = null;
        try {
            process = pb.start();
            result = handler.handler(process.getInputStream());
        } catch (Exception e) {
            log.error("errorStream:{}", result, e);
        } finally {
            if (null != process) {
                try {
                    process.getInputStream().close();
                    process.getOutputStream().close();
                    process.getErrorStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            int flag = process.waitFor();
            if (flag != 0) {
                throw new IllegalThreadStateException("process exit with error value : " + flag);
            }
        } catch (InterruptedException e) {
            log.error("wait for process finish error:{}", e);
        } finally {
            if (null != process) {
                process.destroy();
                pb = null;
            }

            stopwatch.stop();
        }
        if (log.isInfoEnabled()) {
            log.info("ffmpeg run {} seconds, {} milliseconds",
                    stopwatch.elapsed(TimeUnit.SECONDS),
                    stopwatch.elapsed(TimeUnit.MILLISECONDS));
        }
        return result;
    }
}
