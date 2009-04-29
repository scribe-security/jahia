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
//                       __/\ ______|    |__/\.     _______
//            __   .____|    |       \   |    +----+       \
//    _______|  /--|    |    |    -   \  _    |    :    -   \_________
//   \\______: :---|    :    :           |    :    |         \________>
//           |__\---\_____________:______:    :____|____:_____\
//                                      /_____|
//
//                 . . . i n   j a h i a   w e   t r u s t . . .
//
//
//
//
//  NK      18.06.2002
//
//

package org.jahia.data.applications;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jahia.services.applications.ServletIncludeRequestWrapper;
import org.jahia.utils.InsertionSortedMap;
import org.jahia.utils.JahiaConsole;



/**
 * Holds information for a given web application context.
 * Some of them are: servlets, roles, servlet mapping, welcome files.
 *
 * @author Khue Nguyen <a href="mailto:khue@jahia.com">khue@jahia.com</a>
 */
public class WebAppContext implements Serializable
{
    private static final long serialVersionUID = 5206818081114734385L;

    private static final String CLASS_NAME = WebAppContext.class.getName();

    /** the application display name **/
    private String displayName = "";

    /** the application context **/
    private String context = "";

    /** the application description **/
    private String descr = "";

    /** The Map of servlet bean , keyed by the servlet name **/
    private InsertionSortedMap<String, ServletBean> servlets = new InsertionSortedMap<String, ServletBean>();

    /**
     * The hashMap of servlet mapping, keyed with the pattern used to map a servlet.
     * The value is the servlet name.
     **/
    private Map<String, String> servletMappings = new HashMap<String, String>();

    /** List of security roles **/
    private List<String> roles = new ArrayList<String>();

    /** The list of Welcome files **/
    private List<String> welcomeFiles = new ArrayList<String>();

    // Entry points into servlet-based portlet web application.
    private List<EntryPointDefinition> entryPoints = new ArrayList<EntryPointDefinition>();



    //--------------------------------------------------------------------------
    /**
     * Constructor
     *
     */
    public WebAppContext(String context){
        this.context = context;
    }

    //--------------------------------------------------------------------------
    /**
     * Constructor
     *
     * @param context , the application context
     * @param displayName , the application display name
     * @param descr, the application description
     * @param servlets, a List ServletBean
     * @param servletMappings, a map of servletMappings keyed with the url pappern and value = the servlet name
     * @param welcomeFiles, a List of welcome files (String)
     */
    public WebAppContext( 	String context,
                                String displayName,
                                String descr,
                                List<ServletBean> servlets,
                                Map<String, String> servletMappings,
                                List<String> roles,
                                List<String> welcomeFiles )
    {
        this.context = context;

        if ( displayName != null ){
            this.displayName = displayName;
        }
        if ( descr != null ){
            this.descr = descr;
        }
        addServlets(servlets);
        setServletMappings(servletMappings);
        setRoles(roles);
        setWelcomeFiles(welcomeFiles);
    }

    //--------------------------------------------------------------------------
    /**
     * Add a List of ServletBean.
     *
     * @param servlets
     */
    public void addServlets(List<ServletBean> servlets) {
        synchronized (this.servlets) {
            if ( servlets!=null ){
                int size = servlets.size();
                ServletBean servlet = null;
                for ( int i=0 ; i<size ; i++ ){
                    servlet = (ServletBean)servlets.get(i);
                    if ( servlet!=null && servlet.getServletName() != null ){
                        this.servlets.put(servlet.getServletName(), servlet);
                    }
                }
            }
        }
    }

    //--------------------------------------------------------------------------
    /**
     * Add a new servlet bean.
     *
     * @param servlet
     */
    public void addServlet(ServletBean servlet) {
        synchronized (servlets) {
            if ( servlet!=null && servlet.getServletName() != null ){
                servlets.put(servlet.getServletName(), servlet);
            }
        }
    }

    //--------------------------------------------------------------------------
    /**
     * Get a servlet looking at it name.
     *
     * @param name , the servlet name
     */
    public ServletBean getServlet(String name) {
        synchronized (servlets) {
            if ( name!=null){
                return servlets.get(name);
            }
        }
        return null;
    }

    //--------------------------------------------------------------------------
    /**
     * Set the map of servlet mapping.
     *
     * @param servletMappings
     */
    public void setServletMappings(Map<String, String> servletMappings) {
        synchronized (this.servletMappings) {
            if ( servletMappings != null ){
                this.servletMappings = servletMappings;
            }
        }
    }

    //--------------------------------------------------------------------------
    /**
     * Add a new servlet mapping and replace old mapping with same pattern.
     *
     * @param pattern
     * @param name , the servlet name
     */
    public void addServletMapping(String pattern, String name) {
        synchronized (servletMappings) {
            servletMappings.put(pattern, name);
        }
    }

    //--------------------------------------------------------------------------
    /**
     * Return the servlet name mapped by the given pattern
     * otherwise return null.
     *
     * @param pattern the url-pattern
     */
    public String findServletMapping(String pattern) {
        synchronized (servletMappings) {
            return servletMappings.get(pattern);
        }
    }

    //--------------------------------------------------------------------------
    /**
     * Return the List of servlets.
     *
     * @return servlets
     */
    public Map<String, ServletBean> getServlets() {
        return servlets;
    }


    //--------------------------------------------------------------------------
    /**
     * Set roles.
     *
     * @param roles a List of security roles
     */
    public void setRoles(List<String> roles) {
        synchronized (this.roles) {
            if ( roles != null ){
                this.roles = roles;
            }
        }
    }

    //--------------------------------------------------------------------------
    /**
     * Add a new security role.
     *
     * @param role New security role
     */
    public void addRole(String role) {
        synchronized (roles) {
            roles.add(role);
        }
    }

    //--------------------------------------------------------------------------
    /**
     * Return true if a given role is already in the list.
     *
     * @param role the security role to look for
     */
    public boolean findRole(String role) {
        if ( role == null ){
            return false;
        }
        synchronized (roles) {
            return roles.contains(role);
        }
    }

    //--------------------------------------------------------------------------
    /**
     * Return the security roles defined for this application.
     */
    public List<String> getRoles() {
        return roles;
    }

    //--------------------------------------------------------------------------
    /**
     * Set the welcome files.
     *
     * @param welcomeFiles
     */
    public void setWelcomeFiles(List<String> welcomeFiles) {
        synchronized (this.welcomeFiles) {
            if ( welcomeFiles != null ){
                this.welcomeFiles = welcomeFiles;
            }
        }
    }


    //--------------------------------------------------------------------------
    /**
     * Add a new welcome file.
     *
     * @param filename New welcome file name
     */
    public void addWelcomeFile(String filename) {
        synchronized (welcomeFiles) {
            welcomeFiles.add(filename);
        }
    }

    public List<EntryPointDefinition> getEntryPointDefinitions() {
        return entryPoints;
    }

    public void setEntryPointDefinitions(List<EntryPointDefinition> entryPointDefinitions) {
        this.entryPoints = entryPointDefinitions;
    }

    //--------------------------------------------------------------------------
    /**
     * Return the list of welcome file.
     *
     * @return welcomeFiles
     */
    public List<String> getWelcomeFiles() {
        return welcomeFiles;
    }

    //--------------------------------------------------------------------------
    /**
     * Return the context
     *
     */
    public String getContext(){
        return context;
    }

    //--------------------------------------------------------------------------
    /**
     * Set the context
     *
     * @param val
     */
    public void setContext(String val){
        context = val;
    }

    //--------------------------------------------------------------------------
    /**
     * Return the display name
     *
     */
    public String getDisplayName(){
        return displayName;
    }

    //--------------------------------------------------------------------------
    /**
     * Set the display name
     *
     * @param val
     */
    public void setDisplayName(String val){
        displayName = val;
    }

    //--------------------------------------------------------------------------
    /**
     * Return the descr
     *
     */
    public String getDescr(){
        return descr;
    }

    //--------------------------------------------------------------------------
    /**
     * Set the descr
     *
     * @param val
     */
    public void setDescr(String val){
        descr = val;
    }

    //--------------------------------------------------------------------------
    /**
     * Find an exact servlet mapping pattern matching the given path if any
     *
     * @param path ( servlet pat + path info + querystring )
     */
    private String findServletExactMapping(String path){
        if ( path == null ){
            return null;
        }

        Iterator<String> iterator = servletMappings.keySet().iterator();
        String pattern = null;
        String result = "";
        while ( iterator.hasNext() )
        {
            pattern = iterator.next();
            if ( !pattern.equals("/")
                 || !pattern.startsWith("*.")
                 || !(pattern.startsWith("/") && pattern.endsWith("/*")) )
            {
                // we've got an exact mapping pattern

                if ( path.startsWith(pattern) ){
                    boolean match = false;
                    if ( pattern.length() == path.length() ){
                        match = true;
                    } else if ( pattern.endsWith("/") ){
                        match = true;
                    } else if ( path.charAt(pattern.length())=='/' ) {
                        match = true;
                    }
                    if ( match && (pattern.length() > result.length()) ){
                        result = pattern;
                    }
                }
            }
        }

        JahiaConsole.println(	CLASS_NAME+".findServletExactMapping",
                                "result [" + result + "]");

        if ( result.length() == 0 ){
            return null;
        }
        return result;
    }

    //--------------------------------------------------------------------------
    /**
     * Find a servlet path mapping pattern matching the given path if any
     *
     * @param path ( servlet pat + path info + querystring )
     */
    private String findServletPathMapping(String path){
        if ( path == null ){
            return null;
        }

        Iterator<String> iterator = servletMappings.keySet().iterator();
        String pattern = null;
        String result = "";
        while ( iterator.hasNext() )
        {
            pattern = iterator.next();
            if ( pattern.startsWith("/") && pattern.endsWith("/*") )
            {

                // we've got a path mapping pattern
                if ( path.startsWith(pattern.substring(0,pattern.length()-2)) ){
                    String str = pattern.substring(0,pattern.length()-2);
                    if ( (str.startsWith("/") || str.startsWith("?") || str.startsWith(";")
                         || str.startsWith("#") || str.startsWith("&") ) && pattern.length() > result.length() ){
                    result = pattern;
                }
            }
        }
        }

        JahiaConsole.println(	CLASS_NAME+".findServletPathMapping",
                                "result [" + result + "]");

        if ( result.length() == 0 ){
            return null;
        }
        return result;
    }

    //--------------------------------------------------------------------------
    /**
     * Find a servlet extension mapping pattern matching the given path if any
     *
     * @param path ( servlet pat + path info + querystring )
     */
    private String findServletExtensionMapping(String path){
        if ( path == null ){
            return null;
        }

        int pos = path.indexOf("?");
        String str = path;

        if ( pos != -1 ){
            str = path.substring(0,pos);
        }

        pos = str.lastIndexOf("/");

        Iterator<String> iterator = servletMappings.keySet().iterator();
        String pattern = null;
        String result = "";
        int strPos = -1;
        while ( iterator.hasNext() )
        {
            pattern = iterator.next();
            if ( pattern.startsWith("*.") )
            {
                strPos = str.indexOf(pattern.substring(1));
                // we've got a path mapping pattern
                if ( strPos != -1 && pos < strPos
                     && pattern.length() > result.length() )
                {
                    result = pattern;
                }
            }
        }

        JahiaConsole.println(	CLASS_NAME+".findServletExtensionMapping",
                                "result [" + result + "]");

        if ( result.length() == 0 ){
            return null;
        }

        return result;
    }


}
