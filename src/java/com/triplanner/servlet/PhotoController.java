/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.triplanner.servlet;

import com.triplanner.entities.Photo;
import com.triplanner.entities.User;
import com.triplanner.model.PhotoDAO;
import com.triplanner.utils.Utils;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
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
import javax.imageio.ImageIO;
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
    //for resizing
    private static final int IMG_WIDTH = 550;
    private static final int IMG_HEIGHT = 550;

    private static String getRoot(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        String root = url.substring(0, url.indexOf(uri)) + "/" + HOMEDIR;
        return root;
    }

    public static void doUploadPost(HttpServletRequest request, HttpServletResponse response, ServletContext context)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        JSONObject o = new JSONObject();
        User user = (User) request.getSession().getAttribute("user");
        int userid = user.id;
        int tripid = (Integer) request.getSession().getAttribute("tripid");     
        Integer tripdayid = null; //both fields are null if we upload a photo with only trip granularity
        Integer eventid = null;
        if(action.equals("day")){
            String tripdayidString = request.getParameter("tripdayid");
            tripdayid = Utils.isNullOrEmpty(tripdayidString) ? null : Integer.parseInt(tripdayidString);
        } else if(action.equals("event")){
            String tripdayidString = request.getParameter("tripdayid");
            tripdayid = Utils.isNullOrEmpty(tripdayidString) ? null : Integer.parseInt(tripdayidString);
            String eventidString = request.getParameter("eventid");
            eventid = Utils.isNullOrEmpty(eventidString) ? null : Integer.parseInt(eventidString);
        }
        String description = request.getParameter("description"); // Retrieves <input type="text" name="description">
        Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
        String originalName = getFilename(filePart);
        String extension = originalName.substring(originalName.lastIndexOf(".") + 1);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String filename = uuid + "." + extension;

        InputStream filecontent = filePart.getInputStream();
        BufferedImage resizedImage = null;
        try {
            resizedImage = resizeImage(filecontent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        InputStream is = null;
        OutputStream os = null;
        String dirPath = context.getRealPath(ROOTDIR + "/" + userid);
        if (resizedImage == null) {
            response.getWriter().println(o);
            return;
        }
        File dir = new File(dirPath);
        dir.mkdirs();
        File file = new File(dir, filename);
        String url = "../" + ROOTDIR + "/" + userid + "/" + filename; //relative path to the picture url 
        if (!file.exists()) {
            file.createNewFile();
        }
        try {
            ImageIO.write(resizedImage, extension, file);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
            if (os != null) {;
                os.close();
            }
            
            Photo photo = PhotoDAO.uploadPhoto(url, userid, tripid, tripdayid, eventid, description, originalName);

            if (photo != null) {
                o = photo.toJSON();
            }
            response.setContentType("application/json");    
            response.getWriter().println(o);
        }
    }

    public static void doPhotoGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String requestFor = request.getParameter("action").toLowerCase();
        int id = Integer.parseInt(request.getParameter("id"));
        List<Photo> photos = new ArrayList<Photo>();
        if (requestFor.contains("trip")) {
            photos = PhotoDAO.getTripPhotos(id);
        } else if (requestFor.contains("day")) {
            photos = PhotoDAO.getDayPhotos(id);
        } else if (requestFor.contains("event")) {
            photos = PhotoDAO.getEventPhotos(id);
        }
        JSONArray a = new JSONArray();
        for (Photo photo : photos) {
            a.put(photo.toJSON());
        }
        response.setContentType("application/json");
        response.getWriter().println(a);
    }

    /**
     * Will preserve proportions, but we will limit the size of the image
     *
     * @param original
     * @param type
     * @return
     */
    private static BufferedImage resizeImage(InputStream original) throws Exception {
        BufferedImage originalImage = ImageIO.read(original);
        int resizedW = IMG_WIDTH;
        int resizedH = IMG_HEIGHT;
        int w = originalImage.getWidth();
        int h = originalImage.getHeight();
        double originalRatio = (double) w / (double) h;
        double desiredRatio = (double) IMG_WIDTH / (double) IMG_HEIGHT;
        if (originalRatio > desiredRatio) {
            resizedH = (int) (resizedH / originalRatio);
        } else {
            resizedW = (int) (resizedW * originalRatio);
        }
        BufferedImage resizedImage = new BufferedImage(resizedW, resizedH, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(originalImage, 0, 0, resizedW, resizedH, null);
        return resizedImage;
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
