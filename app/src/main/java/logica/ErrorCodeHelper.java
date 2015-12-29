package logica;

/**
 * Created by Brahyam on 29/12/2015.
 */
public class ErrorCodeHelper {

    public ErrorCodeHelper(){

    }

    // Return a readable message in spanish from an error code.
    public static String resolveErrorCode(int code){
        String message;

        switch (code){
            case 100:
                message = "La conexión con el servidor falló, por favor intente de nuevo más tarde.";
                break;
            case 101:
                message = "No se encontró la entidad solicitada.";
                break;
            case 202:
                message = "El nombre de usuario ya está en uso.";
                break;
            default:
                message = "Ha ocurrido un error, por favor intente de nuevo más tarde.";
                break;
        }

        return message;
    }
}
