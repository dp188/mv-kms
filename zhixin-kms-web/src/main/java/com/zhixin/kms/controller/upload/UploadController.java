package com.zhixin.kms.controller.upload;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zhixin.core.enums.ErrorCodeEnum;
import com.zhixin.core.utils.PropertyConfigurer;
import com.zhixin.core.utils.Response;
import com.zhixin.core.utils.ResponseBuilder;
import com.zhixin.dto.kms.KMSRoomDTO;
import com.zhixin.service.kms.IKMSOffLineFileService;
import com.zhixin.service.kms.IKMSRoomService;
import com.zhixin.service.task.impl.FFmpegTaskDispatch;

@Controller
@RequestMapping("/")
public class UploadController {
	/**
	 * 文件上传
	 */
	@Autowired
	IKMSOffLineFileService kmsOffLineFileService;
	@Autowired 
	IKMSRoomService kmsRoomService;
	

	@Autowired
	private PropertyConfigurer propertyConfigurer;
	
//	@Value("${BaseFilePath}")
//	private String BaseFilePath;

	
	@RequestMapping(value = "fileupload")
	@ResponseBody
	public Response upload( HttpServletRequest request,ModelMap model,
			@RequestParam(value = "videoFile", required = false) MultipartFile file,
			@RequestParam ("roomId")String roomId,
			@RequestParam ("Finished")String Finished) {
		try {
			System.out.println("开始");
			String uploadPath;
			String roomname;
			KMSRoomDTO kmsRoomDTO=kmsRoomService.find(Long.parseLong(roomId));
			if (kmsRoomDTO!=null) {
				uploadPath=kmsRoomDTO.getLivePath()+roomId+File.separator; //房间文件夹
				roomname=kmsRoomDTO.getName();
			}
			else
			{
				uploadPath=propertyConfigurer.getProperty("BaseFilePath") +roomId;
				roomname=roomId;
			}
			
			File file2 = new File(uploadPath);
			if(!file2.exists()){
				file2.mkdirs();
			}
			file2.setReadable(true, false);
			file2.setWritable(true, false);
			
			String fileName = file.getOriginalFilename();
			System.out.println(uploadPath);
			File targetFile = new File(uploadPath, fileName);
			file.transferTo(targetFile);//写文件
			/*KMSOffLineFileDTO kmsOffLineFileDTO=new KMSOffLineFileDTO();
			kmsOffLineFileDTO.setKey(roomId);
			kmsOffLineFileDTO.setFilename(fileName);
			kmsOffLineFileService.create(kmsOffLineFileDTO);*/
			
			//uploadPath="D://";
			///如果文件传输完成，调用处理接口
			if (Finished.equals("true")) {
				FFmpegTaskDispatch.addTask(roomname, uploadPath);
			}
			
		} catch (Exception e) {
			e.printStackTrace();	
			return ResponseBuilder.buildFaildResponse(ErrorCodeEnum.FileUploadError);
		}
		return ResponseBuilder.buildSuccessResponse();
	}	
}

