package potal.core.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class ExceptionController {
	Logger log = LoggerFactory.getLogger(this.getClass());

	@ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
    @ResponseBody
    protected void handleMaxUploadSizeExceededException(final HttpServletRequest request,
            final HttpServletResponse response, final Throwable e)
            throws IOException
    {
		System.out.println("-------------------------> MaxUploadSizeExceededException");
        System.out.println(e); 
    }

    @ExceptionHandler(MultipartException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    protected void handleGenericMultipartException(final HttpServletRequest request,
            final HttpServletResponse response, final Throwable e)
            throws IOException
    {
    	System.out.println("MultipartException");
    	System.out.println(e);
    }
    
    @ExceptionHandler(Exception.class)
    public void handleException(Exception e) {
        System.out.println("exception");
    }
}
