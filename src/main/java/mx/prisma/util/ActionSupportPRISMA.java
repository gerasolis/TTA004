/**
 * 
 */
package mx.prisma.util;


import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class ActionSupportPRISMA extends ActionSupport {
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	public static final String INDEX = "index";
	protected static final String EDIT = "edit";
	public static final String EDITNEW = "editNew";
	protected static final String SHOW = "show";
	protected static final String DELETE = "delete";
	protected String urlPrev;
	protected HttpServletRequest request = ServletActionContext.getRequest();
	public ActionSupportPRISMA() {
		super();
		try {
			SessionManager.pushURL(request);
			urlPrev = SessionManager.popURL(request);
		} catch (Exception e) {
			ErrorManager.agregaMensajeError(this, e);
		}
	}
	public String getUrlPrev() {
		return urlPrev;
	}

	public void setUrlPrev(String urlPrev) {
		this.urlPrev = urlPrev;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	
	
}
