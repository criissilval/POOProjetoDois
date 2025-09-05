public class Email {
    public void notificarAguardandoPagamento(Cliente cliente, Pedido pedido){
        System.out.printf("Email para %s: Seu pedido #%d está aguardando pagamento.%n",
                cliente.getEmail(), pedido.getId());
    }

    public void notificarPagamentoAprovado(Cliente cliente, Pedido pedido){
        System.out.printf("E-mail para %s: Pagamento do pedido #%d aprovado.%n",
                cliente.getEmail(), pedido.getId());
    }

    public void notificarPedidoEntregue(Cliente cliente, Pedido pedido){
        System.out.printf("E-mail para %s: Pedido #%d foi entregue. Obrigado!%n",
                cliente.getEmail(), pedido.getId());
    }
}
