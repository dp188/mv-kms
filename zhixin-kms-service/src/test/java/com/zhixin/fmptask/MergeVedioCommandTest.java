package com.zhixin.fmptask;

import org.junit.Test;

import com.zhixin.service.task.command.impl.MergeVedioCommand;

public class MergeVedioCommandTest {
	
	private MergeVedioCommand mvc=null;
	@Test
	public void testExecute() throws Exception {
		mvc=new MergeVedioCommand("C:/videotest/3723988544289636352");
		mvc.setFfmpegInstallPath("C:\\zhixintec\\command\\download\\videochange\\ffmpeg\\ffmpeg.exe");
		mvc.execute();
	}


}
