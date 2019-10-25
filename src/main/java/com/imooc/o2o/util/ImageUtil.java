package com.imooc.o2o.util;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


/**
 * 封装Thumbnailator方法
 */
public class ImageUtil {
    private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Random r = new Random();
    private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    /**
     * 加水印生成缩略图，并返回新生成图片的相对路径
     * @param thumbnail
     * @param targetAddr
     * @return
     */
    public static String generateThumbnail(File thumbnail, String targetAddr) {
        // 为防止文件重名，随机生成新的名字
        String realFileName = getRandomFileName();
        String extension = getFileExtension(thumbnail);
        makeDirPath(targetAddr);
        String relativeAddr = targetAddr + realFileName + extension;
        logger.debug("current relativeAddr is: " + relativeAddr);
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
        logger.debug("complete addr is: " + PathUtil.getImgBasePath() + relativeAddr);
        try {
            Thumbnails.of(thumbnail)
                    .size(200, 200)
                    .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.jpg")), 0.25f)
                    .outputQuality(0.8f)
                    .toFile(dest);
        } catch (IOException e) {
            logger.error(e.toString());
            e.printStackTrace();
        }

        // 需要返回并更新数据库中的shop_img，返回相对路径利于不同操作系统之间的迁移
        return relativeAddr;
    }

    public static File transferCommonsMultipartFileToFile(CommonsMultipartFile cFile){
        File file = new File(cFile.getOriginalFilename());
        try {
            cFile.transferTo(file);
        } catch (IOException e) {
            logger.error(e.toString());
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 生成随机文件名，当前年月日时分秒+五位随机数
     * @return
     */
    private static String getRandomFileName() {
        int random = r.nextInt(89999) + 10000;
        String timeStr = sDateFormat.format(new Date());
        return timeStr + random;
    }

    private static String getFileExtension(File file) {
        String original = file.getName();
        return original.substring(original.lastIndexOf("."));
    }

    /**
     * 创建目标路径所涉及到的所有层级的目录
     * @param targetAddr
     */
    private static void makeDirPath(String targetAddr) {
        String absolutePath = PathUtil.getImgBasePath() + targetAddr;
        File dirPath = new File(absolutePath);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }
    }


    public static void main(String[] args) throws IOException {
        String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        Thumbnails.of(new File("E:/wz/image/xiaohuangren.jpg"))
                .size(500, 500)
                .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.jpg")), 0.5f)
                .outputQuality(0.8)
                .toFile(new File("E:/wz/image/xiaohuangren-with-watermark.jpg"));
    }
}
