/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.triplanner.servlet;

import com.triplanner.entities.User;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author brook
 */
public class PhotoController {
    public static final int BUFFERSZ = 10240;
    
    public static void doUploadPost(HttpServletRequest request, HttpServletResponse response, ServletContext context)
            throws ServletException, IOException {
        //TODO: db add 
        //TODO: get tripid, tripdayid, eventid? 
        String description = request.getParameter("description"); // Retrieves <input type="text" name="description">
        Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
        String filename = getFilename(filePart);
        InputStream filecontent = filePart.getInputStream();
        User user = (User) request.getSession().getAttribute("user");
        int userid = user.id;
        InputStream is = null;
        OutputStream os = null;
        String dirPath = context.getRealPath("uploads/" + userid);
        File dir = new File(dirPath);
        dir.mkdirs();
        File file = new File(dir, filename);
        if(!file.exists()) file.createNewFile();
        try {
            is = new BufferedInputStream(filecontent, BUFFERSZ);
            os = new BufferedOutputStream(new FileOutputStream(file), BUFFERSZ);
            byte[] buffer = new byte[BUFFERSZ];
            for(int read = 0; (read = is.read(buffer)) > 0; ){
                os.write(buffer, 0, read);
            }
        }catch(Exception e){      
            e.printStackTrace();
        }
        finally{
            is.close(); os.close();
        }
    }

    private static String getFilename(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1); // MSIE fix.
            }
        }
        return null;
    }
}
