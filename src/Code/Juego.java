package Code;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author OMy
 */
public class Juego {
     private String nombre;
    private String genero;
    private String desarrollador;
    private String fechaLanzamiento;
    private String gamepath;

    public Juego(String nombre, String genero, String desarrollador, String fechaLanzamiento, String path) {
        this.nombre = nombre;
        this.genero = genero;
        this.desarrollador = desarrollador;
        this.fechaLanzamiento = fechaLanzamiento;
        this.gamepath = path;
    }

    public void savegame(RandomAccessFile raf) throws IOException {
        raf.writeUTF(nombre);
        raf.writeUTF(genero);
        raf.writeUTF(desarrollador);
        raf.writeUTF(fechaLanzamiento);
        raf.writeUTF(gamepath);
    }

    public static Juego loadgame(RandomAccessFile raf) throws IOException {
        String nombre = raf.readUTF();
        String genero = raf.readUTF();
        String desarrollador = raf.readUTF();
        String fechaLanzamiento = raf.readUTF();
        String rutaInstalacion = raf.readUTF();
        return new Juego(nombre, genero, desarrollador, fechaLanzamiento, rutaInstalacion);
    }
    
    public String getNombre(){
        return nombre;
    }

    public String getGenero() {
        return genero;
    }

    public String getDesarrollador() {
        return desarrollador;
    }

    public String getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    public String getGamepath() {
        return gamepath;
    }
    
}
