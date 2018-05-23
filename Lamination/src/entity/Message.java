package entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the message database table.
 * 
 */
@Entity
@Table(name="message")
public class Message implements Serializable {
	private static final long serialVersionUID = 1L;

	
	private int id;

	private int idUser1;

	private int idUser2;

	private String text;

	public Message() {
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	@Column(name = "idUser1")
	public int getIdUser1() {
		return this.idUser1;
	}

	public void setIdUser1(int idUser1) {
		this.idUser1 = idUser1;
	}
	@Column(name = "idUser2")
	public int getIdUser2() {
		return this.idUser2;
	}

	public void setIdUser2(int idUser2) {
		this.idUser2 = idUser2;
	}
	@Column(name = "text")
	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

}