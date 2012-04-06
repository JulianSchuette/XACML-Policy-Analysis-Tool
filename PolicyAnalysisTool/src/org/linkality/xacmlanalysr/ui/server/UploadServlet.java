package org.linkality.xacmlanalysr.ui.server;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * A servlet handling file uploads.
 * 
 * @author Julian Schuette
 *
 */
public class UploadServlet extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);

		// Create a factory for disk-based file items
		System.out.println("getting a file");
		if (isMultipart) {
			System.out.println("Is multipart");
			FileItemFactory factory = new DiskFileItemFactory();

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);

			// Parse the request
			try {
				List items = upload.parseRequest(request);
				// Process the uploaded items
				Iterator iter = items.iterator();
				while (iter.hasNext()) {
					FileItem item = (FileItem) iter.next();

					if (item.isFormField()) {

					} else {
						File file = new File("mypolicy.xacml");
						System.out.println("Writing to " + file.getAbsolutePath());
						item.write(file);
						out.write("OK");
					}
				}
			} catch (FileUploadException e) {
				e.printStackTrace();
			} catch (Exception e2) {
				e2.printStackTrace();
			}

		} else {
			System.out.println("isMultipartContent = false");
		}
	}
}
