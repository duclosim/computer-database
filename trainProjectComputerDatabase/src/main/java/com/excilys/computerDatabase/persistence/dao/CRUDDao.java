package main.java.com.excilys.computerDatabase.persistence.dao;

import java.util.List;

public interface CRUDDao<T> {
	/**
	 * Cette méthode enregistre un nouveau bean dans la table.
	 * @param entity Le nouveau bean à enregsitrer.
	 */
	void create(T entity);
	/**
	 * Cette méthode retourne un bean dont on a donné 
	 *   l'identifiant en paramètre.
	 * @param id L'identifiant du bean recherché.
	 * @return Un bean contenant les informations de la ligne 
	 *   correspondant à l'id donné en paramètre, null s'il ne correspond 
	 *   à aucune ligne.
	 */
	T getById(Long id);
	/**
	 * Cette méthode retourne tout le contenu de la table 
	 *   sous forme de liste de bean.
	 * @param limit Le nombre de beans à récupérer.
	 * @param offset Le numéro du bean à partir duquel commencer à lire.
	 * @return La List de tous les beans.
	 */
	List<T> getAll(int limit, int offset);
	/**
	 * Retourne le nombre de lignes dans la table.
	 * @return Le nombre total de lignes dans la table.
	 */
	int countLines();
	/**
	 * Cette méthode enregistre les modifications apportées à un 
	 *   bean dans la table.
	 * @param entity Le bean dont on doit enregistrer les modifications.
	 */
	void update(T entity);
	/**
	 * Cette méthode supprime un bean de la table.
	 * @param entity Le bean à supprimer.
	 */
	void delete(T entity);
}
