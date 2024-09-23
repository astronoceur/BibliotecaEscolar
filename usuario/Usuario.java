package usuario;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import biblioteca.GerenciamentodeBiblioteca;
import biblioteca.Livro;

public class Usuario extends Pessoa implements GerenciamentodeBiblioteca {
    private ArrayList<Livro> listaDeLivrosEmprestados;
    private String tipoDeUsuario;

    public Usuario(String nome, String senha, String email, String tipoDeUsuario) {
        super(nome, senha, email);
        this.tipoDeUsuario = tipoDeUsuario;
        this.listaDeLivrosEmprestados = new ArrayList<>();
    }

    public void setTipoDeUsuario(String tipoDeUsuario) {
        this.tipoDeUsuario = tipoDeUsuario;
    }
    public String getTipoDeUsuario(){
        return tipoDeUsuario;
    }

    // Getters e Setters para a lista de livros emprestados
    public ArrayList<Livro> getListaDeLivrosEmprestados() {
        return listaDeLivrosEmprestados;
    }

    public void setListaDeLivrosEmprestados(ArrayList<Livro> listaDeLivrosEmprestados) {
        this.listaDeLivrosEmprestados = listaDeLivrosEmprestados;
    }


    // Sobrescrita do método abstrato da classe Pessoa
    @Override
    public void exibirDetalhes() {
        System.out.println("Nome: " + getNome());
        System.out.println("senha: " + getSenha());
        System.out.println("Email: " + getEmail());
    }

    // Implementação dos métodos da interface GerenciamentodeBiblioteca
    @Override
    public void adicionarLivro(Livro livroEmprestado) {
        System.out.println("O LIVRO " + livroEmprestado.getNome() + " FOI EMPRESTADO POR " + getNome());
        listaDeLivrosEmprestados.add(livroEmprestado);
    }

    @Override
    public boolean removerLivro(Livro livroEmprestado) {
        boolean removed = listaDeLivrosEmprestados.remove(livroEmprestado);
        if (removed) {
            System.out.println("O LIVRO " + livroEmprestado.getNome() + " FOI DEVOLVIDO POR " + getNome());
        } else {
            System.out.println("O LIVRO " + livroEmprestado.getNome() + " NÃO FOI ENCONTRADO ENTRE OS EMPRESTADOS DE " + getNome());
        }
        return removed;
    }

    public void listarLivrosEmprestados() {
        System.out.println("Livros emprestados por " + getNome() + ":");
        for (Livro livro : listaDeLivrosEmprestados) {
            System.out.println("- " + livro.getNome());
        }
    }
   
    public String salvarUsuario() {
        try {
            FileWriter fw = new FileWriter("gui/users.txt", true);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(this.getNome()+","+this.getEmail() + "," + this.getSenha() + ",user");
            pw.flush();
            pw.close();
            fw.close();
            return "Usuário Salvo com o Sucesso!!";
        } catch (IOException ex) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            return "Erro ao escrever no arquivo de banco de dados: " + ex.getMessage();
        }
    }
}
