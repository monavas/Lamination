package beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.primefaces.context.RequestContext;
import org.apache.log4j.*;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import entity.Audit;
import entity.Parameter;
import entity.User;
import service.AuditService;
import service.ParameterService;
import service.UserService;


@ManagedBean
@SessionScoped
public class UserBean {
	
	private Logger log = Logger.getLogger(UserBean.class);
	
	
	
	private static User usuarioEnSesion;
	private User usuarioAIngresar = new User();
	private User usuarioAAgregar;
	private User usuarioAModificar;
	private String usuarioOlvidoContraseña = null;
	private List<User> usuarios;
	private UserService userService = new UserService();
	private AuditService auditService = new AuditService();
	private String contraseñaNueva = null;
	private String confContra = null;
	private List<Audit> auditoria;
	private int intentos = 0;
	private String imagenFondo;

	
	
	
	public String getImagenFondo() {
		if(usuarioEnSesion!=null){
			String pais = usuarioEnSesion.getPais().toLowerCase();
			String[] partes = pais.split(" ");
			if(partes[0].equalsIgnoreCase("OTRO")){
				imagenFondo = "../Imagenes/fondo.jpg";
			}else{

				imagenFondo = "../Imagenes/Paises/"+partes[0]+".jpg";
			}
		}else{
			imagenFondo = "../Imagenes/fondo.jpg";
		}
		return imagenFondo;
	}

	public void setImagenFondo(String imagenFondo) {
		this.imagenFondo = imagenFondo;
	}

	public int getIntentos() {
		return intentos;
	}

	public void setIntentos(int intentos) {
		this.intentos = intentos;
	}

	public String prepararUsuarioAAgregar(){
		usuarioAAgregar = new User();




		return "registrar";
	}

	
	public void exportarPDFAudit(){

		
		getAuditoria();

		File file = null;
		try {

			file = new File("auditoria.pdf");
			FileOutputStream archivo = new FileOutputStream(file);
			
			Document doc = new Document();
			PdfWriter.getInstance(doc, archivo);
			doc.open();


			Image img = Image.getInstance("http://localhost:8080/Lamination/Imagenes/cabeza.png");
			img.setAlignment(Element.ALIGN_CENTER);
			img.scaleToFit(500, 500);
			doc.add(img);
			
			Paragraph p2 = new Paragraph(10);
			Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK);
			
			p2.add(Chunk.NEWLINE);
			p2.add("Fecha del registro:   "+(new Date().toLocaleString())+"\n\n\n");
			p2.add(Chunk.NEWLINE);
			p2.add(Chunk.NEWLINE);
			p2.setAlignment(Element.ALIGN_LEFT);
			doc.add(p2);
			Paragraph p = new Paragraph(10);
			p.add(Chunk.NEWLINE);
			p.add("AUDITORIA");
			p.add(Chunk.NEWLINE);
			p.add(Chunk.NEWLINE);
			p.setAlignment(Element.ALIGN_CENTER);
			doc.add(p);
			PdfPTable tabla = new PdfPTable(6);
			tabla.setWidthPercentage(100);
			
			PdfPCell c1 = new PdfPCell(new Phrase("Id del usuario", negrita));
			PdfPCell c2 = new PdfPCell(new Phrase("Nombre de la tabla", negrita));
			PdfPCell c3 = new PdfPCell(new Phrase("Id de la tabla", negrita));
			PdfPCell c4 = new PdfPCell(new Phrase("Fecha", negrita));
			PdfPCell c5 = new PdfPCell(new Phrase("Operación", negrita));
			PdfPCell c6 = new PdfPCell(new Phrase("Ip del usuario", negrita));
			
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			c3.setHorizontalAlignment(Element.ALIGN_CENTER);
			c4.setHorizontalAlignment(Element.ALIGN_CENTER);
			c5.setHorizontalAlignment(Element.ALIGN_CENTER);
			c6.setHorizontalAlignment(Element.ALIGN_CENTER);

			c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
			c2.setBackgroundColor(BaseColor.LIGHT_GRAY);
			c3.setBackgroundColor(BaseColor.LIGHT_GRAY);
			c4.setBackgroundColor(BaseColor.LIGHT_GRAY);
			c5.setBackgroundColor(BaseColor.LIGHT_GRAY);
			c6.setBackgroundColor(BaseColor.LIGHT_GRAY);

			tabla.addCell(c1);
			tabla.addCell(c2);
			tabla.addCell(c3);
			tabla.addCell(c4);
			tabla.addCell(c5);
			tabla.addCell(c6);

			for (int z = 0;z<auditoria.size();z++) {
				
				Audit a = auditoria.get(z);

				tabla.addCell(""+a.getId());
				tabla.addCell(a.getTableName());
				tabla.addCell(""+a.getTableId());
				tabla.addCell(""+a.getCreateDate());
				tabla.addCell(a.getOperation());
				tabla.addCell(a.getIp());
			}

			doc.add(tabla);

			Paragraph p1 = new Paragraph(10);
			p1.add(Chunk.NEWLINE);
			p1.add(Chunk.NEWLINE);
			p1.add(Chunk.NEWLINE);
			p1.setAlignment(Element.ALIGN_LEFT);
			doc.add(p1);
			
			doc.close();
			archivo.close();

			HttpServletResponse response= (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
			FacesContext context = FacesContext.getCurrentInstance();
			int read = 0;
			byte[] bytes = new byte[1024];

			response.setContentType("application/pdf");

			response.setHeader("Content-Disposition", "attachment;filename=\"" +
					file.getName() + "\"");

			FileInputStream fis = null;
			ServletOutputStream os = null;


			try {
				
				fis = new FileInputStream(file);
				os = response.getOutputStream();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				while((read = fis.read(bytes)) != -1){
					os.write(bytes,0,read);
				}
				os.flush();
				os.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			FacesContext.getCurrentInstance().responseComplete();
			
			Audit audit = new Audit();
			audit.setCreateDate(new Date());
			audit.setOperation("PDF A");
			audit.setUserId(1);
			audit.setTableId(0);
			audit.setTableName("Audit");
			HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			String ipAddress = request.getHeader("X-FORWARDED-FOR");
			if (ipAddress == null) {
				ipAddress = request.getRemoteAddr();
			}
			audit.setIp(ipAddress);
			auditService.save(audit);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		
	}


	public void exportarExcelAudit(){

		File file = new File("auditoria.xlsx");
		XSSFWorkbook wk = new XSSFWorkbook();

		
		XSSFCellStyle style = wk.createCellStyle();

		style.setFillForegroundColor(HSSFColor.BLUE_GREY.index);
	    style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	    XSSFFont font = wk.createFont();
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
	    
	    
	    
		style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		
		XSSFSheet pagina = wk.createSheet("Auditoria");

		Row r = pagina.createRow(0);

		Cell c = r.createCell(0);
		c.setCellStyle(style);
		c.setCellValue("Id del usuario");
		Cell c1 = r.createCell(1);
		c1.setCellStyle(style);
		c1.setCellValue("Nombre de la tabla");
		Cell c2 = r.createCell(2);
		c2.setCellStyle(style);
		c2.setCellValue("Id de la tabla");
		Cell c3 = r.createCell(3);
		c3.setCellStyle(style);
		c3.setCellValue("Fecha");
		Cell c4 = r.createCell(4);
		c4.setCellStyle(style);
		c4.setCellValue("Operación");
		Cell c5 = r.createCell(5);
		c5.setCellStyle(style);
		c5.setCellValue("Ip del usuario");
		
		XSSFCellStyle style2 = wk.createCellStyle();

	    
	    
	    
		style2.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		style2.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		style2.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		style2.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		
		
		getAuditoria();
		for(int z = 0;z<auditoria.size();z++){
			Audit a = auditoria.get(z);
			Row row = pagina.createRow((z+1));
			Cell ce1 = row.createCell(0);
			ce1.setCellStyle(style2);
			ce1.setCellValue(a.getId());
			Cell ce2 = row.createCell(1);
			ce2.setCellStyle(style2);
			ce2.setCellValue(a.getTableName());
			Cell ce3 = row.createCell(2);
			ce3.setCellStyle(style2);
			ce3.setCellValue(a.getTableId());
			Cell ce4 = row.createCell(3);
			ce4.setCellStyle(style2);
			ce4.setCellValue(a.getCreateDate().toLocaleString());
			Cell ce5 = row.createCell(4);
			ce5.setCellStyle(style2);
			ce5.setCellValue(a.getOperation());
			Cell ce6 = row.createCell(5);
			ce6.setCellStyle(style2);
			ce6.setCellValue(a.getIp());
		}
		if (pagina.getPhysicalNumberOfRows() > 0) {
            Row row = pagina.getRow(0);
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                int columnIndex = cell.getColumnIndex();
                pagina.autoSizeColumn(columnIndex);
            }
        }
		try {
			FileOutputStream s = new FileOutputStream(file);
			wk.write(s);
		} catch (Exception e) {
			// TODO: handle exception
		}

		HttpServletResponse response= (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
		FacesContext context = FacesContext.getCurrentInstance();
		int read = 0;
		byte[] bytes = new byte[1024];

		response.setContentType("application/xlsx");

		response.setHeader("Content-Disposition", "attachment;filename=\"" +
				file.getName() + "\"");

		FileInputStream fis = null;
		ServletOutputStream os = null;


		try {
			fis = new FileInputStream(file);
			os = response.getOutputStream();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			while((read = fis.read(bytes)) != -1){
				os.write(bytes,0,read);
			}
			os.flush();
			os.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		FacesContext.getCurrentInstance().responseComplete();
		
		
		Audit audit = new Audit();
		audit.setCreateDate(new Date());
		audit.setOperation("Excel A");
		audit.setUserId(1);
		audit.setTableId(0);
		audit.setTableName("Audit");
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}
		audit.setIp(ipAddress);
		auditService.save(audit);
	}
	
	public void exportarExcelUser(){
		
		File file = new File("usuarios.xlsx");
		XSSFWorkbook wk = new XSSFWorkbook();
		
		XSSFCellStyle style = wk.createCellStyle();

		style.setFillForegroundColor(HSSFColor.BLUE_GREY.index);
	    style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	    XSSFFont font = wk.createFont();
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
	    
	    
	    
		style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		
		XSSFSheet pagina = wk.createSheet("Usuarios");

		Row r = pagina.createRow(0);

		Cell c = r.createCell(0);
		c.setCellStyle(style);
		c.setCellValue("Id");
		Cell c1 = r.createCell(1);
		c1.setCellStyle(style);
		c1.setCellValue("Nombre de usuario");
		Cell c2 = r.createCell(2);
		c2.setCellStyle(style);
		c2.setCellValue("Nombre completo");
		Cell c3 = r.createCell(3);
		c3.setCellStyle(style);
		c3.setCellValue("Número telefónico");
		Cell c4 = r.createCell(4);
		c4.setCellStyle(style);
		c4.setCellValue("Correo electrónico");
		Cell c5 = r.createCell(5);
		c5.setCellStyle(style);
		c5.setCellValue("Fecha ultimo cambio de contraseña");
		Cell c6 = r.createCell(6);
		c6.setCellStyle(style);
		c6.setCellValue("Estado");
		
		
		XSSFCellStyle style2 = wk.createCellStyle();

	    
	    
	    
		style2.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		style2.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		style2.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		style2.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		
		getUsuarios();
		for(int z = 0;z<usuarios.size();z++){
			User u = usuarios.get(z);
			Row row = pagina.createRow((z+1));
			Cell ce1 = row.createCell(0);
			ce1.setCellStyle(style2);
			ce1.setCellValue(u.getId());
			Cell ce2 = row.createCell(1);
			ce2.setCellStyle(style2);
			ce2.setCellValue(u.getUserName());
			Cell ce3 = row.createCell(2);
			ce3.setCellStyle(style2);
			ce3.setCellValue(u.getFullName());
			Cell ce4 = row.createCell(3);
			ce4.setCellStyle(style2);
			ce4.setCellValue(u.getPhoneNumber());
			Cell ce5 = row.createCell(4);
			ce5.setCellStyle(style2);
			ce5.setCellValue(u.getEmailAddress());
			Cell ce6 = row.createCell(5);
			ce6.setCellStyle(style2);
			ce6.setCellValue(u.getDateLastPassword().toLocaleString());
			Cell ce7 = row.createCell(6);
			ce7.setCellStyle(style2);
			ce7.setCellValue(u.getActive());
		}
		
        if (pagina.getPhysicalNumberOfRows() > 0) {
            Row row = pagina.getRow(0);
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                int columnIndex = cell.getColumnIndex();
                pagina.autoSizeColumn(columnIndex);
            }
        }
		try {
			FileOutputStream s = new FileOutputStream(file);
			wk.write(s);
		} catch (Exception e) {
			// TODO: handle exception
		}

		HttpServletResponse response= (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
		FacesContext context = FacesContext.getCurrentInstance();
		int read = 0;
		byte[] bytes = new byte[1024];

		response.setContentType("application/xlsx");

		response.setHeader("Content-Disposition", "attachment;filename=\"" +
				file.getName() + "\"");

		FileInputStream fis = null;
		ServletOutputStream os = null;


		try {
			fis = new FileInputStream(file);
			os = response.getOutputStream();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			while((read = fis.read(bytes)) != -1){
				os.write(bytes,0,read);
			}
			os.flush();
			os.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		FacesContext.getCurrentInstance().responseComplete();
		
		Audit audit = new Audit();
		audit.setCreateDate(new Date());
		audit.setOperation("Excel U");
		audit.setUserId(1);
		audit.setTableId(0);
		audit.setTableName("User");
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}
		audit.setIp(ipAddress);
		auditService.save(audit);
		
	}
	
	public String laminasFalatantes(){
		Audit audit = new Audit();
		audit.setCreateDate(new Date());
		audit.setOperation("Faltantes");
		audit.setUserId(usuarioEnSesion.getId());
		audit.setTableId(0);
		audit.setTableName("User");
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}
		audit.setIp(ipAddress);
		auditService.save(audit);
		
		
		return "laminasFaltantes";
	}
	
	public String laminasRepetidas() {
		
		
		Audit audit = new Audit();
		audit.setCreateDate(new Date());
		audit.setOperation("Repetidas");
		audit.setUserId(usuarioEnSesion.getId());
		audit.setTableId(0);
		audit.setTableName("User");
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}
		audit.setIp(ipAddress);
		auditService.save(audit);
		return "laminasRepetidas";
	}
	
	public void exportarPDFUser(){

		getUsuarios();


		File file = null;
		try {





			file = new File("usuarios.pdf");
			FileOutputStream archivo = new FileOutputStream(file);
			Document doc = new Document();
			PdfWriter.getInstance(doc, archivo);
			doc.open();


			Image img = Image.getInstance("http://localhost:8080/Lamination/Imagenes/cabeza.png");
			img.setAlignment(Element.ALIGN_CENTER);
			img.scaleToFit(500, 500);
			doc.add(img);
			Paragraph p2 = new Paragraph(10);
			Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK);
			p2.add(Chunk.NEWLINE);
			p2.add("Fecha del registro:   "+(new Date().toLocaleString())+"\n\n\n");
			p2.add(Chunk.NEWLINE);
			p2.add(Chunk.NEWLINE);
			p2.setAlignment(Element.ALIGN_LEFT);
			doc.add(p2);
			Paragraph p = new Paragraph(10);
			p.add(Chunk.NEWLINE);
			p.add("USUARIOS");
			p.add(Chunk.NEWLINE);
			p.add(Chunk.NEWLINE);
			p.setAlignment(Element.ALIGN_CENTER);
			doc.add(p);
			PdfPTable tabla = new PdfPTable(7);
			tabla.setWidthPercentage(100);
			PdfPCell c1 = new PdfPCell(new Phrase("Id", negrita));
			PdfPCell c2 = new PdfPCell(new Phrase("Nombre de usuario", negrita));
			PdfPCell c3 = new PdfPCell(new Phrase("Nombre completo", negrita));
			PdfPCell c4 = new PdfPCell(new Phrase("Número telefónico", negrita));
			PdfPCell c5 = new PdfPCell(new Phrase("Correo electrónico", negrita));
			PdfPCell c6 = new PdfPCell(new Phrase("Fecha ultimo cambio de contraseña", negrita));
			PdfPCell c7 = new PdfPCell(new Phrase("Estado", negrita));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			c3.setHorizontalAlignment(Element.ALIGN_CENTER);
			c4.setHorizontalAlignment(Element.ALIGN_CENTER);
			c5.setHorizontalAlignment(Element.ALIGN_CENTER);
			c6.setHorizontalAlignment(Element.ALIGN_CENTER);
			c7.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
			c2.setBackgroundColor(BaseColor.LIGHT_GRAY);
			c3.setBackgroundColor(BaseColor.LIGHT_GRAY);
			c4.setBackgroundColor(BaseColor.LIGHT_GRAY);
			c5.setBackgroundColor(BaseColor.LIGHT_GRAY);
			c6.setBackgroundColor(BaseColor.LIGHT_GRAY);
			c7.setBackgroundColor(BaseColor.LIGHT_GRAY);
			tabla.addCell(c1);
			tabla.addCell(c2);
			tabla.addCell(c3);
			tabla.addCell(c4);
			tabla.addCell(c5);
			tabla.addCell(c6);
			tabla.addCell(c7);

			for (int z = 0;z<usuarios.size();z++) {
				User u = usuarios.get(z);

				tabla.addCell(""+u.getId());
				tabla.addCell(""+u.getUserName());
				tabla.addCell(""+u.getFullName());
				tabla.addCell(""+u.getPhoneNumber());
				tabla.addCell(""+u.getEmailAddress());
				tabla.addCell(""+u.getDateLastPassword().toLocaleString());
				tabla.addCell(""+u.getActive());
			}

			doc.add(tabla);



			Paragraph p1 = new Paragraph(10);
			p1.add(Chunk.NEWLINE);
			p1.add(Chunk.NEWLINE);
			p1.add(Chunk.NEWLINE);
			p1.setAlignment(Element.ALIGN_LEFT);
			doc.add(p1);
			doc.close();
			archivo.close();




			HttpServletResponse response= (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
			FacesContext context = FacesContext.getCurrentInstance();
			int read = 0;
			byte[] bytes = new byte[1024];

			response.setContentType("application/pdf");

			response.setHeader("Content-Disposition", "attachment;filename=\"" +
					file.getName() + "\"");

			FileInputStream fis = null;
			ServletOutputStream os = null;


			try {
				fis = new FileInputStream(file);
				os = response.getOutputStream();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				while((read = fis.read(bytes)) != -1){
					os.write(bytes,0,read);
				}
				os.flush();
				os.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			FacesContext.getCurrentInstance().responseComplete();

			Audit audit = new Audit();
			audit.setCreateDate(new Date());
			audit.setOperation("PDF U");
			audit.setUserId(1);
			audit.setTableId(0);
			audit.setTableName("User");
			HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			String ipAddress = request.getHeader("X-FORWARDED-FOR");
			if (ipAddress == null) {
				ipAddress = request.getRemoteAddr();
			}
			audit.setIp(ipAddress);
			auditService.save(audit);


		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public String actualizarUsuario(){
		if(usuarioAModificar!=null){
			Audit audit = new Audit();
			audit.setCreateDate(new Date());
			audit.setOperation("Modificó");
			audit.setUserId(1);
			audit.setTableId(usuarioAModificar.getId());
			audit.setTableName("User");
			HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			String ipAddress = request.getHeader("X-FORWARDED-FOR");
			if (ipAddress == null) {
				ipAddress = request.getRemoteAddr();
			}
			audit.setIp(ipAddress);
			auditService.save(audit);
			userService.uptade(usuarioAModificar);
		}
		usuarioAModificar = null;
		return "gestionUsuarios";
	}

	public String auditoria(){
		Audit audit = new Audit();
		audit.setCreateDate(new Date());
		audit.setOperation("Auditoria");
		audit.setUserId(1);
		audit.setTableId(0);
		audit.setTableName("Audit");
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}
		audit.setIp(ipAddress);
		auditService.save(audit);
		return "auditoria";
	}

	public static User darUsuarioEnSesion(){
		return usuarioEnSesion;
	}

	public String buscarLaminas(){
		Audit audit = new Audit();
		audit.setCreateDate(new Date());
		audit.setOperation("Buscó L");
		audit.setUserId(usuarioEnSesion.getId());
		audit.setTableId(0);
		audit.setTableName("User");
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}
		audit.setIp(ipAddress);
		auditService.save(audit);
		return "buscarLaminas";
	}

	public String iniciarSesion(){
		
		if(usuarioAIngresar!=null){
			usuarioEnSesion = userService.getUserLogin(usuarioAIngresar.getUserName().trim(), 
					getStringMessageDigest(usuarioAIngresar.getPassword().trim()));
			if(usuarioEnSesion!=null){
				intentos = 0;
				if(usuarioEnSesion.getUserType().equalsIgnoreCase("ADMIN")){
					Audit audit = new Audit();
					audit.setCreateDate(new Date());
					audit.setOperation("Inició");
					audit.setTableId(0);
					audit.setTableName("User");
					audit.setUserId(1);
					HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
					String ipAddress = request.getHeader("X-FORWARDED-FOR");
					if (ipAddress == null) {
						ipAddress = request.getRemoteAddr();
					}
					audit.setIp(ipAddress);
					auditService.save(audit);
					
					
					
					return "sesionADMIN";

				}else if(usuarioEnSesion.getActive().equalsIgnoreCase("P")){
					Audit audit = new Audit();
					audit.setCreateDate(new Date());
					audit.setOperation("Inició P");
					audit.setTableId(0);
					audit.setTableName("User");
					audit.setUserId(usuarioEnSesion.getId());
					HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
					String ipAddress = request.getHeader("X-FORWARDED-FOR");
					if (ipAddress == null) {
						ipAddress = request.getRemoteAddr();
					}
					audit.setIp(ipAddress);
					auditService.save(audit);
					return "primerIngreso";
				}else if(usuarioEnSesion.getActive().equalsIgnoreCase("A")){

					Parameter p = new ParameterService().getParameter("Dias");
					int nD = p.getNumberValue();
					Date date = usuarioEnSesion.getDateLastPassword();
					long sD = 86400000;
					date = new Date(date.getTime()+(sD*nD));
					Date hoy = new Date();
					int co = hoy.compareTo(date);
					if(co>=0){
						return "primerIngreso";
					}

					//TODO: Forzar cambio de contraseña
					Audit audit = new Audit();
					audit.setCreateDate(new Date());
					audit.setOperation("Inició");
					audit.setTableId(0);
					audit.setTableName("User");
					audit.setUserId(usuarioEnSesion.getId());
					HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
					String ipAddress = request.getHeader("X-FORWARDED-FOR");
					if (ipAddress == null) {
						ipAddress = request.getRemoteAddr();
					}
					audit.setIp(ipAddress);
					auditService.save(audit);
					return "sesion";
				}else{
					RequestContext c = RequestContext.getCurrentInstance();
					c.execute("PF('inicioSesionBloqueo').show()");
					return "";
				}
			}else{
				User u = userService.getUserUserName(usuarioAIngresar.getUserName().trim());
				if(u!=null&&!(u.getUserType().equalsIgnoreCase("ADMIN"))){
					intentos++;
					Parameter p = new ParameterService().getParameter("Intentos");
					int i = p.getNumberValue();
					if(intentos>=i){
						u.setActive("I");
						userService.uptade(u);
						intentos = 0;
						Audit audit = new Audit();
						audit.setCreateDate(new Date());
						audit.setOperation("Bloqueo");
						audit.setTableId(u.getId());
						audit.setTableName("User");
						audit.setUserId(u.getId());
						HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
						String ipAddress = request.getHeader("X-FORWARDED-FOR");
						if (ipAddress == null) {
							ipAddress = request.getRemoteAddr();
						}
						audit.setIp(ipAddress);
						auditService.save(audit);

						RequestContext c = RequestContext.getCurrentInstance();
						c.execute("PF('inicioSesionBloqueoP').show()");
					}else{
						RequestContext c = RequestContext.getCurrentInstance();
						c.execute("PF('inicioSesionIncorrecto').show()");
					}
				}else{
					RequestContext c = RequestContext.getCurrentInstance();
					c.execute("PF('inicioSesionIncorrecto').show()");
				}
				return "";
			}
		}
		
		return "";
		
	}

	
	
	public String cerrarSesion(){

		Audit audit = new Audit();
		audit.setCreateDate(new Date());
		audit.setOperation("Salió");
		audit.setTableId(0);
		audit.setTableName("User");
		audit.setUserId(usuarioEnSesion.getId());
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}
		audit.setIp(ipAddress);
		auditService.save(audit);
		usuarioAIngresar = new User();
		usuarioEnSesion = null;
		return "index";
	}


	public String olvidoContraseña(){
		if(usuarioOlvidoContraseña!=null){
			User u = userService.getUserUserName(usuarioOlvidoContraseña.trim());

			if(u==null){
				RequestContext c = RequestContext.getCurrentInstance();
				c.execute("PF('olvidoContrasenia').show()");
				
				
				
				return "";
			}else{
				Audit audit = new Audit();
				audit.setCreateDate(new Date());
				audit.setOperation("Recuperó C");
				audit.setTableId(u.getId());
				audit.setTableName("User");
				audit.setUserId(u.getId());
				HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
				String ipAddress = request.getHeader("X-FORWARDED-FOR");
				if (ipAddress == null) {
					ipAddress = request.getRemoteAddr();
				}
				audit.setIp(ipAddress);
				auditService.save(audit);
				RequestContext c = RequestContext.getCurrentInstance();
				c.execute("PF('olvidoCorrecto').show()");
				enviarCorreoRecuperarContraseña(u);
				
				
				
				
				usuarioOlvidoContraseña = null;
			}
		}
		return "";
	}

	public String darContraseñaAlAzar(){
		String letras = "qwertyuiopasdfghjklzxcvbnm1234567890.,_-";
		char[] l = letras.toCharArray();
		String clave = "";
		Random r = new Random();
		for (int i = 0; i < 8; i++) {
			int n = r.nextInt(l.length);
			String p = ""+l[n];
			int a = r.nextInt(2);
			if(a==0){
				p = p.toUpperCase();
			}
			clave+=p;
		}
		return clave;
	}

	public void enviarCorreoRecuperarContraseña(User usuario){
		Properties props = System.getProperties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.user", "laminationmundial@gmail.com");
		props.put("mail.smtp.password", "LAMINATIONmundial2018");

		Session session = Session.getInstance(props,null);

		try {

			String clave = darContraseñaAlAzar();
			usuario.setPassword(getStringMessageDigest(clave));
			usuario.setActive("P");
			userService.uptade(usuario);


			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress("laminationmundial@gmail.com"));
			message.setRecipient(Message.RecipientType.TO,new InternetAddress(usuario.getEmailAddress()));
			message.setSubject("Contraseña");
			message.setText("Recibimos una solicitud de cambio de contraseña para tu cuenta de Lamination.\n"
					+ "Usuario: "+usuario.getUserName()+"\n"
					+ "Tu nueva contraseña es: "+clave+"\n"
					+ "Para mas información puedes comunicarte con nosotros: laminationmundial@gmail.com");

			Transport t = session.getTransport("smtp");
			t.connect("smtp.gmail.com","laminationmundial@gmail.com","LAMINATIONmundial2018");
			t.sendMessage(message,message.getAllRecipients());
			t.close();



		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public String cambiarContraseniaPrimerIngreso(){
		if(usuarioEnSesion!=null&&contraseñaNueva!=null&&confContra!=null){
			if(contraseñaNueva.trim().equals(confContra.trim())){

				String c = usuarioEnSesion.getPassword();
				if(c.trim().equals(getStringMessageDigest(contraseñaNueva.trim()))){
					
					
					RequestContext co = RequestContext.getCurrentInstance();
					co.execute("PF('primerAnterior').show()");
					
					return "";
				}else{
					usuarioEnSesion.setPassword(getStringMessageDigest(contraseñaNueva));
					usuarioEnSesion.setDateLastPassword(new Date());
					usuarioEnSesion.setActive("A");
					userService.uptade(usuarioEnSesion);
					contraseñaNueva = null;
					confContra = null;
					return "sesion";
				}


			}else{
				RequestContext co = RequestContext.getCurrentInstance();
				co.execute("PF('primerDiferente').show()");
				
				return "";
			}


		}

		return "";
	}

	public String eliminarUsuario(){
		if(usuarioAModificar!=null){
			if(!usuarioAModificar.getUserType().equalsIgnoreCase("ADMIN")){
				if(usuarioAModificar.getActive().equalsIgnoreCase("A")){
					usuarioAModificar.setActive("I");
					Audit audit = new Audit();
					audit.setCreateDate(new Date());
					audit.setOperation("Bloqueo");
					audit.setTableId(usuarioAModificar.getId());
					audit.setTableName("User");
					audit.setUserId(1);
					HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
					String ipAddress = request.getHeader("X-FORWARDED-FOR");
					if (ipAddress == null) {
						ipAddress = request.getRemoteAddr();
					}
					audit.setIp(ipAddress);
					auditService.save(audit);
				}else{
					usuarioAModificar.setActive("A");
					Audit audit = new Audit();
					audit.setCreateDate(new Date());
					audit.setOperation("Activo");
					audit.setTableId(usuarioAModificar.getId());
					audit.setTableName("User");
					audit.setUserId(1);
					HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
					String ipAddress = request.getHeader("X-FORWARDED-FOR");
					if (ipAddress == null) {
						ipAddress = request.getRemoteAddr();
					}
					audit.setIp(ipAddress);
					auditService.save(audit);
				}
				userService.uptade(usuarioAModificar);
			}
			
		}
		return "";
	}
	
	
	
	public String prepararPerfil(){
		usuarioAModificar = usuarioEnSesion;
		return "perfilUser";
	}
	public String actualizarPerfil(){
		if(usuarioAModificar!=null){
					

					userService.uptade(usuarioAModificar);
					usuarioEnSesion = usuarioAModificar;
					
					RequestContext co = RequestContext.getCurrentInstance();
					co.execute("PF('actuPerfil').show()");
					
		}
		return "";
	}
	
	public String cambiarContraseña(){
		
		if(confContra!=null&&contraseñaNueva!=null){
			if(contraseñaNueva.trim().equals(confContra.trim())){
				usuarioEnSesion.setPassword(getStringMessageDigest(contraseñaNueva.trim()));
				usuarioEnSesion.setDateLastPassword(new Date());
				userService.uptade(usuarioEnSesion);
				contraseñaNueva = null;
				confContra = null;
				RequestContext co = RequestContext.getCurrentInstance();
				co.execute("PF('conCorr').show()");
			}else{
				RequestContext co = RequestContext.getCurrentInstance();
				co.execute("PF('conInc').show()");
			}
		}
		
		
		return "";
	}
	
	public String gestionDeUsuarios(){
		Audit audit = new Audit();
		audit.setCreateDate(new Date());
		audit.setOperation("Gestión");
		audit.setTableId(0);
		audit.setTableName("User");
		audit.setUserId(1);
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}
		audit.setIp(ipAddress);
		auditService.save(audit);
		return "gestionUsuarios";
	}

	private static String toHexadecimal(byte[] digest) {
		String hash = "";
		for (byte aux : digest) {
			int b = aux & 0xff;
			if (Integer.toHexString(b).length() == 1)
				hash += "0";
			hash += Integer.toHexString(b);
		}
		return hash;
	}

	public static String getStringMessageDigest(String message) {
		byte[] digest = null;
		byte[] buffer = message.getBytes();
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(buffer);
			digest = messageDigest.digest();
		} catch (NoSuchAlgorithmException ex) {

		}
		return toHexadecimal(digest);
	}

	public String agregarUsuario(){
		
		if(usuarioAAgregar!=null){
			
			User u = userService.getUserUserName(usuarioAAgregar.getUserName().trim());
			
			if(u==null){
				
				usuarioAAgregar.setActive("P");
				usuarioAAgregar.setUserType("Normal");
				String clave = darContraseñaAlAzar();
				usuarioAAgregar.setPassword(getStringMessageDigest(clave));
				usuarioAAgregar.setDateLastPassword(new Date());
				userService.save(usuarioAAgregar);
				Audit audit = new Audit();
				audit.setCreateDate(new Date());
				audit.setOperation("Registró");
				audit.setTableId(0);
				audit.setTableName("User");
				audit.setUserId(userService.getUserUserName(usuarioAAgregar.getUserName().trim()).getId());
				HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
				String ipAddress = request.getHeader("X-FORWARDED-FOR");
				if (ipAddress == null) {
					ipAddress = request.getRemoteAddr();
				}
				audit.setIp(ipAddress);
				auditService.save(audit);
				enviarCorreo(clave);

				
				RequestContext c = RequestContext.getCurrentInstance();
				c.execute("PF('regisCorrecto').show()");
				return "index";
			}else{
				
				RequestContext c = RequestContext.getCurrentInstance();
				c.execute("PF('regisIncorrecto').show()");

				return "";
			}
		}else{

			return "";
		}
	}

	public void enviarCorreo(String cont){
		
		Properties props = System.getProperties();
		
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.user", "laminationmundial@gmail.com");
		props.put("mail.smtp.password", "LAMINATIONmundial2018");

		Session session = Session.getInstance(props,null);

		try {

			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress("laminationmundial@gmail.com"));
			message.setRecipient(Message.RecipientType.TO,new InternetAddress(usuarioAAgregar.getEmailAddress()));
			message.setSubject("Contraseña");
			message.setText("Bienvenido a Lamination.\n¡Nos encanta que estes aquí!\nEsta es tu información:\n"
					+ "Usuario: "+usuarioAAgregar.getUserName()+"\n"
					+ "Correo electronico: " + usuarioAAgregar.getEmailAddress() + "\n"
					+ "Nombre completo: "+usuarioAAgregar.getFullName()+"\n"
					+ "Número de telefono: "+usuarioAAgregar.getPhoneNumber()+"\n"
					+ "Tu contraseña es: "+cont+"\n"
					+ "Para mas información puedes comunicarte con nosotros: laminationmundial@gmail.com");




			//	            Multipart multipart = new MimeMultipart();
			//	                  
			//	                   
			//	                	   MimeBodyPart messageBodyPart = new MimeBodyPart();
			//	                      multipart.addBodyPart(messageBodyPart);
			//	                   
			////	                   
			////	                   File f2 = new File("./data/afedo.jpg");
			////	                   if( f2.exists() ){
			////	                	   MimeBodyPart messageBodyPart = new MimeBodyPart();
			////	       	            messageBodyPart.setContent(message,"text/html");
			////	                      DataSource source = new FileDataSource("./data/afedo.jpg");
			////	                      messageBodyPart.setDataHandler( new DataHandler(source) );
			////	                      messageBodyPart.setFileName(f2.getName());
			////	                      multipart.addBodyPart(messageBodyPart);
			////	                   }
			//	          
			//	            message.setContent(multipart);



			Transport t = session.getTransport("smtp");
			t.connect("smtp.gmail.com","laminationmundial@gmail.com","LAMINATIONmundial2018");
			t.sendMessage(message,message.getAllRecipients());
			t.close();



		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public User getUsuarioEnSesion() {
		return usuarioEnSesion;
	}

	public void setUsuarioEnSesion(User usuarioEnSesion) {
		UserBean.usuarioEnSesion = usuarioEnSesion;
	}

	public User getUsuarioAAgregar() {
		return usuarioAAgregar;
	}

	public void setUsuarioAAgregar(User usuarioAAgregar) {
		this.usuarioAAgregar = usuarioAAgregar;
	}

	public User getUsuarioAModificar() {
		return usuarioAModificar;
	}

	public void setUsuarioAModificar(User usuarioAModificar) {
		this.usuarioAModificar = usuarioAModificar;
	}

	public List<User> getUsuarios() {
		usuarios = userService.list();
		return usuarios;
	}

	public void setUsuarios(List<User> usuarios) {
		this.usuarios = usuarios;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public User getUsuarioAIngresar() {
		return usuarioAIngresar;
	}

	public void setUsuarioAIngresar(User usuarioAIngresar) {
		this.usuarioAIngresar = usuarioAIngresar;
	}

	public String getContraseñaNueva() {
		return contraseñaNueva;
	}

	public void setContraseñaNueva(String contraseñaNueva) {
		this.contraseñaNueva = contraseñaNueva;
	}

	public String getConfContra() {
		return confContra;
	}

	public void setConfContra(String confContra) {
		this.confContra = confContra;
	}

	public String getUsuarioOlvidoContraseña() {
		return usuarioOlvidoContraseña;
	}

	public void setUsuarioOlvidoContraseña(String usuarioOlvidoContraseña) {
		this.usuarioOlvidoContraseña = usuarioOlvidoContraseña;
	}

	public List<Audit> getAuditoria() {
		auditoria = auditService.list();
		return auditoria;
	}

	public void setAuditoria(List<Audit> auditoria) {
		this.auditoria = auditoria;
	}



}
