package com.excilys.computerDatabase.persistence;

import java.util.List;

import com.excilys.computerDatabase.model.ComputerBean;

/**
 * Cette interface rassemble des méthodes d'accès à la table computer.
 * @author excilys
 *
 */
public interface ComputerDAO {
	/**
	 * Cette méthode retourne un ComputerBean dont on a donné 
	 *   l'identifiant en paramètre.
	 * @param id L'identifiant du computer recherché.
	 * @return Un ComputerBean contenant les informations de la ligne 
	 *   correspondant à l'id donné en paramètre, null s'il ne correspond 
	 *   à aucune ligne.
	 */
	public ComputerBean getById(Long id);
	/**
	 * Cette méthode retourne tout le contenu de la table computer 
	 *   sous forme de liste de ComputerBean.
	 * @return La List de tous les ComputerBean.
	 */
	public List<ComputerBean> getAll();
	/**
	 * Cette méthode enregistre un nouveau computerBean dans la table computer.
	 * @param computerBean Le nouveau bean à enregsitrer.
	 */
	public void createComputer(ComputerBean computerBean);
	/**
	 * Cette méthode enregistre les modifications apportées à un 
	 *   computerBean dans la table computer.
	 * @param computerBean Le bean dont on doit enregistrer les modifications.
	 */
	public void updateComputer(ComputerBean computerBean);
	/**
	 * Cette méthode supprime un computerBean de la table computer.
	 * @param computerBean Le bean à supprimer.
	 */
	public void deleteComputer(ComputerBean computerBean);
}
