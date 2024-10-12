package com.emrheathgroup.backend.Configuration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

public class BufferedServletResponseWrapper extends HttpServletResponseWrapper {

	private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	private PrintWriter writer = new PrintWriter(outputStream);

	public BufferedServletResponseWrapper(HttpServletResponse response) throws IOException {
		super(response);
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return new ServletOutputStream() {
			@Override
			public void write(int b) throws IOException {
				outputStream.write(b);
			}

			@Override
			public void write(byte[] b) throws IOException {
				outputStream.write(b);
			}

			@Override
			public boolean isReady() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void setWriteListener(WriteListener listener) {
				// TODO Auto-generated method stub

			}
		};
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		return writer;
	}

	@Override
	public void flushBuffer() throws IOException {
		if (writer != null) {
			writer.flush();
		} else if (outputStream != null) {
			outputStream.flush();
		}
	}

	public String getResponseData() {
		return outputStream.toString();
	}

}
