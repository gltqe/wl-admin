package com.gltqe.wladmin.commons.utils;

import com.gltqe.wladmin.commons.enums.FileTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;

/**
 * 文件工具类
 *
 * @author gltqe
 * @date 2022/7/3 0:22
 **/
@Slf4j
public class FileUtil {
    public final static String MB = "1048576";
    public final static String KB = "1024";
    public final static String JPG = ".jpg";
    public final static String PNG = ".png";
    public final static String TXT = ".txt";
    public final static String DOC = ".doc";
    public final static String PDF = ".pdf";

    /**
     * 指定图片宽度和高度和压缩比例对图片进行压缩
     *
     * @param imgSrc    源图片地址
     * @param imgTar    目标图片地址
     * @param widthTar  缩后图片的宽度
     * @param heightTar 压缩后图片的高度
     * @param rate      压缩的比例
     * @author
     * @date 2022/7/3 0:22
     **/
    public static void compressWHS(String imgSrc, String imgTar, int widthTar, int heightTar, Float rate) {
        File srcFile = new File(imgSrc);
        // 检查文件是否存在
        if (!srcFile.exists()) {
            log.error("文件不存在");
        }
        // 如果比例不为空则说明是按比例压缩
        if (rate != null && rate > 0) {
            //获得源图片的宽高存入数组中
            int[] results = getImgWidthHeight(srcFile);
            if (results == null || results[0] == 0 || results[1] == 0) {
                return;
            } else {
                //按比例缩放或扩大图片大小，将浮点型转为整型
                widthTar = (int) (results[0] * rate);
                heightTar = (int) (results[1] * rate);
            }
        }
        //创建文件输出流
        try (FileOutputStream fos = new FileOutputStream(imgTar)) {
            // 开始读取文件并进行压缩
            Image src = ImageIO.read(srcFile);
            // 构造一个类型为预定义图像类型之一的 BufferedImage
            BufferedImage tag = new BufferedImage(widthTar, heightTar, BufferedImage.TYPE_INT_RGB);
            //绘制图像  getScaledInstance表示创建此图像的缩放版本，返回一个新的缩放版本Image,按指定的width,height呈现图像
            //Image.SCALE_SMOOTH,选择图像平滑度比缩放速度具有更高优先级的图像缩放算法。
            tag.getGraphics().drawImage(src.getScaledInstance(widthTar, heightTar, Image.SCALE_SMOOTH), 0, 0, null);
            //文件类型
            String formatName = imgSrc.substring(imgSrc.lastIndexOf(".") + 1);
            ImageIO.write(tag, formatName, fos);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("图片压缩出现异常");
        }
    }

    /**
     * 获取图片宽度和高度
     *
     * @param file
     * @return int[] 返回图片的宽高
     * @author
     * @date 2022/7/3 0:23
     **/
    public static int[] getImgWidthHeight(File file) {
        BufferedImage src = null;
        int result[] = {0, 0};
        // 获得文件输入流
        try (InputStream is = new FileInputStream(file)) {
            // 从流里将图片写入缓冲图片区
            src = ImageIO.read(is);
            // 得到源图片宽
            result[0] = src.getWidth(null);
            // 得到源图片高
            result[1] = src.getHeight(null);
        } catch (Exception e) {
            log.error("获取图片宽高出现异常");
        }
        return result;
    }

    /**
     * 获取图片文件字节
     *
     * @param imageFile
     * @return byte[]
     * @author gltqe
     * @date 2022/7/3 0:23
     **/
    public static byte[] getByteByPic(File imageFile) throws IOException {
        InputStream inStream = new FileInputStream(imageFile);
        BufferedInputStream bis = new BufferedInputStream(inStream);
        BufferedImage bm = ImageIO.read(bis);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        String imageUrl = imageFile.getAbsolutePath();
        String type = imageUrl.substring(imageUrl.length() - 3);
        ImageIO.write(bm, type, bos);
        bos.flush();
        bos.close();
        bis.close();
        byte[] data = bos.toByteArray();
        return data;
    }

    /**
     * 获取文件后缀名
     *
     * @param fileName
     * @return java.lang.String
     * @author gltqe
     * @date 2022/7/3 0:24
     **/
    public static String getSuffixName(String fileName) {
        return StringUtils.isNotBlank(fileName) && fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".")) : "";
    }

    /**
     * 获取文件类型
     *
     * @param suffix
     * @return java.lang.String
     * @author gltqe
     * @date 2022/7/3 0:25
     **/
    public static Integer getTypeCode(String suffix) {
        if (JPG.equals(suffix) || PNG.equals(suffix)) {
            return FileTypeEnum.IMAGE.getCode();
        } else if (PDF.equals(suffix)) {
            return FileTypeEnum.PDF.getCode();
        } else {
            return FileTypeEnum.OTHER.getCode();
        }
    }

    /**
     * 获取文件大小MB
     *
     * @param size
     * @return BigDecimal
     * @author gltqe
     * @date 2022/7/3 0:27
     **/
    public static BigDecimal getFileSizeMB(Long size) {
        BigDecimal sizeDecimal = new BigDecimal(size);
        BigDecimal mbDecimal = new BigDecimal(MB);
        BigDecimal sizeMB = sizeDecimal.divide(mbDecimal, 2, BigDecimal.ROUND_HALF_UP);
        return sizeMB;
    }

    /**
     * 获取文件大小KB
     *
     * @param size
     * @return BigDecimal
     * @author gltqe
     * @date 2022/7/3 0:27
     **/
    public static BigDecimal getFileSizeKB(Long size) {
        BigDecimal sizeDecimal = new BigDecimal(size);
        BigDecimal mbDecimal = new BigDecimal(KB);
        BigDecimal sizeKB = sizeDecimal.divide(mbDecimal, 2, BigDecimal.ROUND_HALF_UP);
        return sizeKB;
    }
}
