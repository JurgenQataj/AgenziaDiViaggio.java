package model.dao;

import java.sql.SQLException;

/**
 * Interfaccia generica per tutti i Data Access Object (DAO).
 * Definisce un contratto per l'esecuzione di un'operazione sul database.
 *
 * @param <T> Il tipo di dato che il DAO restituirà (es. List<Trip>, Integer, Void).
 */
public interface BaseDAO<T> {

    /**
     * Esegue l'operazione specifica del DAO sul database.
     * I parametri necessari per l'operazione vengono passati tramite il costruttore
     * della classe che implementa questa interfaccia, non tramite questo metodo.
     *
     * @return Il risultato dell'operazione, di tipo T.
     * @throws SQLException Se si verifica un errore durante l'interazione con il database.
     */
    T execute() throws SQLException;

}
