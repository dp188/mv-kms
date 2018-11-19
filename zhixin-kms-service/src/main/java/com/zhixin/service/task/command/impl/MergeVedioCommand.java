package com.zhixin.service.task.command.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.zhixin.core.common.exceptions.ApiException;
import com.zhixin.core.enums.ErrorCodeEnum;
import com.zhixin.service.task.command.FFmpegHelper;
import com.zhixin.service.task.command.FMPCommand;
/**
 * 文件合并命令
 * 
 * @author zhangtiebin@bwcmall.com
 * @description
 * @class MergeVedioCommand
 * @package com.zhixin.service.task.command.impl
 * @Date 2016年1月16日 下午10:49:02
 */
public class MergeVedioCommand extends FMPCommand {
	//视频文件目录
	//private static final Logger log = LoggerFactory.getLogger(MergeVedioCommand.class);
	
	private String vedioFileDirs;

	public MergeVedioCommand(String vedioFileDirs) {
		super();
		this.vedioFileDirs = vedioFileDirs;
	}

    public static final int BUFSIZE = 1024 * 8;  
    
	@Override
	public String execute() throws ApiException {
		File result = null;
		try{
			File filedir = new File(vedioFileDirs);
			File wholeVedioFile = null;
			List<File> 	debrisFileList = new ArrayList<File>();
			List<File> 	mainFileList = new ArrayList<File>();
			 //1-1111-111.mp4
			 //11111111.mp4
			String roomid=vedioFileDirs.substring(vedioFileDirs.lastIndexOf("/")+1); 
			if (filedir.isDirectory()) {
		        File file[] = filedir.listFiles();
				for (int i = 0; i < file.length; i++) {
					
					String name=file[i].getName();
					if(name.indexOf(".mp4")==-1)
						continue;//只处理视频文件
					if(name.indexOf("Audio")!=-1)//音频文件
						continue;//只处理视频文件
					
					//获取主文件名，文件名含有文件夹名称的都是主文件
					if(name.indexOf("0_0_0")==0){
						//开始就是离线文件
						wholeVedioFile=file[i];
					}
					if(name.indexOf(roomid)!=-1)
					{
						mainFileList.add(file[i]);
					}
					else if(name.indexOf("_")!=-1)
					{
						debrisFileList.add(file[i]);
					}
			    } 
			}
			if(mainFileList.size() == 0&&wholeVedioFile==null){
				throw new ApiException(ErrorCodeEnum.SystemError.getCode(),"完整的视频文件找不到！");
			}
			else if(mainFileList.size()>0){
				Collections.sort(mainFileList);
				wholeVedioFile=mainFileList.get(0);//第一个在线文件作为主文件
				String timestart=wholeVedioFile.getName().substring(wholeVedioFile.getName().indexOf("_")+1);
				timestart=timestart.substring(0, timestart.length()-4);
				//计算偏移量，将主文件按时间戳加入碎片列表
				for(int i=1;i<mainFileList.size();i++){
					File file=mainFileList.get(i);
					String filetime=file.getName().substring(file.getName().indexOf("_")+1);
					filetime=filetime.substring(0, filetime.length()-4);
					long f1=longtimeStrToLong(filetime);
					long f2=longtimeStrToLong(timestart);
					long offset=f1-f2;
					filetime=longToStr(offset);
					filetime+=".mp4";
					File renameToIndexPathFile;  
					renameToIndexPathFile = new File(vedioFileDirs +File.separator+ filetime);   
					if(file.renameTo(renameToIndexPathFile)) 
						debrisFileList.add(renameToIndexPathFile);
				}
			}
			if(CollectionUtils.isEmpty(debrisFileList)){ //如果碎片文件为空，则不需要合并
				//如果碎片文件为空，则不需要合并
			}else{
				//对碎片文件排序
				Collections.sort(debrisFileList);
//				Collections.sort(debrisFileList,new Comparator<File>(){
//					@Override
//					public int compare(File s, File t) {
//						int sn = Integer.parseInt(s.getName().split("_")[0]);
//						int tn = Integer.parseInt(s.getName().split("_")[0]);
//						return sn-tn;
//					}
//				});
				//计算时间
				//分割： 时间偏移从0开始，0到第一个视频的时间偏移是一个，一个视频的结束到第二个的开始有是一个 。。 等等递归
				String start = "00_00_00.00"; //开始时间
				String debDeviation  = ""; //碎片文件时间偏移 
				long vedioLength = 0; //碎片文件长度
				long wholeLength = getVedioTimeLength(wholeVedioFile);
				List<File> allFileList = new ArrayList<File>();
				allFileList.add(wholeVedioFile);
				for(File file : debrisFileList){
					String time=file.getName().substring(0,file.getName().length()-4);
					debDeviation=time.replace('_', ':');
					vedioLength = getVedioTimeLength(file);
					//切割 start-->debDeviation  
					if(timeStrToLong(debDeviation)>=wholeLength)
					{
						allFileList.add(file);
					}
					else{
						//从start到debDeviation这里切一个文件出来
						if(timeStrToLong(start)<wholeLength){
							if(wholeLength<timeStrToLong(debDeviation))
								debDeviation=longToStr(wholeLength);
							allFileList.add(spiltVedio(wholeVedioFile, start,debDeviation ));
							//增加一个碎片文件
							allFileList.add(file);
							//start指针指向新地址
							start = longToStr(timeStrToLong(debDeviation) +vedioLength); 
						}
					}
				}
				if(timeStrToLong(start)>=wholeLength)
					allFileList.add(spiltVedio(wholeVedioFile, start,longToStr(getVedioTimeLength(wholeVedioFile)) ));
				result = this.mergeVideoList(allFileList); 
				if(result!=null){
//					String wholename=wholeVedioFile.getAbsolutePath();
//					String filename=result.getAbsolutePath();     
//					if (filename.indexOf(".") >= 0) {
//						filename = filename.substring(0, filename.lastIndexOf("."));
//					}

					File renameToIndexPathFile;  
					renameToIndexPathFile = new File(vedioFileDirs +File.separator+ roomid+".mp4");   
					result.renameTo(renameToIndexPathFile); // 改名
					//删除临时文件
					for(int j=0;j<allFileList.size();j++){
						File df=allFileList.get(j);
						String name=df.getName();
						if(name.indexOf(roomid)==-1&&name.indexOf("_")==-1&&name.indexOf(".mp4")!=-1)
							df.delete();
					}
				}
				//TODO 文件不删除
			}
		}
		catch(Exception e){
		    throw new ApiException(ErrorCodeEnum.SystemError.getCode(),e.getMessage());
		}
		if(result != null){
			return result.getAbsolutePath();
		}
		return null;
	}
	/**
	 * 获取视频的长度
	 * @param fileName
	 * @return
	 */
	private Long getVedioTimeLength(File file){
		StringBuffer sb = new StringBuffer();
		sb.append(getFfmpegInstallPath()+" -i ");
		sb.append(file.getAbsolutePath());
		
		StringBuffer timesb =FFmpegHelper.executeFFmpegCommand(sb.toString());
		
		String strTime = FFmpegHelper.getVedioTimeLength(timesb.toString());

		return timeStrToLong(strTime);
	}
	/**
	 * 时间00:00:36.53转毫秒
	 * @param timestr
	 * @return
	 */
	private Long timeStrToLong(String timestr){
		timestr=timestr.replace('_', ':');
		timestr=timestr.replace('.', ':');
		String[] array = timestr.split(":");
		long time = 0;
		int[] rate  = new int[]{60*60*1000,60*1000,1000,1};
		for(int i = 0;i<array.length;i++){
			time=time+ Integer.parseInt(array[i])*rate[i];
		}
		return time; 
	}
	
	private Long longtimeStrToLong(String timestr) throws ParseException{
		Date date = stringToDate(timestr, "yyyy-MM-dd_HH-mm-ss-SSS"); // String类型转成date类型
 		if (date == null) {
 			return (long) 0;
 		}
 		else {
 			return date.getTime();
 		}
	}
	
	public static Date stringToDate(String strTime, String formatType)
 			throws ParseException {
 		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
 		Date date = null;
 		date = formatter.parse(strTime);
 		return date;
 	}
	
	/**
	 * 毫秒转00:00:36.53
	 * @param time
	 * @return
	 */
	private String longToStr(Long time){ 
		StringBuffer sb = new StringBuffer();
		int[] rate  = new int[]{60*60*1000,60*1000,1000,1};
		for(int i = 0 ;i<rate.length;i++){
			if(i == 3){
				sb.append(".").append(time);
			}else{
				if(sb.length() != 0){
					sb.append("_");
				}
				sb.append(formateTime(time,rate[i]));
				time = time % rate[i];
			}
		}
		return sb.toString();
	}
	/**
	 * 时间格式化，计算时间，如果是1位，则加一个0到前面
	 * @param time
	 * @param rate
	 * @return
	 */
	private String formateTime(long time,int rate){
		String result = String.valueOf((int) (time/rate));
		if(result.length() == 1){
			result = "0"+result;
		}
		return result;
	}
	  
	/**
	 * 切割视频
	 * @param vedioFile
	 * @param start
	 * @param end
	 * @return
	 */
	private File spiltVedio(File vedioFile,String start,String end){
		start=start.replace('_', ':');
		end=end.replace('_', ':');
		String outfile = vedioFile.getParentFile().getAbsolutePath()+File.separator+UUID.randomUUID().toString()+".mp4";
		StringBuffer sb = new StringBuffer();
    	sb.append(getFfmpegInstallPath()+" -ss ");
    	sb.append(start);
    	sb.append(" -i ");
    	sb.append(vedioFile.getAbsolutePath());
    	sb.append(" -vcodec copy -acodec copy -t ");
    	end=longToStr(timeStrToLong(end)-timeStrToLong(start));
		end=end.replace('_', ':');
    	sb.append(end);
    	sb.append(" ");
    	sb.append(outfile.toString());
    	sb.append(" -y ");
    	String command=sb.toString();
    	
		FFmpegHelper.executeFFmpegCommand(command);
		
		return new File(outfile);
	}
	
	/**
	 * 拼接所有的视频
	 * @param fileList
	 * @return
	 */
	private File mergeVideoList(List<File> fileList){
		//所有视频拼接结果的视频
		String outfile = fileList.get(0).getParentFile().getAbsolutePath()+File.separator+UUID.randomUUID().toString()+".mp4";
		StringBuffer fb = new StringBuffer();
		String filename=outfile.substring(0,outfile.length()-4);
		String command="";
		for(Integer i=0;i<fileList.size();i++){
			String file1=fileList.get(i).getAbsolutePath();
			String filename1=filename+"_"+ i.toString()+".mpg";
			StringBuffer sb = new StringBuffer();
	    	sb.append(getFfmpegInstallPath()+"  -i \"");
	    	sb.append(file1);
	    	sb.append("\" -f mpeg -sameq -y -r 30 \""+filename1+"\" -y");
	    	command=sb.toString();
			FFmpegHelper.executeFFmpegCommand(command);
			fb.append(filename1);
			fb.append(",");
		}
		String[] xx=fb.toString().split(",");
		mergeFiles(filename+".mpg", xx);
		fb.setLength(0);
    	fb.append(getFfmpegInstallPath()+ "  -i "+filename+".mpg -f mp4 -y ");
    	fb.append(outfile);
    	command=fb.toString();
		FFmpegHelper.executeFFmpegCommand(command);
		for(Integer j=0;j<fileList.size()-1;j++){
			String name=filename+"_"+ j.toString()+".mpg";
			File file=new File(name);
			file.delete();
		}
		//fileList.get(0).delete();
		return new File(outfile);
	}
	 
	public void MergeVideo(String file1,String file2,String Outfile){
		String filename=Outfile.substring(0,Outfile.length()-4);
		StringBuffer sb = new StringBuffer();
    	sb.append(getFfmpegInstallPath()+"  -i \"");
    	sb.append(file1);
    	sb.append("\" -f mpeg -sameq -y -r 30 \""+filename+"_01.mpg\" -y");
    	String command=sb.toString();
		FFmpegHelper.executeFFmpegCommand(command);
		sb.setLength(0);
    	sb.append(getFfmpegInstallPath()+"  -i \"");
    	sb.append(file2);
    	sb.append("\" -f mpeg -sameq -y -r 30 \""+filename+"_02.mpg\"");
    	command=sb.toString();
		FFmpegHelper.executeFFmpegCommand(command);
		File file=new File(Outfile);
		file.delete();
		mergeFiles(filename+".mpg", new String[]{filename+"_01.mpg",filename+"_02.mpg"});
		sb.setLength(0);
    	sb.append(getFfmpegInstallPath()+ "  -i "+filename+".mpg -qscale 6 ");
    	sb.append(Outfile);
    	command=sb.toString();
		FFmpegHelper.executeFFmpegCommand(command);
		File file11=new File(filename+"_01.mpg");
		file11.delete();
		File file21=new File(filename+"_02.mpg");
		file21.delete();
		//File file41=new File(file2);
		//file41.delete();
		File file31=new File(filename+".mpg");
		file31.delete();
	}
	
	public static void mergeFiles(String outFile, String[] files) {
		FileChannel outChannel = null;
		try {
			outChannel = new FileOutputStream(outFile).getChannel();
			for (String f : files) {
				FileChannel fc = new FileInputStream(f).getChannel();
				ByteBuffer bb = ByteBuffer.allocate(BUFSIZE);
				while (fc.read(bb) != -1) {
					bb.flip();
					outChannel.write(bb);
					bb.clear();
				}
				fc.close();
			}
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} 
	}
}
