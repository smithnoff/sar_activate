package logica;

import java.util.Date;

/**
 * Created by Luis Adrian on 16/12/2015.
 */
public class Conversacion {
    /** The date. */
    private Date date;
    /** The sender. */
    private String usuario1;
    /** The receiver. */
    private String usuario2;

    /** The Constant STATUS_SENDING. */
    public static final int STATUS_SENDING = 0;

    /** The Constant STATUS_SENT. */
    public static final int STATUS_SENT = 1;

    /** The Constant STATUS_FAILED. */
    public static final int STATUS_FAILED = 2;

    /** The status. */
    private int status = STATUS_SENT;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUsuario1() {
        return usuario1;
    }

    public void setUsuario1(String usuario1) {
        this.usuario1 = usuario1;
    }

    public String getUsuario2() {
        return usuario2;
    }

    public void setUsuario2(String usuario2) {
        this.usuario2 = usuario2;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Checks if is sent.
     *
     * @return true, if is sent
     */
    /*
    public boolean isSent()
    {
        return UserListActivity.user.getUsername().equals(sender);
    }
    */

    //Aqui deberia estar el modelo de mensajeDirectos y colocar el texto del mensaje como parametro
    public Conversacion(Date date, String sender)
    {
        this.date = date;
        this.usuario1 = sender;
    }

    /**
     * Instantiates a new conversation.
     */
    public Conversacion()
    {
    }

}
