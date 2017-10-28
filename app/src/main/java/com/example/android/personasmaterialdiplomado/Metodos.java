package com.example.android.personasmaterialdiplomado;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by android on 07/10/2017.
 */

public class Metodos {
    public static int fotoAleatoria(ArrayList<Integer> fotos){
        int fotoSeleccionada;
        Random r = new Random();
        fotoSeleccionada = r.nextInt(fotos.size());
        return fotos.get(fotoSeleccionada);
    }

    public static boolean existencia_persona(ArrayList<Persona> personas, String cedula){
        for (int i = 0; i <personas.size() ; i++) {
            if(personas.get(i).getCedula().equals(cedula)){
                return true;
            }
        }
        return false;
    }
    public static boolean persona_editar(ArrayList<Persona> personas, String cedulaE, String cedulaActual){

        if (!cedulaActual.equals(cedulaE)) {
            if (existencia_persona(personas, cedulaE)) {
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }




}
