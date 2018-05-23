package beans;

import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import entity.New;
import service.NewService;

@ManagedBean
@SessionScoped
public class NewBean {
	private List<New> noticias;
	private NewService newService = new NewService();
	private New noticiaAAgregar;
	
	public New getNoticiaAAgregar() {
		return noticiaAAgregar;
	}

	public void setNoticiaAAgregar(New noticiaAAgregar) {
		this.noticiaAAgregar = noticiaAAgregar;
	}

	public String prepararNoticia(){
		noticiaAAgregar = new New();
		noticiaAAgregar.setState("A");
		noticiaAAgregar.setDateNews(new Date());
		noticiaAAgregar.setIdUser(UserBean.darUsuarioEnSesion().getId());
		return "agregarNoticias";
	}
	
	public String agregarNoticia(){
		if(noticiaAAgregar!=null){
			newService.save(noticiaAAgregar);
			
			
			RequestContext c = RequestContext.getCurrentInstance();
			c.execute("PF('noticia').show()");
		}
		return "";
	}
	
	public List<New> getNoticias() {
		noticias = newService.list();
		return noticias;
	}
	public void setNoticias(List<New> noticias) {
		this.noticias = noticias;
	}
	
	
	
}
