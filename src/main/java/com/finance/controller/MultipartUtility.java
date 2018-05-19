package com.finance.controller;

/**
 * Created by zt
 * 2018/4/21 14:31
 */

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

public class MultipartUtility {

    private static final String BOUNDARY = "*****";
    private static final String CRLF = "\r\n";
    private static final String TWO_HYPHENS = "--";
    private HttpURLConnection httpConn;
    private DataOutputStream request;

    /**
     * This constructor initializes a new HTTP POST request with content type
     * is set to multipart/form-data
     */
    public MultipartUtility(String requestURL) throws IOException {
        // creates a unique BOUNDARY based on time stamp
        URL url = new URL(requestURL);
        httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true); // indicates POST method
        httpConn.setDoInput(true);
        httpConn.setRequestMethod("POST");
        httpConn.setRequestProperty("Connection", "Keep-Alive");
        httpConn.setRequestProperty("Cache-Control", "no-cache");
        httpConn.setRequestProperty(
                "Content-Type", "multipart/form-data;BOUNDARY=" + BOUNDARY);
        request = new DataOutputStream(httpConn.getOutputStream());
    }

    /**
     * Adds a form field to the request
     *
     * @param name  field name
     * @param value field value
     */
    public void addFormField(String name, String value) throws IOException {
        request.writeBytes(TWO_HYPHENS + BOUNDARY + CRLF);
        request.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"" + CRLF);
        request.writeBytes("Content-Type: text/plain; charset=UTF-8" + CRLF);
        request.writeBytes(CRLF);
        request.writeBytes(value + CRLF);
        request.flush();
    }

    /**
     * Adds a upload file section to the request
     *
     * @param fieldName  name attribute in <input type="file" name="..." />
     * @param uploadFile a File to be uploaded
     */
    public void addFilePart(String fieldName, File uploadFile)
            throws IOException {
        String fileName = uploadFile.getName();
        request.writeBytes(TWO_HYPHENS + BOUNDARY + CRLF);
        request.writeBytes("Content-Disposition: form-data; name=\"" +
                fieldName + "\";filename=\"" +
                fileName + "\"" + CRLF);
        request.writeBytes(CRLF);
        byte[] bytes = Files.readAllBytes(uploadFile.toPath());
        request.write(bytes);
    }

    public void addFilePart(String fieldName, String fileName, InputStream inputStream)
            throws IOException {
        request.writeBytes(TWO_HYPHENS + BOUNDARY + CRLF);
        request.writeBytes("Content-Disposition: form-data; name=\"" +
                fieldName + "\";filename=\"" +
                fileName + "\"" + CRLF);
        request.writeBytes(CRLF);
        byte[] bytes = IOUtils.toByteArray(inputStream);
        request.write(bytes);
    }

    /**
     * Completes the request and receives response from the server.
     *
     * @return a list of Strings as response in case the server returned
     * status OK, otherwise an exception is thrown.
     */
    public String finish() throws IOException {
        String response = "";
        request.writeBytes(CRLF);
        request.writeBytes(TWO_HYPHENS + BOUNDARY +
                TWO_HYPHENS + CRLF);
        request.flush();
        request.close();
        // checks server's status code first
        int status = httpConn.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            InputStream responseStream = new
                    BufferedInputStream(httpConn.getInputStream());
            BufferedReader responseStreamReader =
                    new BufferedReader(new InputStreamReader(responseStream));
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            responseStreamReader.close();
            response = stringBuilder.toString();
            httpConn.disconnect();
        } else {
            throw new IOException("Server returned non-OK status: " + status);
        }
        return response;
    }

}