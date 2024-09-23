package usuario;

public abstract class Pessoa { // CLASSE ABSTRATA

    private String nome;
    private String senha;
    private String email;

    public Pessoa(String nome, String senha, String email) {
        this.nome = nome;
        this.senha = senha;
        this.email = email;
    }

    // Getters
    public String getNome() {
        return nome;
    }

    public String getSenha() {
        return senha;
    }

    public String getEmail() {
        return email;
    }

    // Setters
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setsenha(String senha) {
        this.senha = senha;
    }

    public void setemail(String email) {
        this.email = email;
    }

    // Método abstrato
    public abstract void exibirDetalhes();
}
