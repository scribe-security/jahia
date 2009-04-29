/**
 * Jahia Enterprise Edition v6
 *
 * Copyright (C) 2002-2009 Jahia Solutions Group. All rights reserved.
 *
 * Jahia delivers the first Open Source Web Content Integration Software by combining Enterprise Web Content Management
 * with Document Management and Portal features.
 *
 * The Jahia Enterprise Edition is delivered ON AN "AS IS" BASIS, WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED OR
 * IMPLIED.
 *
 * Jahia Enterprise Edition must be used in accordance with the terms contained in a separate license agreement between
 * you and Jahia (Jahia Sustainable Enterprise License - JSEL).
 *
 * If you are unsure which license is appropriate for your use, please contact the sales department at sales@jahia.com.
 */
package org.jahia.ajax.gwt.filemanagement.server;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.JCRStoreService;
import org.jahia.services.usermanager.JahiaUser;
import org.jahia.params.ParamBean;
import org.jahia.registries.ServicesRegistry;
import org.jahia.utils.i18n.JahiaResourceBundle;
import org.jahia.bin.Jahia;
import org.jahia.ajax.gwt.filemanagement.server.helper.FileManagerWorker;
import org.jahia.ajax.gwt.client.service.GWTJahiaServiceException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.jcr.RepositoryException;
import java.io.*;
import java.util.*;
import java.text.MessageFormat;

/**
 * Created by IntelliJ IDEA.
 *
 * @author rfelden
 * @version 2 avr. 2008 - 16:51:39
 */
public class GWTFileManagerUploadServlet extends HttpServlet {

    private static Logger logger = Logger.getLogger(GWTFileManagerUploadServlet.class) ;
    private JCRStoreService jcr = ServicesRegistry.getInstance().getJCRStoreService() ;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.debug("Entered GWT upload servlet") ;

        FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setSizeMax(Jahia.getSettings().getJahiaFileUploadMaxSize());
        upload.setHeaderEncoding("UTF-8");
        Map<String, FileItem> uploads = new HashMap<String, FileItem>() ;
        String location = null ;
        String type = null;
        boolean unzip = false ;

        try {
			List<FileItem> items = upload.parseRequest(request) ;
            for (FileItem item : items) {
                if ("unzip".equals(item.getFieldName())) {
                    unzip = true ;
                } else if ("uploadLocation".equals(item.getFieldName())) {
                    location = item.getString("UTF-8") ;
                } else if ("asyncupload".equals(item.getFieldName())) {
                    String name = item.getName() ;
                    if (name.trim().length() > 0) {
                        uploads.put(extractFileName(name), item) ;
                    }
                    type = "async";
                } else if (!item.isFormField() && item.getFieldName().startsWith("uploadedFile")) {
                    String name = item.getName() ;
                    if (name.trim().length() > 0) {
                        uploads.put(extractFileName(name), item) ;
                    }
                    type = "sync";
                }
            }
		} catch (FileUploadBase.SizeLimitExceededException e) {
            Locale locale = (Locale) request.getSession().getAttribute(ParamBean.SESSION_LOCALE);
            String locMsg = null ;
            try {
                ResourceBundle res = ResourceBundle.getBundle(JahiaResourceBundle.JAHIA_INTERNAL_RESOURCES, locale) ;
                locMsg = MessageFormat.format(res.getString("org.jahia.engines.filemanager.Filemanager_Engine.fileSizeError.label"),
                                              Jahia.getSettings().getJahiaFileUploadMaxSize()) ;
            } catch (Exception ex) {
                logger.debug("Error while using default engine resource bundle (" + JahiaResourceBundle.JAHIA_INTERNAL_RESOURCES + ") with locale " + locale, ex);
            }
            if (locMsg == null) {
                locMsg = "File upload exceeding limit of " + Jahia.getSettings().getJahiaFileUploadMaxSize() + " bytes" ;
            }
            logger.error(locMsg, e) ;
            response.getWriter().write(locMsg);
			return ;
        } catch (FileUploadException e) {
            logger.error("UPLOAD-ISSUE", e) ;
            response.getWriter().write("UPLOAD-ISSUE");
			return;
        }

        if (type == null || type.equals("sync")) {
            response.setContentType("text/plain");

            final JahiaUser user = (JahiaUser) request.getSession().getAttribute(ParamBean.SESSION_USER);

            boolean failed = false ;
            final List<String> pathsToUnzip = new ArrayList<String>() ;
            for (String filename: uploads.keySet()) {
                try  {
                    if (!writeToDisk(user, uploads.get(filename), location, filename)) {
                        logger.error("UPLOAD-FAILED: " + filename) ;
                        failed = true ;
                    } else {
                        if (unzip && filename.toLowerCase().endsWith(".zip")) {
                            pathsToUnzip.add(new StringBuilder(location).append("/").append(filename).toString()) ;
                        }
                    }
                }  catch (IOException e) {
                    logger.error("Upload failed for file " + filename, e) ;
                    failed = true ;
                }
            }

            if (failed) {
                response.getWriter().write("UPLOAD-FAILED");
            } else {
                // direct blocking unzip
                if (unzip && pathsToUnzip.size() > 0) {
                    try {
                        FileManagerWorker.unzip(pathsToUnzip, true, user);
                    } catch (GWTJahiaServiceException e) {
                        logger.error("Auto-unzipping failed", e);
                    }
                }
                // unzip archives in another thread (do not block/interrupt post response)
                /*new Thread() {
                    @Override
                    public void run() {
                        try {
                            FileManagerWorker.unzip(pathsToUnzip, true, user);
                        } catch (GWTJahiaServiceException e) {
                            logger.error("Auto-unzipping failed", e);
                        }
                    }
                }.start();*/
                logger.debug("UPLOAD-SUCCEEDED") ;
                response.getWriter().write("OK");
            }
        } else {
            response.setContentType("text/html");

            for (FileItem fileItem : uploads.values()) {
                response.getWriter().write("<html><body>");
                File f = File.createTempFile("upload", ".tmp");
                IOUtils.copy(fileItem.getInputStream(), new FileOutputStream(f));
                response.getWriter().write("<div id=\"uploaded\" key=\""+f.getName() + "\" name=\""+fileItem.getName()+"\"></div>\n");
                response.getWriter().write("</body></html>");
                asyncItems.put(f.getName(), new Item(fileItem.getContentType(), fileItem.getSize(), f));
            }
        }
	}

    private String extractFileName(String rawFileName) {
        if (rawFileName.indexOf("\\") >= 0) {
            return rawFileName.substring(rawFileName.lastIndexOf("\\")+1) ;
        } else if (rawFileName.indexOf("/") >= 0) {
            return rawFileName.substring(rawFileName.lastIndexOf("/")+1) ;
        } else {
            return rawFileName ;
        }
    }

    private boolean writeToDisk(JahiaUser user, FileItem item, String location, String filename) throws IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("item : " + item);
            logger.debug("destination : " + location);
            logger.debug("filename : " + filename);
            logger.debug("size : " + item.getSize()) ;
        }
        if (item == null || location == null || filename == null) {
            return false ;
        }



        JCRNodeWrapper locationFolder = jcr.getFileNode(location, user) ;

        Exception ex = locationFolder.getException() ;
        if (ex != null) {
            logger.error("Exception building the node", ex) ;
        }

        if (!locationFolder.isWriteable()) {
            logger.debug("destination is not writable for user " + user.getName()) ;
            return false ;
        }
        JCRNodeWrapper result ;
        try {
            InputStream is = item.getInputStream() ;
            result = locationFolder.uploadFile(filename, is, item.getContentType());
            is.close() ;
            locationFolder.save();
        } catch (RepositoryException e) {
            logger.error("exception ",e) ;
            return false;
        }
        return result.isValid() ;
    }

    private static Map<String, Item> asyncItems = new HashMap<String, Item>();

    public static class Item {
        public String contentType;
        public long length;
        public FileInputStream file;

        Item(String contentType, long length, final File file) throws FileNotFoundException {
            this.contentType = contentType;
            this.length = length;
            this.file = new FileInputStream(file) {
                @Override
                public void close() throws IOException {
                    super.close();
                    file.delete();
                }
            };
        }
    }

    public static Item getItem(String key) {
        return asyncItems.get(key);
    }
}
