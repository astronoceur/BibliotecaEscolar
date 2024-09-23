package gui;
import biblioteca.*;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import usuario.*;

public class Sistema{

    private static final String DATABASE_FILE = "gui/users.txt";
    private static final String DATABASE_BOOK = "livros.txt";

    static Funcionario funcionario = new Funcionario("ADM PADRÃO", "ADM", "ADM");

    private static void admRegister() {
        String nome = JOptionPane.showInputDialog("Digite seu nome:");
        String email = JOptionPane.showInputDialog("Digite seu E-mail:");
        String password = JOptionPane.showInputDialog("Digite sua senha:");

        String[] userTypes = { "user", "admin" };
        String userType = (String) JOptionPane.showInputDialog(null, "Selecione o tipo de usuário:", "Tipo de Usuário",
                JOptionPane.QUESTION_MESSAGE, null, userTypes, userTypes[0]);
                
        if (nome == null || email == null || password == null || nome.isEmpty() || email.isEmpty()|| password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "O nome, email e senha não podem estar vazios.");
            return;
        }


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATABASE_FILE, true))) {
            writer.write(nome + "," + email + "," + password + "," + userType);
            writer.newLine();
            JOptionPane.showMessageDialog(null, "Registro bem-sucedido!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao escrever no arquivo de banco de dados: " + e.getMessage());
        }
    }

    public static boolean login() {
        String email = JOptionPane.showInputDialog("Digite seu e-mail:");
        String password = JOptionPane.showInputDialog("Digite sua senha:");

        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "O nome de usuário e/ou senha não podem estar vazios.");
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(DATABASE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4 && parts[1].equals(email) && parts[2].equals(password)) {
                    String userType = parts[3];
                    if ("admin".equals(userType)) {
                        JOptionPane.showMessageDialog(null, "Login bem-sucedido! Bem-vindo, administrador.");
                        Usuario admin = new Usuario(parts[0], parts[2], parts[1],"admin");
                        adminMenu(admin);
                    } else if ("user".equals(userType)) {
                        JOptionPane.showMessageDialog(null, "Login bem-sucedido! Bem-vindo, usuário.");
                        Usuario usuario = new Usuario(parts[0], parts[2], parts[1],"user");
                        userMenu(usuario);
                    }
                    return true;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao ler o arquivo de banco de dados: " + e.getMessage());
        }

        JOptionPane.showMessageDialog(null, "Nome de usuário ou senha incorretos.");
        return false;
    }

    public static void register() {
        String nome = JOptionPane.showInputDialog("Digite seu nome:");
        String email = JOptionPane.showInputDialog("Digite seu E-mail:");
        String password = JOptionPane.showInputDialog("Digite sua senha:");

        if (nome == null || email == null || password == null || nome.isEmpty() || email.isEmpty()
                || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "O nome, email e senha não podem estar vazios.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(DATABASE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4 && parts[1].equals(email)) {
                    JOptionPane.showMessageDialog(null, "O e-mail utilizado já foi cadastrado no sistema.");
                    return;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao ler o arquivo de banco de dados: " + e.getMessage());
            return;
        }

        Usuario novoUsuario = new Usuario(nome, password, email,"user");
        String resultado = novoUsuario.salvarUsuario();
        JOptionPane.showMessageDialog(null, resultado);
    }

    private static void Livroregister() {
        String nome = JOptionPane.showInputDialog("Digite o nome do livro:");
        String datadepublicacao = JOptionPane.showInputDialog("Data da publicação:");
        String autor = JOptionPane.showInputDialog("Autor do livro:");

        if (nome == null || datadepublicacao == null || autor == null || nome.isEmpty() || datadepublicacao.isEmpty()
                || autor.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Os campos não foram preenchidos corretamente, tente novamente.");
            return;
        }

        Livro novoLivro = new Livro(nome, datadepublicacao, autor, "Livro_Disponivel");
        String resultado = novoLivro.salvarLivro();
        JOptionPane.showMessageDialog(null, resultado);
    }

    public static void LivroDeletar() {
        
        List<Livro> livrosDisponiveis = new ArrayList<>();
        List<String> livrosEmprestados = new ArrayList<>();
    
        try (BufferedReader reader = new BufferedReader(new FileReader(DATABASE_BOOK))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(",");
                if (dados.length >= 5 && dados[3].equals("Livro_Disponivel")) {
                    Livro livroD = new Livro(dados[0], dados[1], dados[2], dados[3]);
                    livrosDisponiveis.add(livroD);
                } else {
                    livrosEmprestados.add(linha); 
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao ler o arquivo de livros.");
            return;
        }
        
        
        if (livrosDisponiveis.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum livro cadrastado na biblioteca nesse\n momento pode ser apagado.");
            return;
        }


        String listaDeLivrosDisponiveis = "Lista de Livros Disponíveis:\n\n";
        for (int i = 0; i < livrosDisponiveis.size(); i++) {
            Livro livro = livrosDisponiveis.get(i);
            listaDeLivrosDisponiveis += (i + 1) + ". " + livro.getNome() + " - " + livro.getDatadepublicacao() + " - "
                    + livro.getAutor() + "\n";
        }
        String input = JOptionPane.showInputDialog(listaDeLivrosDisponiveis + "\nDigite o número do livro a ser deletado:");
    
        if (input == null || input.isEmpty()) {
            JOptionPane.showMessageDialog(null, "A entrada não pode estar vazia.");
            return;
        }
    
        int numeroDoLivro;
        try {
            numeroDoLivro = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Entrada inválida.");
            return;
        }
    
        if (numeroDoLivro < 1 || numeroDoLivro > livrosDisponiveis.size()) {
            JOptionPane.showMessageDialog(null, "Número do livro inválido.");
            return;
        }
    
        livrosDisponiveis.remove(numeroDoLivro - 1);
    
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATABASE_BOOK))) {
            
            for (Livro livro : livrosDisponiveis) {
                writer.write(livro.getNome() + "," + livro.getDatadepublicacao() + "," + livro.getAutor() + ","
                        + livro.getEstado_do_livro() + "," + null);
                writer.newLine();
            }
            
            for (String livroEmprestado : livrosEmprestados) {
                writer.write(livroEmprestado);
                writer.newLine();
            }
            JOptionPane.showMessageDialog(null, "Livro deletado com sucesso!");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao salvar o arquivo de livros.");
        }
    }

    public static void LivroListar() {
        List<Livro> livros = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(DATABASE_BOOK))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(",");
                if (dados.length >= 5 && dados[3].equals("Livro_Disponivel") ) {
                    Livro livro = new Livro(dados[0], dados[1], dados[2], dados[3]);
                    livros.add(livro);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao ler o arquivo de livros.");
            return;
        }

        
        List<Livro> livrosE = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(DATABASE_BOOK))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(",");
                if (dados.length >= 5 && dados[3].equals("Livro_Emprestado")) {
                    
                    Livro livroE = new Livro(dados[0], dados[1], dados[2], dados[3]);
                    livrosE.add(livroE);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao ler o arquivo de livros.");
            return;
        }

        String listaDeLivrosEmprestados = "\n-> Lista de Livros Emprestados: \n\n";

        for (int i = 0; i < livrosE.size(); i++) {
            Livro livro = livrosE.get(i);
            listaDeLivrosEmprestados += (i + 1) + ". " + livro.getNome() + " - " + livro.getDatadepublicacao() + " - "
                    + livro.getAutor() + "\n";
        }

        String listaDeLivros = "-> Lista de Livros Disponiveis:\n\n";
        for (int i = 0; i < livros.size(); i++) {
            Livro livro = livros.get(i);
            listaDeLivros += (i + 1) + ". " + livro.getNome() + " - " + livro.getDatadepublicacao() + " - "
                    + livro.getAutor()+ "\n";
        }

        JOptionPane.showMessageDialog(null, listaDeLivros + listaDeLivrosEmprestados);
    }
    
    public static void LivroEmprestar(Usuario usuario) {
        List<Livro> livrosD = new ArrayList<>();

        // Ler todos os livros disponíveis do arquivo
        try (BufferedReader reader = new BufferedReader(new FileReader(DATABASE_BOOK))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(",");
                if (dados.length >= 4 && dados[3].equals("Livro_Disponivel")) {
                    Livro livroD = new Livro(dados[0], dados[1], dados[2], dados[3]);
                    livrosD.add(livroD);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao ler o arquivo de livros.");
            return;
        }

        if (livrosD.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum livro disponível para empréstimo.");
            return;
        }

        // Exibir lista de livros disponíveis para o usuário
        StringBuilder listaDeLivrosDisponiveis = new StringBuilder("Lista de Livros Disponíveis:\n");
        for (int i = 0; i < livrosD.size(); i++) {
            Livro livro = livrosD.get(i);
            listaDeLivrosDisponiveis.append(i + 1).append(". ").append(livro.getNome()).append(" - ")
                    .append(livro.getDatadepublicacao()).append(" - ").append(livro.getAutor()).append("\n");
        }

        String input = JOptionPane.showInputDialog(listaDeLivrosDisponiveis + "\nDigite o número do livro a ser emprestado:");

        if (input == null || input.isEmpty()) {
            JOptionPane.showMessageDialog(null, "A entrada não pode estar vazia.");
            return;
        }

        int numeroDoLivro;
        try {
            numeroDoLivro = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Entrada inválida.");
            return;
        }

        if (numeroDoLivro < 1 || numeroDoLivro > livrosD.size()) {
            JOptionPane.showMessageDialog(null, "Número do livro inválido.");
            return;
        }

        Livro livroEmprestado = livrosD.get(numeroDoLivro - 1);

        // Ler todo o arquivo e atualizar o status do livro emprestado
        List<String> linhasAtualizadas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DATABASE_BOOK))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(",");
                if (dados.length >= 4 && dados[0].equals(livroEmprestado.getNome()) &&
                        dados[1].equals(livroEmprestado.getDatadepublicacao()) &&
                        dados[2].equals(livroEmprestado.getAutor())) {
                    linhasAtualizadas.add(dados[0] + "," + dados[1] + "," + dados[2] + ",Livro_Emprestado," + usuario.getNome());
                } else {
                    linhasAtualizadas.add(linha);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao ler o arquivo de livros.");
            return;
        }

        // Escrever de volta o conteúdo atualizado no arquivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATABASE_BOOK))) {
            for (String linha : linhasAtualizadas) {
                writer.write(linha);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao atualizar o arquivo de livros.");
            return;
        }

        JOptionPane.showMessageDialog(null, "Livro emprestado com sucesso!");
    }


    public static void listaUsuarios(){
        List<Usuario> usuarios = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(DATABASE_FILE))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(",");
                if (dados.length >= 3 && dados[3].equals("user") ) {
                    Usuario usuario = new Usuario (dados[0], dados[2], dados[1], dados[3]);
                    usuarios.add(usuario);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao ler o arquivo de livros.");
            return;
        }
        List<Usuario> funcionarios = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(DATABASE_FILE))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(",");
                if (dados.length >= 3 && dados[3].equals("admin") ) {
                    Usuario funcionario = new Usuario (dados[0], dados[2], dados[1],dados[3]);
                    funcionarios.add(funcionario);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao ler o arquivo de livros.");
            return;
        }

        String listaDeUsuarios = "->Lista de Usuarios: \n\n";

        for (int i = 0; i < usuarios.size(); i++) {
            Usuario usuario = usuarios.get(i);
            listaDeUsuarios += (i + 1) + ". " + usuario.getNome() + " - " + usuario.getEmail() + "\n";
        }

        String listaDeFuncioarios = "\n->Lista de Funcionarios:\n\n";
        for (int i = 0; i < funcionarios.size(); i++) {
            Usuario funcionario = funcionarios.get(i);
            listaDeFuncioarios += (i + 1) + ". " + funcionario.getNome() + " - " + funcionario.getEmail() + "\n";
        }

        JOptionPane.showMessageDialog(null, listaDeFuncioarios + listaDeUsuarios);



    }

    public static void DevolverLivro(Usuario usuario){
        List<Livro> livrosEmprestados = new ArrayList<>();

        // Ler todos os livros disponíveis do arquivo
        try (BufferedReader reader = new BufferedReader(new FileReader(DATABASE_BOOK))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(",");
                if (dados.length >= 4 && dados[4].equals(usuario.getNome())) {
                    Livro livroEmprestado = new Livro(dados[0], dados[1], dados[2], dados[3]);
                    livrosEmprestados.add(livroEmprestado);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao ler o arquivo de livros.");
            return;
        }

        if (livrosEmprestados.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum livro disponível para empréstimo.");
            return;
        }

        String listaDeLivrosEmprestados = "Lista de Livros Emprestados: \n\n";

        for (int i = 0; i < livrosEmprestados.size(); i++) {
            Livro livro = livrosEmprestados.get(i);
            listaDeLivrosEmprestados += (i + 1) + ". " + livro.getNome() + " - " + livro.getDatadepublicacao() + " - "
                    + livro.getAutor() + "\n";
        }

    
            String input = JOptionPane.showInputDialog(listaDeLivrosEmprestados + "\nDigite o número do livro para ser devolvido:");
            if (input == null || input.isEmpty()) {
                JOptionPane.showMessageDialog(null, "A entrada não pode estar vazia.");
                return;
            }
    
            int numeroDoLivro;
            try {
                numeroDoLivro = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Entrada inválida.");
                return;
            }
    
            if (numeroDoLivro < 1 || numeroDoLivro > livrosEmprestados.size()) {
                JOptionPane.showMessageDialog(null, "Número do livro inválido.");
                return;
            }
    
            Livro livroEmprestado = livrosEmprestados.get(numeroDoLivro - 1);
    
            // Ler todo o arquivo e atualizar o status do livro emprestado
            List<String> linhasAtualizadas = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(DATABASE_BOOK))) {
                String linha;
                while ((linha = reader.readLine()) != null) {
                    String[] dados = linha.split(",");
                    if (dados.length >= 4 && dados[0].equals(livroEmprestado.getNome()) &&
                            dados[1].equals(livroEmprestado.getDatadepublicacao()) &&
                            dados[2].equals(livroEmprestado.getAutor())) {
                        linhasAtualizadas.add(dados[0] + "," + dados[1] + "," + dados[2] + ",Livro_Disponivel"+","+null);
                    } else {
                        linhasAtualizadas.add(linha);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro ao ler o arquivo de livros.");
                return;
            }
    
            // Escrever de volta o conteúdo atualizado no arquivo
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATABASE_BOOK))) {
                for (String linha : linhasAtualizadas) {
                    writer.write(linha);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro ao atualizar o arquivo de livros.");
                return;
            }
    
            JOptionPane.showMessageDialog(null, "Livro devolvido com sucesso!");
        }


        public static void ListarLivrosEmprestados(Usuario usuario){
            List<Livro> livrosEmprestados = new ArrayList<>();

        // Ler todos os livros disponíveis do arquivo
        try (BufferedReader reader = new BufferedReader(new FileReader(DATABASE_BOOK))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(",");
                if (dados.length >= 4 && dados[4].equals(usuario.getNome())) {
                    Livro livroEmprestado = new Livro(dados[0], dados[1], dados[2], dados[3]);
                    livrosEmprestados.add(livroEmprestado);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao ler o arquivo de livros.");
            return;
        }

        if (livrosEmprestados.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum livro emprestado pelo usuário.");
            return;
        }

        String listaDeLivrosEmprestados = "-> Lista de Livros Emprestados: \n";

        for (int i = 0; i < livrosEmprestados.size(); i++) {
            Livro livro = livrosEmprestados.get(i);
            listaDeLivrosEmprestados += (i + 1) + ". " + livro.getNome() + " - " + livro.getDatadepublicacao() + " - "
                    + livro.getAutor() + "\n";
        }

        JOptionPane.showMessageDialog(null, listaDeLivrosEmprestados);

        }



    public static void informacoesAdmin(Usuario admin){
        String informacoes = "-> Informações do usuário\n"+ "\nNome: "+ admin.getNome() + "\n"
                                                         +"E-mail: "+admin.getEmail() + "\n"
                                                         +"Senha: "+admin.getSenha();

    
        JOptionPane.showMessageDialog(null, informacoes);

    }
    public static void trocaSenhaUsuario(Usuario usuario){
        String password = JOptionPane.showInputDialog("Digite sua nova senha:");

        if (password == null || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Operação cancelada ou senha inválida.");
            return;
        }

        usuario.setsenha(password);

        List<String> linhas = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(DATABASE_FILE))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(",");
                if (dados.length >= 4 && dados[1].equals(usuario.getEmail())) {
                    dados[2] = password;
                    linha = dados[0] + "," + dados[1] + "," + dados[2] + "," + dados[3];
                }
                linhas.add(linha);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao ler o arquivo de banco de dados.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATABASE_FILE))) {
            for (String linha : linhas) {
                writer.write(linha);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao escrever no arquivo de banco de dados.");
            return;
        }

        JOptionPane.showMessageDialog(null, "Senha atualizada com sucesso.");
    }

    public static void excluiUsuarios(Usuario admin){

        List<Usuario> usuarios = new ArrayList<>();
        List<String> usuariosEmprestando = new ArrayList<>();
        List<Livro> livrosEmprestados = new ArrayList<>();
    
        try (BufferedReader readerUsuarios = new BufferedReader(new FileReader(DATABASE_FILE));
             BufferedReader readerLivros = new BufferedReader(new FileReader(DATABASE_BOOK))) {
    
            // Lendo todos os usuários
            String linha;
            while ((linha = readerUsuarios.readLine()) != null) {
                String[] dados = linha.split(",");
                if (dados.length >= 4) {
                    Usuario usuario = new Usuario(dados[0], dados[2], dados[1], dados[3]);
                    usuarios.add(usuario);
                }
            }
    
            // Lendo todos os livros emprestados
            while ((linha = readerLivros.readLine()) != null) {
                String[] dados = linha.split(",");
                if (dados.length >= 5 && dados[3].equals("Livro_Emprestado")) {
                    Livro livro = new Livro(dados[0], dados[2], dados[1], dados[3]);
                    livrosEmprestados.add(livro);
                    usuariosEmprestando.add(dados[4]);
                }
            }
    
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao ler os arquivos.");
            return;
        }
    
        if (usuarios.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum usuário cadastrado no sistema.");
            return;
        }
    
        String listaDeUsuarios = "Lista de Usuários:\n\n";
        List<Usuario> usuariosDisponiveisParaExclusao = new ArrayList<>();
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario usuario = usuarios.get(i);
            if (!usuariosEmprestando.contains(usuario.getNome()) && !usuario.getEmail().equals(admin.getEmail())) {
                usuariosDisponiveisParaExclusao.add(usuario);
                listaDeUsuarios += (usuariosDisponiveisParaExclusao.size()) + ". " + usuario.getNome() + " - " + usuario.getEmail() + " - " + usuario.getTipoDeUsuario() + "\n";
            }
        }
    
        if (usuariosDisponiveisParaExclusao.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum usuário disponível para exclusão.");
            return;
        }
    
        String input = JOptionPane.showInputDialog(listaDeUsuarios + "\nDigite o número do usuário a ser deletado:");
    
        if (input == null || input.isEmpty()) {
            JOptionPane.showMessageDialog(null, "A entrada não pode estar vazia.");
            return;
        }
    
        int numeroDoUsuario;
        try {
            numeroDoUsuario = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Entrada inválida.");
            return;
        }
    
        if (numeroDoUsuario < 1 || numeroDoUsuario > usuariosDisponiveisParaExclusao.size()) {
            JOptionPane.showMessageDialog(null, "Número do usuário inválido.");
            return;
        }
    
        Usuario usuarioAExcluir = usuariosDisponiveisParaExclusao.get(numeroDoUsuario - 1);
        usuarios.remove(usuarioAExcluir);
    
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATABASE_FILE))) {
            for (Usuario usuario : usuarios) {
                writer.write(usuario.getNome() + "," + usuario.getEmail() + "," + usuario.getSenha() + "," + usuario.getTipoDeUsuario());
                writer.newLine();
            }
            JOptionPane.showMessageDialog(null, "Usuário deletado com sucesso!");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao salvar o arquivo de usuários.");
        }
    }
        

    // MENUS

    private static void userMenu(Usuario usuario) {
        while (true) {
            String[] options = { "Área da Biblioteca", "Informações", "Trocar conta", "Sair" };
            int opcao = JOptionPane.showOptionDialog(null, "O que deseja fazer "+usuario.getNome()+ "?\n", "Menu Inicial - Usuário",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            switch (opcao) {
                case 0:
                    userBiblioteca(usuario);
                    break;
                case 1:
                    mostarInformacoes(usuario);
                    break;
                case 2:
                    loginMenu();
                case 3:
                    System.exit(0);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida. Por favor, tente novamente.");
                    break;
            }
        }
    }

    private static void adminMenu(Usuario admin) {
        while (true) {
            String[] options = { "Área de Cadastro", "Área da Biblioteca", "Informações", "Trocar conta", "Sair" };
            int opcao = JOptionPane.showOptionDialog(null, "O que deseja fazer "+admin.getNome()+ "?\n", "Menu Inicial - Administrador",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            switch (opcao) {
                case 0:
                    escolhaDeRegistro(admin);
                    break;
                case 1:
                    adminBiblioteca();
                    break;
                case 2:
                    informacoesAdmin(admin);
                    break;
                case 3:
                    loginMenu();
                case 4:
                    System.exit(0);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida. Por favor, tente novamente.");
                    break;
            }
        }
    }

    private static void escolhaDeRegistro(Usuario admin) {
        String[] register = { "Cadastro de funcionário/usuário", "Lista de usuários", "Excluir usuários", "Voltar" };
        int opRegister = JOptionPane.showOptionDialog(null, "Que tipo de usuário deseja cadastrar?", "Área de cadastro",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, register, register[0]);

        switch (opRegister) {
            case 0:
                admRegister();
                break;
            case 1:
                listaUsuarios();
                break;
            case 2:
                excluiUsuarios(admin);
                break;
            case 3:
                break;
            default:
                JOptionPane.showMessageDialog(null, "Opção inválida. Por favor, tente novamente.");
                break;
        }
    }

    private static void userBiblioteca(Usuario usuario) {
        String[] biblioteca = { "Emprestar livro", "Devolver livro", "Lista de livros emprestados", "Voltar" };
        int opBiblioteca = JOptionPane.showOptionDialog(null, "Como usuário, você tem as seguintes opções: ",
                "Área da biblioteca",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, biblioteca, biblioteca[0]);
        switch (opBiblioteca) {
            case 0:
                LivroEmprestar(usuario);
                break;
            case 1:
                DevolverLivro(usuario);
                break;
            case 2:
                ListarLivrosEmprestados(usuario);
                break;
            case 3:
                break;
            default:
                JOptionPane.showMessageDialog(null, "Opção inválida. Por favor, tente novamente.");
                break;
        }
    }

    private static void adminBiblioteca() {
        String[] biblioteca = { "Adicionar livros", "Remover livros", "Lista de livros", "Voltar" };
        int opBiblioteca = JOptionPane.showOptionDialog(null, "Como funcionário, você tem as seguintes opções: ",
                "Área da biblioteca",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, biblioteca, biblioteca[0]);
        switch (opBiblioteca) {
            case 0:
                Livroregister();
                break;
            case 1:
                LivroDeletar();
                break;
            case 2:
                LivroListar();
                break;
            case 3:
                break;
            default:
                JOptionPane.showMessageDialog(null, "Opção inválida. Por favor, tente novamente.");
                break;
        }
    }

    public static void loginMenu() {
        while (true) {
            String[] loginOptions = { "Login", "Registro", "Sair" };
            int loginChoice = JOptionPane.showOptionDialog(null, "Escolha uma opção:", "Login/Registro",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, loginOptions, loginOptions[0]);

            if (loginChoice == 0) {
                if (login()) {
                    break;
                } else {
                    JOptionPane.showMessageDialog(null, "Login falhou. Por favor, tente novamente.");
                }
            } else if (loginChoice == 1) {
                register();
            } else if (loginChoice == 2) {
                System.exit(0);
            }
        }

    }

    public static void mostarInformacoes(Usuario usuario){
        String informacoes = "-> Informações do usuário\n"+ "\nNome: "+ usuario.getNome() + "\n"
        +"E-mail: "+usuario.getEmail() + "\n"
        +"Senha: "+usuario.getSenha();

        String[] loginOptions = { "Trocar senha", "Voltar"};

        int loginChoice = JOptionPane.showOptionDialog(null, informacoes, "Login/Registro",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, loginOptions, loginOptions[0]);


    
        switch (loginChoice) {
            case 0:
                trocaSenhaUsuario(usuario);
                break;
            case 1:
                break;
            default:
                break;
        }


    }
}