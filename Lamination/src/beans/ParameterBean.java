package beans;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import entity.Parameter;
import service.ParameterService;
@ManagedBean
@SessionScoped
public class ParameterBean {
	
	private List<Parameter> parametros;
	private ParameterService parameterService = new ParameterService();
	private Parameter parametroAAgregar;
	private Parameter parametroAModificar;
	
	
	public String prepararParametro(){
		parametroAAgregar = new Parameter();
		return "crearParametro";
	}
	
	public String actualizarParametro(){
		if(parametroAModificar!=null){
			parameterService.update(parametroAModificar);
		}
		parametroAModificar = null;
		return "parametros";
	}
	
	public String agregarParametro(){
		parameterService.save(parametroAAgregar);
		return "parametros";
	}
	
	public List<Parameter> getParametros() {
		parametros = parameterService.list();
		return parametros;
	}

	public void setParametros(List<Parameter> parametros) {
		this.parametros = parametros;
	}

	public ParameterService getParameterService() {
		return parameterService;
	}

	public void setParameterService(ParameterService parameterService) {
		this.parameterService = parameterService;
	}

	public Parameter getParametroAAgregar() {
		return parametroAAgregar;
	}

	public void setParametroAAgregar(Parameter parametroAAgregar) {
		this.parametroAAgregar = parametroAAgregar;
	}

	public Parameter getParametroAModificar() {
		return parametroAModificar;
	}

	public void setParametroAModificar(Parameter parametroAModificar) {
		this.parametroAModificar = parametroAModificar;
	}

	

	
}
