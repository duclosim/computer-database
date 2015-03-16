package com.excilys.computerDatabase.persistence;

import java.util.List;

import com.excilys.computerDatabase.model.CompanyBean;

/**
 * Cette interface rassemble des méthodes d'accès à la table company.
 * @author excilys
 *
 */
public interface CompanyDAO {
	/**
	 * Cette méthode retourne un CompanyBean dont on a donné 
	 *   l'identifiant en paramètre.
	 * @param id L'identifiant de la company recherchée.
	 * @return Un CompanyBean contenant les informations de la ligne 
	 *   correspondant à l'id donné en paramètre, null s'il ne correspond 
	 *   à aucune ligne.
	 */
	public CompanyBean getById(Long id);
	/**
	 * Cette méthode retourne tout le contenu de la table company 
	 *   sous forme de liste de CompanyBean.
	 * @return La List de tous les CompanyBean.
	 */
	public List<CompanyBean> getAll();
}
