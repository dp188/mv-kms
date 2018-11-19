package com.zhixin.core.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * 
 * @author zhangtiebin@bwcmall.com
 * @description  
 * @class PNGUtil
 * @package com.zhixin.core.utils
 * @Date 2016年1月16日 下午10:26:27
 */
public class PNGUtil {
	// 根据str,font的样式以及输出文件目录
	public static void createImage(String str, Font font, File outFile) throws Exception {
		// 获取font的样式应用在str上的整个矩形
		Rectangle2D r = font.getStringBounds(str,
				new FontRenderContext(AffineTransform.getScaleInstance(1, 1), false, false));
		int height = (int) Math.floor(r.getHeight());// 获取单个字符的高度
		// 获取整个str用了font样式的宽度这里用四舍五入后+1保证宽度绝对能容纳这个字符串作为图片的宽度
		int width = (int) Math.round(r.getWidth()) + 1;
		// 创建BufferedImage对象
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// 获取Graphics2D
		Graphics2D g2d = image.createGraphics();
		// ---------- 增加下面的代码使得背景透明 -----------------
		image = g2d.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
		g2d.dispose();
		g2d = image.createGraphics();
		// ---------- 背景透明代码结束 -----------------
		// 画图
		g2d.setColor(new Color(255, 0, 0));
		g2d.setStroke(new BasicStroke(1));
		g2d.setFont(font);// 设置画笔字体
		g2d.drawString(str, 0, font.getSize());// 画出字符串
		// 释放对象
		g2d.dispose();
		// 保存文件
		ImageIO.write(image, "png", outFile);
	}

}
