package com.zhixin.service.task.command;

import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhixin.core.common.exceptions.ApiException;
import com.zhixin.core.enums.ErrorCodeEnum;

/**
 * ffmpeg命令执行工具类
 * 
 * @author zhangtiebin@bwcmall.com
 * @description
 * @class FFmpegHelper
 * @package com.zhixin.service.task.command
 * @Date 2016年1月16日 下午11:19:22
 */
public class FFmpegHelper {
	private static final Logger log = LoggerFactory.getLogger(FFmpegHelper.class);

	/**
	 * 执行ffmpeg命令
	 * 
	 * @param command
	 */
	public static StringBuffer executeFFmpegCommand(String command) {
		log.debug("debug>>executeFFmpegCommand->cmd=" + command);
		StringBuffer stdout = new StringBuffer();
		try {
			final Process process = Runtime.getRuntime().exec(command);
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					log.debug("debug>>executeCmd->ShutdownHook");
					process.destroy();
				}
			});

			InputStreamReader streamreader = new InputStreamReader(process.getErrorStream());

			char c = (char) streamreader.read();

			if (c != '\uFFFF') {
				stdout.append(c);
			}
			while (c != '\uFFFF') {
				if (!streamreader.ready()) {
					try {
						System.out.println(stdout);
						process.exitValue();
						break;
					} catch (IllegalThreadStateException _ex) {
						try {
							Thread.sleep(100L);
						} catch (InterruptedException _ex2) {
							log.error("RunCmd : Error closing InputStream " ,_ex2);
						}
					}
				} else {
					c = (char) streamreader.read();
					stdout.append(c);
				}
			}
			try {
				streamreader.close();
			} catch (IOException ioexception2) {
				log.error("RunCmd : Error closing InputStream " ,ioexception2);
				  throw new ApiException(ErrorCodeEnum.SystemError.getCode(),ioexception2.getMessage());
			}

		} catch (Throwable e) {
			e.printStackTrace();
			log.debug("发生错误：" + e);
		    throw new ApiException(ErrorCodeEnum.SystemError.getCode(),e.getMessage());
		}finally{
			log.info(stdout.toString());
		}
		return stdout;
	}
	/**
	 * 获取视频时常
	 * @param result
	 * @return
	 */
	public static String getVedioTimeLength(String result){
		 PatternCompiler compiler =new Perl5Compiler();  
		    try {  
		        String regexDuration ="Duration: (.*?), start: (.*?), bitrate: (\\d*) kb\\/s";  
		        Pattern patternDuration = compiler.compile(regexDuration,Perl5Compiler.CASE_INSENSITIVE_MASK);  
		        PatternMatcher matcherDuration = new Perl5Matcher();  
		        if(matcherDuration.contains(result, patternDuration)){  
		            MatchResult re = matcherDuration.getMatch();  
		            return re.group(1); 
		        }  
		    } catch (MalformedPatternException e) {  
		        e.printStackTrace();  
		    }  
		    return null;
	}
	
}
