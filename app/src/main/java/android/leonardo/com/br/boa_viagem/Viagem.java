package android.leonardo.com.br.boa_viagem;

/**
 * Created by Amministratore on 20/08/2017.
 */

public class Viagem {

    private int id;
    private String destino;
    private int tipoViagem;
    private String dataChegada;
    private String  dataSaida;
    private double orcamento;
    private int  quantidadePessoas;
    public Viagem(){}
    public Viagem(int  id, String destino, int tipoViagem,
                  String dataChegada, String dataSaida, double orcamento,
                  int  quantidadePessoas) {
        this.id = id;
        this.destino = destino;
        this.tipoViagem = tipoViagem;
        this.dataChegada = dataChegada;
        this.dataSaida = dataSaida;
        this.orcamento = orcamento;
        this.quantidadePessoas = quantidadePessoas;
    }

    public Viagem( String destino, int tipoViagem,
                   String dataChegada, String dataSaida, Double orcamento,
                   int  quantidadePessoas) {

        this.destino = destino;
        this.tipoViagem = tipoViagem;
        this.dataChegada = dataChegada;
        this.dataSaida = dataSaida;
        this.orcamento = orcamento;
        this.quantidadePessoas = quantidadePessoas;
    }

    public int  getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getDestino(){return  destino;}

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public int  getTipoViagem() {
        return tipoViagem;
    }

    public void setTipoViagem(int  tipoViagem) {
        this.tipoViagem = tipoViagem;
    }

    public String  getDataChegada() {
        return dataChegada;
    }

    public void setDataChegada(String dataChegada) {
        this.dataChegada = dataChegada;
    }

    public String getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(String dataSaida) {
        this.dataSaida = dataSaida;
    }

    public double getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(double orcamento) {
        this.orcamento = orcamento;
    }

    public int  getQuantidadePessoas() {
        return quantidadePessoas;
    }

    public void setQuantidadePessoas(int quantidadePessoas) {
        this.quantidadePessoas = quantidadePessoas;
    }
}
