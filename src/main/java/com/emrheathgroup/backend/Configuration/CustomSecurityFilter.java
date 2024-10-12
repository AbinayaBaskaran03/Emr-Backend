package com.emrheathgroup.backend.Configuration;

import java.io.IOException;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.emrheathgroup.backend.Service.Security.SecurityService;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(1)
public class CustomSecurityFilter implements Filter {

	Logger logger = LoggerFactory.getLogger(getClass());

	private String enableResponseEncrKey = "enableResponseEncr";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		BufferedServletResponseWrapper bufferedResponse = new BufferedServletResponseWrapper(httpServletResponse);

		chain.doFilter(httpServletRequest, bufferedResponse);

		String responseData = bufferedResponse.getResponseData();
		logger.debug("plainResponse: " + responseData);

		String value = SecurityService.getItemKeyValue(enableResponseEncrKey);
		System.out.println("enableResponseEncrKey: " + value);
		Boolean isEncrptEnabled = "yes".equalsIgnoreCase(value);
		String encryptedResponseData = isEncrptEnabled ? EncryptDecrypt.encrypt(responseData) : responseData;
		logger.debug("encrypt: " + encryptedResponseData);

//		String decryptedStr = EncryptDecrypt.decrypt(encryptedResponseData);
//		logger.debug("decrypt: " + temp);

		OutputStream outputStream = httpServletResponse.getOutputStream();
		outputStream.write(encryptedResponseData.getBytes());
		outputStream.flush();
		outputStream.close();
	}

}
