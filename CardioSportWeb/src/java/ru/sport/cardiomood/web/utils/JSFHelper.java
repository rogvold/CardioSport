package ru.sport.cardiomood.web.utils;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Provides some handy routines for working with JSF context and session.
 *
 * @author danon
 */
public class JSFHelper {

    private FacesContext context;

    /**
     * Creates new instance with current Faces context.
     */
    public JSFHelper() {
        this(FacesContext.getCurrentInstance());
    }

    public JSFHelper(FacesContext context) {
        this.context = context;
    }

    public FacesContext getFacesContext() {
        return context;
    }

    public ExternalContext getExternalContext() {
        final FacesContext fc = getFacesContext();
        if (fc == null) {
            return null;
        }
        return fc.getExternalContext();
    }

    public HttpServletRequest getRequest() {
        final ExternalContext ec = getExternalContext();
        if (ec == null) {
            return null;
        }
        return (HttpServletRequest) ec.getRequest();
    }

    public Application getApplication() {
        final FacesContext fc = getFacesContext();
        if (fc == null) {
            return null;
        }
        return fc.getApplication();
    }

    public HttpSession getSession(boolean create) {
        final HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        return request.getSession(create);
    }

    public Long getUserId() {
        return SessionUtils.getSessionAttribute(Long.class, getSession(true), SessionUtils.USER_ID_SESSION_ATTR);
    }

    public void setUserId(Long userId) {
        SessionUtils.setSessionAttribute(getSession(true), SessionUtils.USER_ID_SESSION_ATTR, userId);
    }

    public FacesMessage addMessage(FacesMessage.Severity severity, String summary, String details) {
        return JSFHelper.addMessage(getFacesContext(), null, severity, summary, details);
    }

    public FacesMessage addMessage(String component, FacesMessage.Severity severity, String summary, String details) {
        return JSFHelper.addMessage(getFacesContext(), component, severity, summary, details);
    }

    public void redirect(String nav) {
        NavigationHandler handler = getApplication().getNavigationHandler();
        handler.handleNavigation(getFacesContext(), null, nav + "?faces-redirect=true");
    }

    public void redirect(String nav, String... params) {
        NavigationHandler handler = getApplication().getNavigationHandler();
        nav += "?faces-redirect=true&";
        for (int i = 0; i < params.length - 1; i += 2) {
            nav += params[i] + "=" + params[i + 1];
        }
        handler.handleNavigation(getFacesContext(), null, nav);
    }

    public static FacesMessage addMessage(FacesContext fc, String component, FacesMessage.Severity severity, String summary, String details) {
        return addMessage(fc, component, new FacesMessage(severity, summary, details));
    }

    public static FacesMessage addMessage(FacesContext fc, String component, FacesMessage msg) {
        if (fc != null) {
            fc.addMessage(component, msg);
        }
        return msg;
    }
}