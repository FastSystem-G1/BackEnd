package com.fastsystem.login.fastsystem;
public class EmpresaMaquina {
    private Integer idEmpresa = 0;
    private Integer idMaquina = 0;
    private Integer idComponente = 0;
    private Integer idProcesso = 0;

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public Integer getIdMaquina() {
        return idMaquina;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public void setIdMaquina(Integer idMaquina) {
        this.idMaquina = idMaquina;
    } 

    public Integer getIdComponente() {
        return idComponente;
    }

    public void setIdComponente(Integer idComponente) {
        this.idComponente = idComponente;
    }

    public Integer getIdProcesso() {
        return idProcesso;
    }

    public void setIdProcesso(Integer idProcesso) {
        this.idProcesso = idProcesso;
    }
}
