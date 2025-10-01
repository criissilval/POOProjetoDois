import java.util.Locale;

public class Cliente {
    private String nome;
    private final String documento;
    private String email;

    public Cliente(String nome, String documento, String email) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        if (documento == null || documento.isBlank()) {
            throw new IllegalArgumentException("Documento é obrigatório");
        }

        String apenasDigitos = documento.replaceAll("\\D", "");
        if (apenasDigitos.length() != 11) {
            throw new IllegalArgumentException("Documento deve ter exatamente 11 dígitos.");
        }

        this.nome = nome.trim();
        this.documento = documento;
        setEmail(email);
    }
    //get e set
    public String getDocumento() {return documento;}

    public String getNome() {return nome;}
    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        this.nome = nome.trim();
    }

    public String getEmail() {return email;}
    public void setEmail(String email) {
        String normalized = normalizeEmail(email);
        if (!isValidEmail(normalized)) {
            throw new IllegalArgumentException("Email inválido");
        }
        this.email = normalized;
    }

    public String getDocumentoMascarado() {
        if (documento == null || documento.isBlank()) return "***";
        String digits = documento.replaceAll("\\D", "");
        if (digits.length() < 5) return "***";
        String inicio = digits.substring(0, 3);
        String fim = digits.substring(digits.length() - 2);
        String meio = "*".repeat(digits.length() - 5);
        return inicio + meio + fim;
    }

    private String normalizeEmail(String email) {
        if (email == null) return null;
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.isBlank()) return false;
        if (email.contains(" ") || email.indexOf('@') != email.lastIndexOf('@')) return false;
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!email.matches(regex)) return false;
        String[] parts = email.split("@", 2);
        String local = parts[0];
        String domain = parts[1];
        if (local.contains("..") || domain.contains("..")) return false;
        if (domain.startsWith("-") || domain.endsWith("-")) return false;
        for (String label : domain.split("\\.")) {
            if (label.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String s = String.format(
                "%sCliente%s | Nome: %s%s%s | Documento: %s%s%s | E-mail: %s%s%s",
                ConsoleColors.BRIGHT_CYAN, ConsoleColors.RESET,
                ConsoleColors.YELLOW, nome, ConsoleColors.RESET,
                ConsoleColors.MAGENTA, getDocumentoMascarado(), ConsoleColors.RESET,
                ConsoleColors.BLUE, email, ConsoleColors.RESET
        );
        return ConsoleColors.clean(s);
    }


}
