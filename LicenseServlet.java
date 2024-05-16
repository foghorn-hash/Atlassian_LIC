package fi.i4ware;

import java.io.IOException;
import java.net.URI;
import java.util.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atlassian.upm.api.license.entity.PluginLicense;
import com.atlassian.upm.api.license.PluginLicenseManager;
import com.atlassian.upm.api.license.entity.Organization;

import com.atlassian.plugin.webresource.WebResourceManager;
import com.atlassian.templaterenderer.TemplateRenderer;

import org.apache.commons.lang.StringUtils;

public class LicenseServlet extends HttpServlet
{
	private final PluginLicenseManager licenseManager;
	private WebResourceManager webResourceManager;
	private static final String TEMPLATE = "license.vm";
	private final TemplateRenderer renderer;
    private Organization organization;

    public LicenseServlet(PluginLicenseManager licenseManager,
    					WebResourceManager webResourceManager,
    					TemplateRenderer renderer)
    {
        this.licenseManager = licenseManager;
        this.webResourceManager = webResourceManager;
        this.renderer = renderer;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        resp.setContentType("application/json");
        
        final Map<String, Object> context = initVelocityContext(resp);
        
        //Check and see if a license is currently stored.
        //This accessor method can be used whether or not a licensing-aware UPM is present.
        if (licenseManager.getLicense().isDefined())
        {
            PluginLicense pluginLicense = licenseManager.getLicense().get();
            //Check and see if the stored license has an error. If not, it is currently valid.
            if (pluginLicense.getError().isDefined())
            {
                //A license is currently stored, however, it is invalid (e.g. expired or user count mismatch)
                context.put("licenseMsg", "Your license has an error: " + pluginLicense.getError().get().name() + ".");
                context.put("success","false");
            }
            else
            {
                //A license is currently stored and it is valid.
                organization = pluginLicense.getOrganization();
                String org = organization.getName();
                context.put("licenseMsg", "License for " + org + " is valid.");
                context.put("success","true");
            }
        }
        else
        {
            //No license (valid or invalid) is stored.
            context.put("licenseMsg", "You don't have a license.");
            context.put("success","false");
        }   
        
        renderer.render(TEMPLATE, context, resp.getWriter()); 
         
        resp.getWriter().close();
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        resp.setContentType("application/json");
        
        final Map<String, Object> context = initVelocityContext(resp);
        
        //Check and see if a license is currently stored.
        //This accessor method can be used whether or not a licensing-aware UPM is present.
        if (licenseManager.getLicense().isDefined())
        {
            PluginLicense pluginLicense = licenseManager.getLicense().get();
            //Check and see if the stored license has an error. If not, it is currently valid.
            if (pluginLicense.getError().isDefined())
            {
                //A license is currently stored, however, it is invalid (e.g. expired or user count mismatch)
                context.put("licenseMsg", "Your license has an error: " + pluginLicense.getError().get().name() + ".");
                context.put("success","false");
            }
            else
            {
                //A license is currently stored and it is valid.
                organization = pluginLicense.getOrganization();
                String org = organization.getName();
                context.put("licenseMsg", "License for " + org + " is valid.");
                context.put("success","true");
            }
        }
        else
        {
            //No license (valid or invalid) is stored.
            context.put("licenseMsg", "You don't have a license.");
            context.put("success","false");
        }
     
        renderer.render(TEMPLATE, context, resp.getWriter()); 
         
        resp.getWriter().close();
    }
    
    private Map<String, Object> initVelocityContext(HttpServletResponse resp)
    {

        final Map<String, Object> context = new HashMap<String, Object>();

        resp.setContentType("application/json;charset=utf-8");

        return context;
    }
}
