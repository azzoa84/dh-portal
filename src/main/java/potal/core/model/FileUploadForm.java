/**
	@file	FileUploadForm.java
	@brief	
*/
package potal.core.model;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * @author Administrator
 * 
 */
public class FileUploadForm
{	
	private List<MultipartFile> files = null;
	
	public List<MultipartFile> getFiles()
	{
		return files;
	}
	
	public void setFiles(List<MultipartFile> files)
	{
		this.files = files;
	}	
}