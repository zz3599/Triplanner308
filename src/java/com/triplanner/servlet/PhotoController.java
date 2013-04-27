/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.triplanner.servlet;

import com.triplanner.entities.Photo;
import com.triplanner.entities.User;
import com.triplanner.model.PhotoDAO;
import com.triplanner.utils.Utils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author brook
 */
public class PhotoController {
    private static final String HOMEDIR = "Triplanner";
    private static final String ROOTDIR = "uploads";
    public static final int BUFFERSZ = 10240;
        
    private static String getRoot(HttpServletRequest request){
        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        String root = url.substring(0, url.indexOf(uri)) + "/" + HOMEDIR;
        return root;
    }
    public static void doUploadPost(HttpServletRequest request, HttpServletResponse response, ServletContext context)
            throws ServletException, IOException {   
        //String root = getRoot(request);
        User user = (User) request.getSession().getAttribute("user");
        int userid = user.id;
        String description = request.getParameter("description"); // Retrieves <input type="text" name="description">
        String tripdayidString = request.getParameter("tripdayid");
        String eventidString = request.getParameter("eventid");
        int tripid = ((Integer) request.getSession().getAttribute("tripid")).intValue();
        Integer tripdayid = Utils.isNullOrEmpty(tripdayidString) ? null : Integer.parseInt(tripdayidString);
        Integer eventid = Utils.isNullOrEmpty(eventidString) ? null : Integer.parseInt(eventidString);
        
        Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
        String originalName = getFilename(filePart);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String filename = uuid + originalName.substring(originalName.lastIndexOf("."));
        
        InputStream filecontent = filePart.getInputStream();
        InputStream is = null;
        OutputStream os = null;
        String dirPath = context.getRealPath(ROOTDIR + "/" + userid);
        File dir = new File(dirPath);
        dir.mkdirs();
        File file = new File(dir, filename);
        String url = "../" + ROOTDIR + "/" + userid + "/" + filename; //relative path to the picture url 
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
            if(is != null) is.close(); 
            if(os != null) os.close();
            Photo photo = PhotoDAO.uploadPhoto(url, userid, tripid, tripdayid, eventid, description, originalName);
            JSONObject o = new JSONObject();
            if(photo != null){
                o = photo.toJSON();
            }
            response.getWriter().println(o);
        }
    }

    public static void doPhotoGet (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {  
        String requestFor = request.getParameter("type").toLowerCase();
        int id = Integer.parseInt(request.getParameter("id"));
        List<Photo> photos = new ArrayList<Photo>();
        if(requestFor.contains("trip")){
            photos = PhotoDAO.getTripPhotos(id);
        } else if(requestFor.contains("tripday")){
            photos = PhotoDAO.getDayPhotos(id);
        } else if(requestFor.contains("event")){
            photos = PhotoDAO.getEventPhotos(id);
        }
        JSONArray a = new JSONArray();
        for(Photo photo : photos){
            a.put(photo.toJSON());
        }
        response.getWriter().println(a);
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