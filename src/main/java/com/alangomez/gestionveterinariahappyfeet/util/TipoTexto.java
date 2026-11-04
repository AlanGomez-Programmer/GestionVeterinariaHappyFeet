package com.alangomez.gestionveterinariahappyfeet.util;

import java.util.regex.Pattern;

public enum TipoTexto {
    // Solo para permitir letras y no números ni simbolos (Para nombres)
    SOLO_LETRAS(
            "^[\\p{L} ]+$", 
            "Solo se permiten letras"
    ),
    // Permite letras, signos (.,#/-) y números
    ALFANUMERICO(
            "^[\\p{L}0-9 .,#/-]+$",
            "Solo se permiten letras, números y los signos .,#/-"
    ),
    // Formato de correo: usuario@dominio.ext
    CORREO(
            "^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$",
            "El correo debe tener el formato usuario@dominio.com"
    ),
    // Solo para permitir Documentos
    DOCUMENTO_PERSONAL(
            "\\d+",
            "Solo se permiten numeros"
    ),
    // Formato de hora: HH:MM
    HORA(
            "^\\d{2}:\\d{2}$",
            "La hora debe tener el formato HH:MM"
    ),
    // Formato de enlace web: http://... o https://...
    URL(
            "^https?://\\S+$",
            "La URL debe iniciar con http:// o https:// y no tener espacios"
    );
 
    private final Pattern patron;
    private final String mensajeError;
    
    private TipoTexto(String patron, String mensajeError){
        this.patron = Pattern.compile(patron);
        this.mensajeError = mensajeError;
    }
    
    public boolean esValido(String texto){
        return texto != null && patron.matcher(texto).matches();
    }
    
    public String getMensajeError(){
        return mensajeError;
    }
}
