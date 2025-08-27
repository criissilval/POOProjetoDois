public class Cliente {
    private String nome;
    private final String documento;
    private String email;

    public Cliente(String nome, String documento, String email) throws IllegalAccessException {
        if (documento == null || documento.isBlank()) {
            throw new IllegalArgumentException("Documento é obrigatório");
        }

        String apenasDigitos = documento.replaceAll("\\D", "");
        if (apenasDigitos.length() != 11) {
            throw new IllegalArgumentException("Documento deve ter exatamente 11 dígitos.");
        }


        this.nome = nome;// Java
        this.documento = documento;
        this.email = email;
    }
    //get e set
    public String getDocumento() {return documento;}

    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    @Override
    public String toString() {
        return "Cliente{" +
                "documento ='" + documento + '\'' +
                ", nome ='" + nome + '\'' +
                ", email ='" + email + '\'' +
                '}';
    }

}
