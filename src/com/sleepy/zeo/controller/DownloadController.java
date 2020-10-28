package com.sleepy.zeo.controller;

import com.sleepy.zeo.data.Constants;
import com.sleepy.zeo.util.Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import static com.sleepy.zeo.data.Constants.*;

@Controller
@RequestMapping("/download")
public class DownloadController {

    private static final int RANGE_TYPE_NONE = 0; // no Range
    private static final int RANGE_TYPE_FROM_START = 1; // Range: bytes=xx-
    private static final int RANGE_TYPE_LAST_END = 2; // Range: bytes=-xx
    private static final int RANGE_TYPE_FROM_START_TO_END = 3; // Range: bytes=xx-yy
    private static final int RANGE_TYPE_CHUNKED = 5; // Range: bytes=xx-yy,mm-nn,kk-ll

    private static final Log logger = LogFactory.getLog(DownloadController.class);

    @RequestMapping(value = "/{filename:.*}")
    public void download(@PathVariable("filename") String filename,
                         HttpServletRequest request, HttpServletResponse response) throws Exception {
        String downloadDir = request.getSession().getServletContext().getRealPath(Constants.DOWNLOAD_DIR);
        String filePath = downloadDir + File.separator + filename;
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            response.setStatus(HTTP_CODE_NOT_FOUND);
            response.getWriter().write("File " + filename + " not found");
        }
        long targetFileLength = targetFile.length();
        FileInputStream fileInputStream = new FileInputStream(targetFile);

        int rangeType;
        String rangeContent;
        String rangeHeader = request.getHeader(HTTP_HEADER_RANGE_KEY);
        if (rangeHeader == null || rangeHeader.isEmpty() || !rangeHeader.contains("bytes=")) {
            rangeType = RANGE_TYPE_NONE;
            rangeContent = "";
        } else {
            rangeContent = rangeHeader.replaceAll("bytes=", "").trim();
            if (rangeContent.contains(",")) {
                rangeType = RANGE_TYPE_CHUNKED;
            } else {
                if (rangeContent.endsWith("-")) { // xx-
                    rangeType = RANGE_TYPE_FROM_START;
                } else if (rangeContent.startsWith("-")) { // -xx
                    rangeType = RANGE_TYPE_LAST_END;
                } else { // xx-yy
                    rangeType = RANGE_TYPE_FROM_START_TO_END;
                }
            }
        }
        if (rangeType == RANGE_TYPE_NONE) {
            // range not needed
            downloadCommon(response, fileInputStream, filename, targetFileLength);
        } else {
            // range needed
            try {
                // range success
                downloadRange(response, fileInputStream, rangeType, rangeContent, targetFile, filename, targetFileLength);
            } catch (Throwable e) {
                // range failed
                response.setStatus(HTTP_CODE_REQUESTED_RANGE_NOT_SATISFIABLE);
                response.setHeader(HTTP_HEADER_ACCEPT_RANGES_KEY, "bytes");
                response.setHeader(HTTP_HEADER_CONTENT_RANGE_KEY, String.format("bytes */%d", targetFileLength));
                fileInputStream.close();
            }
        }
    }

    private void downloadCommon(HttpServletResponse response, FileInputStream fileInputStream,
                                String filename, long targetFileLength) throws Exception {
        logger.info("downloadCommon");
        response.setHeader(HTTP_HEADER_CONTENT_DISPOSITION, "attachment;filename=" + filename);
        response.setHeader(HTTP_HEADER_CONTENT_LENGTH_KEY, String.valueOf(targetFileLength));
        OutputStream outputStream = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.close();
        fileInputStream.close();
    }

    @SuppressWarnings("Duplicates")
    private void downloadRange(HttpServletResponse response, FileInputStream fileInputStream,
                               int rangeType, String rangeContent, File targetFile, String filename,
                               long targetFileLength) throws Exception {
        logger.info("downloadRange");
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        long needSkipBytes = 0;
        long needWriteBytes = 0;
        response.setStatus(HTTP_CODE_PARTIAL_CONTENT);
        response.setHeader(HTTP_HEADER_ACCEPT_RANGES_KEY, "bytes");
        response.setHeader(HTTP_HEADER_ETAG_KEY, Utils.md5(targetFile));
        response.setHeader(HTTP_HEADER_LAST_MODIFIED_KEY, Utils.getLastModified(targetFile).toString());
        if (rangeType == RANGE_TYPE_FROM_START_TO_END) {
            String[] endpoints = rangeContent.split("-");
            if (endpoints.length != 2) {
                throw new Exception("endpoints length not valid");
            }
            long startValue = Long.parseLong(endpoints[0]);
            long endValue = Long.parseLong(endpoints[1]);
            if (startValue < 0 || endValue < 0 || endValue < startValue || endValue >= targetFileLength) {
                throw new Exception("params not valid");
            }
            needSkipBytes = startValue;
            needWriteBytes = endValue - startValue + 1;
            response.setHeader(HTTP_HEADER_CONTENT_RANGE_KEY, String.format("bytes %d-%d/%d", startValue, endValue, targetFileLength));
            response.setHeader(HTTP_HEADER_CONTENT_LENGTH_KEY, String.valueOf(needWriteBytes));
        } else if (rangeType == RANGE_TYPE_FROM_START) {
            long startValue = Long.parseLong(rangeContent.replace("-", ""));
            if (startValue < 0 || startValue >= targetFileLength) {
                throw new Exception("params not valid");
            }
            needSkipBytes = startValue;
            needWriteBytes = targetFileLength - startValue;
            response.setHeader(HTTP_HEADER_CONTENT_RANGE_KEY, String.format("bytes %d-%d/%d", startValue, targetFileLength - 1, targetFileLength));
            response.setHeader(HTTP_HEADER_CONTENT_LENGTH_KEY, String.valueOf(needWriteBytes));
        } else if (rangeType == RANGE_TYPE_LAST_END) {
            long lastBytes = Long.parseLong(rangeContent.replace("-", ""));
            if (lastBytes < 0 || lastBytes > targetFileLength) {
                throw new Exception("params not valid");
            }
            long startValue = targetFileLength - lastBytes;
            long endValue = targetFileLength - 1;
            needSkipBytes = startValue;
            needWriteBytes = lastBytes;
            response.setHeader(HTTP_HEADER_CONTENT_RANGE_KEY, String.format("bytes %d-%d/%d", startValue, endValue, targetFileLength));
            response.setHeader(HTTP_HEADER_CONTENT_LENGTH_KEY, String.valueOf(needWriteBytes));
        } else {
            // just for range chunked
            response.setHeader(HTTP_HEADER_CONTENT_TYPE_KEY, "multipart/byteranges; boundary=" + CATALINA_MIME_BOUNDARY);
            downloadRangeChunked(response, bufferedInputStream, rangeContent, targetFileLength);
            return;
        }
        bufferedInputStream.skip(needSkipBytes);

        byte[] buffer = new byte[1024];
        int length;
        long readLength = 0;
        OutputStream outputStream = response.getOutputStream();
        while ((length = bufferedInputStream.read(buffer)) != -1) {
            if (readLength + length > needWriteBytes) {
                int needWriteLength = (int) (needWriteBytes - readLength);
                outputStream.write(buffer, 0, needWriteLength);
                break;
            } else if (readLength + length == needSkipBytes) {
                outputStream.write(buffer, 0, length);
                break;
            } else {
                outputStream.write(buffer, 0, length);
                readLength += length;
            }
        }
        outputStream.close();
        bufferedInputStream.close();
    }

    @SuppressWarnings("Duplicates")
    private void downloadRangeChunked(HttpServletResponse response, BufferedInputStream bufferedInputStream,
                                      String rangeContent, long targetFileLength) throws Exception {
        logger.info("downloadRangeChunked");
        String[] endpoints = rangeContent.split(",");
        OutputStream outputStream = response.getOutputStream();
        for (int i = 0; i < endpoints.length; ++i) {
            String endpoint = endpoints[i];
            // mark buffer to 0
            bufferedInputStream.mark(0);
            outputStream.write(("\r\n--" + CATALINA_MIME_BOUNDARY + "\r\n").getBytes());
            outputStream.write((HTTP_HEADER_CONTENT_TYPE_KEY + ": text/plain\r\n").getBytes());
            long needSkipBytes = 0;
            long needWriteBytes = 0;
            if (endpoint.startsWith("-")) { // -xx
                long lastBytes = Long.parseLong(endpoint.replace("-", ""));
                if (lastBytes < 0 || lastBytes > targetFileLength) {
                    throw new Exception("params not valid");
                }
                long startValue = targetFileLength - lastBytes;
                long endValue = targetFileLength - 1;
                needSkipBytes = startValue;
                needWriteBytes = lastBytes;

                outputStream.write(String.format(HTTP_HEADER_CONTENT_RANGE_KEY + ": bytes %d-%d/%d\r\n",
                        startValue, targetFileLength - 1, targetFileLength).getBytes());
                outputStream.write("\r\n".getBytes());
            } else if (endpoint.endsWith("-")) { // yy-
                long startValue = Long.parseLong(endpoint.replace("-", ""));
                if (startValue < 0 || startValue >= targetFileLength) {
                    throw new Exception("params not valid");
                }
                needSkipBytes = startValue;
                needWriteBytes = targetFileLength - startValue;

                outputStream.write(String.format(HTTP_HEADER_CONTENT_RANGE_KEY + ": bytes %d-%d/%d\r\n", startValue,
                        targetFileLength - 1, targetFileLength).getBytes());
                outputStream.write("\r\n".getBytes());
            } else { // xx-yy
                String[] values = endpoint.split("-");
                if (values.length != 2) {
                    throw new Exception("value length not valid");
                }
                long startValue = Long.parseLong(values[0]);
                long endValue = Long.parseLong(values[1]);
                if (startValue < 0 || endValue < 0 || endValue < startValue || endValue >= targetFileLength) {
                    throw new Exception("params not valid");
                }
                needSkipBytes = startValue;
                needWriteBytes = endValue - startValue + 1;

                outputStream.write(String.format(HTTP_HEADER_CONTENT_RANGE_KEY + ": bytes %d-%d/%d\r\n", startValue, endValue, targetFileLength).getBytes());
                outputStream.write("\r\n".getBytes());
            }

            bufferedInputStream.skip(needSkipBytes);
            byte[] buffer = new byte[1024];
            int length;
            long readLength = 0;

            while ((length = bufferedInputStream.read(buffer)) != -1) {
                if (readLength + length > needWriteBytes) {
                    int needWriteLength = (int) (needWriteBytes - readLength);
                    outputStream.write(buffer, 0, needWriteLength);
                    break;
                } else if (readLength + length == needSkipBytes) {
                    outputStream.write(buffer, 0, length);
                    break;
                } else {
                    outputStream.write(buffer, 0, length);
                    readLength += length;
                }
            }
            // reset buffer to 0
            bufferedInputStream.reset();
            if (i == endpoints.length - 1) {
                outputStream.write(("\r\n--" + CATALINA_MIME_BOUNDARY + "--").getBytes());
            }
        }
        outputStream.close();
        bufferedInputStream.close();
    }
}
