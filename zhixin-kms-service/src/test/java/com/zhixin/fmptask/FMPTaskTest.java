package com.zhixin.fmptask;

import java.io.File;

import org.junit.Test;

import com.zhixin.service.task.command.FMPCommand;
import com.zhixin.service.task.command.impl.AddSubtitlesCommand;
import com.zhixin.service.task.command.impl.FileBackupCommand;

public class FMPTaskTest {
	
	/**
	 * 
	 */
	@Test
	public void testTask(){ 
		File vedioFile = new File("E:/dolphin/test/0_0_1.29.mp4");
		File subtitlesFile = new File("E:/dolphin/test/3721929462628220928.txt");
		FMPCommand command = new AddSubtitlesCommand(vedioFile,subtitlesFile);
		
		command.setFfmpegInstallPath("E:/dolphin/ffmpeg/ffmpeg.exe");
		
		command.execute();
	}
	
	@Test
	public void testFileBackUp(){ 
		FMPCommand command = new FileBackupCommand("E:/dolphin/"); 
		command.execute();
	}
}
