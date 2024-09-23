package biblioteca;

import usuario.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Livro {
    private String nome;
    private String datadepublicacao;
    private String autor;
    private String Estado_do_livro;
    private Usuario Usuario_Emprestador;
    private String data_de_devolucao;

    public Livro(String nome, String datadepublicacao, String autor, String Estado_do_livro) {
        this.nome = nome;
        this.datadepublicacao = datadepublicacao;
        this.autor = autor;
        this.Estado_do_livro = Estado_do_livro;
    }

    // Getters
    public String getNome() {
        return nome;
    }

    public String getDatadepublicacao() {
        return datadepublicacao;
    }

    public String getAutor() {
        return autor;
    }

    public String getEstado_do_livro() {
        return Estado_do_livro;
    }

    public Usuario getUsuario_Emprestador() {
        return Usuario_Emprestador;
    }

    public String getData_de_devolucao() {
        return data_de_devolucao;
    }

    // Setters
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDatadepublicacao(String datadepublicacao) {
        this.datadepublicacao = datadepublicacao;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setEstado_do_livro(String Estado_do_livro) {
        this.Estado_do_livro = Estado_do_livro;
    }

    public void setUsuario_Emprestador(Usuario Usuario_Emprestador) {
        this.Usuario_Emprestador = Usuario_Emprestador;
    }

    public void setData_de_devolucao(String data_de_devolucao) {
        this.data_de_devolucao = data_de_devolucao;
    }


        public String salvarLivro() {
        try {
            FileWriter fw = new FileWriter("livros.txt", true);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(this.getNome() + "," + this.getDatadepublicacao() + "," + this.getAutor() + "," + "Livro_Disponivel"+","+ null);
            pw.flush();
            pw.close();
            fw.close();
            return "LIVRO SALVO COM SUCESSO :)";
        } catch (IOException ex) {
            Logger.getLogger(Livro.class.getName()).log(Level.SEVERE, null, ex);
            return "Erro ao escrever no arquivo de banco de dados: " + ex.getMessage();
        }
    }
}
