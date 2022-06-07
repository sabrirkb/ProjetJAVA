<?php 
if(!isset($_REQUEST['action']))
{
	$_REQUEST['action'] = 'demandeConnexion';
}
$action = $_REQUEST['action'];
switch($action)
{
	case 'demandeConnexion':{
		include("vues/v_connexion.php");
		break;
	}
	case 'valideConnexion':{
		$login = $_REQUEST['login'];
		$mdp = hash('sha256', $_REQUEST['mdp']);
		$visiteur = $pdo->getInfosVisiteur($login,$mdp);
		if(!is_array( $visiteur)){
			ajouterErreur("Login ou mot de passe incorrect");
			include("vues/v_erreurs.php");
			include("vues/v_connexion.php");
		}
		else{
			$id = $visiteur['VIS_MATRICULE'];
			$nom =  $visiteur['VIS_NOM'];
			$prenom = $visiteur['VIS_PRENOM'];
            $_SESSION['login']= $login; // mémorise les variables de session
			$_SESSION['vis_matricule']= $id;
            $_SESSION['nom']= $nom;
            $_SESSION['prenom']= $prenom;
			$_SESSION['role_vis'] = $pdo->getRoleUtilisateur($id);
			if ($_SESSION['role_vis'] == "Délégué")
			{
				$_SESSION['STAT_TYPELIEU'] = "ma région";
				$_SESSION['localite'] = $pdo->getRegionUtilisateur($id);
			}
			if ($_SESSION['role_vis'] == "Responsable")
			{
				$_SESSION['STAT_TYPELIEU'] = "mon secteur";
				$_SESSION['localite'] = $pdo->getSecteurUtilisateur($id);
			}
			if ($_SESSION['role_vis'] == "Visiteur")
			{
				$_SESSION['STAT_TYPELIEU'] = "ma région";
				$_SESSION['localite'] = $pdo->getRegionUtilisateur($id);
			}
			$_SESSION['region'] = $pdo->getRegionUtilisateur($id);
			$_SESSION['STAT_VISITE'] = 0;
			$_SESSION['STAT_MEDECIN'] = 0;
			$_SESSION['STAT_VISITEURS'] = 0;
			$_SESSION['STAT_DELEGUES'] = 0;
			$_SESSION['STAT_RESPONSABLES'] = 0;
			$_SESSION['STAT_VISITE_POURCENT'] = 0;
			$_SESSION['STAT_MEDECIN_POURCENT'] = 0;
			$_SESSION['STAT_VISITEURS_POURCENT'] = 0;
			$_SESSION['STAT_DELEGUES_POURCENT'] = 0;
			$_SESSION['STAT_RESPONSABLES_POURCENT'] = 0;
            include("controleurs/c_Sommaire.php");
			include("vues/v_accueil.php");
			}
		break;
	} 
	case 'deconnexion':
		{
			session_destroy();
		}
	default :{
		include("vues/v_connexion.php");
		break;
	}
}
?>