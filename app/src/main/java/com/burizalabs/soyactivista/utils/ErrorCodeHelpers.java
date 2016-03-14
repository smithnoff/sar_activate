package com.burizalabs.soyactivista.utils;

/**
 * Created by Brahyam on 29/12/2015.
 */
public class ErrorCodeHelpers {

    public ErrorCodeHelpers(){

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
            case 116:
                message = "El objeto que trata de cargar es demasiado grande.";
                break;
            case 119:
                message = "La operación no está permitida para clientes. Contacte a un administrador";
                break;
            case 122:
                message = "Nombre del archivo inválido. Sólo debe contener caracteres a-zA-Z0-9_. y un tamaño entre 1 y 128 caracteres.";
                break;
            case 123:
                message = "Usted no posee los permisos adecuados para realizar esta acción.";
                break;
            case 124:
                message = "El tiempo de su solicitud ha vencido. Intente de nuevo más tarde o contacte a un administrador su el problema persiste.";
                break;
            case 125:
                message = "La dirección de email que indicó es invalida";
                break;
            case 137:
                message = "Está intentando insertar un objeto duplicado.";
                break;
            case 140:
                message = "Cuota del servidor excedida, por favor contacte a un administrador";
                break;
            case 155:
                message = "Solicitudes del servidor excedida, por favor contacte a un administrador";
                break;
            case 202:
                message = "El nombre de usuario ya se encuentra en uso";
                break;
            case 203:
                message = "El email ya se encuentra en uso";
                break;
            case 205:
                message = "El email no se encuentra asociado a ninguna cuenta.";
                break;
            default:
                message = "Ha ocurrido un error, por favor intente de nuevo más tarde.";
                break;
        }

        return message;
    }

    public static String resolveLogErrorString(int errorCode, String errorMessage){
        return "Error "+errorCode+": "+errorMessage;
    }
}
