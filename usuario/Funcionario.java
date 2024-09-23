package usuario;

public class Funcionario extends Pessoa {

    public Funcionario(String nome, String senha, String email) {
        super(nome, senha, email);
    }

    @Override
    public void exibirDetalhes() {
        System.out.println("Nome: " + getNome());
        System.out.println("senha: " + getSenha());
        System.out.println("Email: " + getEmail());
    }
}
