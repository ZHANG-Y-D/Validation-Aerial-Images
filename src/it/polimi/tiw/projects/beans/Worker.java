package it.polimi.tiw.projects.beans;

public class Worker extends User {
    private String level;
    private String foto;

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
