package com.anil.turan.exchange.api.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class ApiExceptionHandler {

	
	@ExceptionHandler({ApiRequestException.class})
	public ResponseEntity<Object> handleApiRequestException(ApiRequestException ex) {     
        List<String> details = new ArrayList<String>();
        details.add(ex.getMessage());       
        ApiError err = new ApiError(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST, 
            "" ,
            details);
        
        return ResponseEntityBuilder.build(err);
    }
	
	@ExceptionHandler({ MethodArgumentTypeMismatchException.class })
	public ResponseEntity<Object> handleMethodArgumentTypeMismatch3( MethodArgumentTypeMismatchException ex,  WebRequest request) {          
		List<String> details = new ArrayList<String>();
        details.add(ex.getMessage());        
        ApiError err = new ApiError(
        LocalDateTime.now(),
        HttpStatus.BAD_REQUEST, 
        "Type Mismatch" ,
        details);         
       return ResponseEntityBuilder.build(err);
   }
	
	@ExceptionHandler({ NoSuchElementException.class })
	public ResponseEntity<Object> noSuchElementException(NoSuchElementException ex){
		 return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler({ HttpClientErrorException.class })
	public ResponseEntity<Object> noSuchElementException(HttpClientErrorException ex){
		 return new ResponseEntity<Object>(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {      
        List<String> details = new ArrayList<String>();
        details.add(ex.getLocalizedMessage());       
        ApiError err = new ApiError(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST, 
            "Error occurred" ,
            details);
        
        return ResponseEntityBuilder.build(err);
    }
	
	@ExceptionHandler({MissingServletRequestParameterException.class})
	public ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, WebRequest request) {        
        List<String> details = new ArrayList<String>();
        details.add(ex.getParameterName() + " parameter is missing");
        ApiError err = new ApiError(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST, 
            "Missing Parameters" ,
            details);      
        return ResponseEntityBuilder.build(err);
    }
	
	@ExceptionHandler({HttpMediaTypeNotSupportedException.class})
	public ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,HttpHeaders headers,HttpStatus status,WebRequest request) {       
        List<String> details = new ArrayList<String>();
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
        
        details.add(builder.toString());
        ApiError err = new ApiError(
            LocalDateTime.now(), 
            HttpStatus.UNSUPPORTED_MEDIA_TYPE, 
            "Unsupported Media Type" ,
            details);
        
        return ResponseEntityBuilder.build(err);
    }
	
	@ExceptionHandler({HttpMessageNotReadableException.class})
	 public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status,  WebRequest request) {       
         List<String> details = new ArrayList<String>();
         details.add(ex.getMessage());
         
         ApiError err = new ApiError(
             LocalDateTime.now(),
             HttpStatus.BAD_REQUEST, 
             "Malformed JSON request" ,
             details);
         
         return ResponseEntityBuilder.build(err);
     }
	
	@ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
	public ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, WebRequest request) {	 
		List<String> details = new ArrayList<String>();
        details.add(ex.getMessage());
        
        ApiError err = new ApiError(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST, 
            "Method Not Supported" ,
            details);
        
        return ResponseEntityBuilder.build(err);
	}
	
	@ExceptionHandler({ NoHandlerFoundException.class })
	public ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> details = new ArrayList<String>();
        details.add(String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()));
        
        ApiError err = new ApiError(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND, 
            "Method Not Found" ,
            details);
        
        return ResponseEntityBuilder.build(err);
        
    }
	
	@ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(Exception ex,  WebRequest request) {      
        List<String> details = new ArrayList<String>();
        details.add(ex.getMessage());       
        ApiError err = new ApiError(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST, 
            "Constraint Violations" ,
            details);
        
        return ResponseEntityBuilder.build(err);
    }
	 
	@ExceptionHandler({ MethodArgumentNotValidException.class })
	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,  HttpStatus status,  WebRequest request) {        
        List<String> details = new ArrayList<String>();
        details = ex.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(error -> error.getObjectName()+ " : " +error.getDefaultMessage())
                    .collect(Collectors.toList());
        
        ApiError err = new ApiError(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST, 
            "Validation Errors" ,
            details);
        
        return ResponseEntityBuilder.build(err);
    }
}
