package cn.offway.hades.controller;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/file")
public class FileController {

	
	@Value("${ph.file.path}")
	private String FILE_PATH;
	
	@Value("${ph.file.server}")
	private String FILE_SERVER;
	
	@ResponseBody
	@RequestMapping("/upload")
	public Map<String, Object> upload(MultipartFile file,String param) {

		try {
			String path = FILE_PATH;
			File uploadPath = new File(path);
			if (!uploadPath.exists()) {
				uploadPath.mkdirs();
			}
			String oldFileName = file.getOriginalFilename();
			// 扩展名
			String suffix = oldFileName.substring(oldFileName.lastIndexOf(".") + 1);
			// 新文件名
			String newFileName = UUID.randomUUID() + "." + suffix;
			File uploadFile = new File(path, newFileName);
			file.transferTo(uploadFile);
			Map<String, Object> map = new HashMap<>();
			map.put("param", param);
			map.put("key", FILE_SERVER+"/"+newFileName);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@RequestMapping("/download")
	public ResponseEntity<byte[]> download(String fileName, HttpServletRequest request) throws Exception {

		String saveFilePath = "D:/file/"+fileName;
		File file = new File(saveFilePath);

		HttpHeaders headers = new HttpHeaders();
		fileName = encode(request, fileName);
		headers.setContentDispositionFormData("attachment", fileName);
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return new ResponseEntity<byte[]>(FileUtil.readAsByteArray(file), headers, HttpStatus.OK);
	}

	/**
	 * 处理下载中文名乱码
	 * 
	 * @param request
	 * @param fileName
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String encode(HttpServletRequest request, String fileName) throws UnsupportedEncodingException {
		// 为了解决中文名称乱码问题
		String agent = request.getHeader("USER-AGENT");
		if (null != agent && -1 != agent.indexOf("MSIE") || null != agent && -1 != agent.indexOf("Trident")) {
			// IE浏览器
			String name = java.net.URLEncoder.encode(fileName, "UTF8");
			fileName = name;
		} else {
			fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
		}
		return fileName;
	}
}