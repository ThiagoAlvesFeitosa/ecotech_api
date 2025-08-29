package br.com.fiap.ecotech.ecotech_api.model;

/*
 * [Eu explicando]
 * Essa enum é só pra representar o "papel" do usuário no sistema.
 * Por que criar? Porque é melhor do que usar String solta (evita erro de digitação).
 * Valores possíveis: USER (app) e ADMIN (dashboard Angular).
 */
public enum Role {
    USER,
    ADMIN
}
